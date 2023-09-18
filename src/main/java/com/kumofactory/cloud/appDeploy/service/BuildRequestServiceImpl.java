package com.kumofactory.cloud.appDeploy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.kumofactory.cloud.appDeploy.domain.BuildLog;
import com.kumofactory.cloud.appDeploy.domain.CfnOutput;
import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;
import com.kumofactory.cloud.appDeploy.dto.SseResponseDto;
import com.kumofactory.cloud.appDeploy.repository.BuildLogRepository;
import com.kumofactory.cloud.appDeploy.repository.CfnOutputRepository;
import com.kumofactory.cloud.member.MemberRepository;
import com.kumofactory.cloud.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
@Repository
@RequiredArgsConstructor
public class BuildRequestServiceImpl implements BuildRequestService {
	private final MemberRepository memberRepository;
	private final CfnOutputRepository cfnOutputRepository;
	private final BuildLogRepository buildLogRepository;
	private final Logger logger = LoggerFactory.getLogger(BuildRequestServiceImpl.class);
	private String token;
	private String baseUri = "https://api.github.com";
	@Value("${build.server}")
	private String buildServerUri;

	@Override
	public void RequestBuild(BuildRequestDto request, String oauthId) {
		Member member = memberRepository.findMemberByOauthId(oauthId);
		this.token = member.getGithubAccessToken();
		String url = buildServerUri + "/api/v1/deploy";
		request.setDockerfile(isDockerfileExist(request.user(), request.repo()));
		request.setgithubToken(token);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<BuildRequestDto> httpEntity = new HttpEntity<>(request, headers);
		ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.POST, httpEntity, String.class);
	}

	@Override
	public CfnOutput getMyResources(String blueprintUuid, String oauthId) {
		logger.info("blueprintUuid: " + blueprintUuid);
		CfnOutput output = cfnOutputRepository.findByKey(blueprintUuid);
		return output;
	}

	private Boolean isDockerfileExist(String userName, String repoName) {
		String url = baseUri + "/repos/" + userName + "/" + repoName + "/contents/Dockerfile";
		try {
			ResponseEntity<JsonNode> response = RequestGitHubAPIs(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private ResponseEntity<JsonNode> RequestGitHubAPIs(String uri) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		if (StringUtils.hasText(token)) {
			headers.setBearerAuth(token);
		}
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		return new RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, JsonNode.class);
	}

	@Override
	public Flux<ServerSentEvent<String>> RequestBuildAsync(BuildRequestDto request, String oauthId) {
		WebClient webClient = WebClient.create();
		Member member = memberRepository.findMemberByOauthId(oauthId);
		this.token = member.getGithubAccessToken();
		String url = buildServerUri + "/api/v1/deploy";
		request.setDockerfile(isDockerfileExist(request.user(), request.repo()));
		request.setgithubToken(token);

		BuildLog buildLog = new BuildLog();
		buildLog.set_id(request.instanceId());
		buildLog.setStatus(0);
		buildLog.setRepository(request.user()+'/'+request.repo());
		buildLog.setBranch(request.branch());
		buildLogRepository.save(buildLog);

		Flux<SseResponseDto> sseFlux = webClient.post()
				.uri(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.TEXT_EVENT_STREAM)
				.bodyValue(request)
				.retrieve()
				.bodyToFlux(SseResponseDto.class);

		return sseFlux.map(response -> {
					logger.info("Received SSE: " + response.event() + response.data());
					if(response.event().equals("failed")) {
						buildLog.setStatus(-1);
						buildLogRepository.save(buildLog);
					} else if(response.event().equals("success")) {
						buildLog.setStatus(1);
						buildLogRepository.save(buildLog);
					}
					return ServerSentEvent.<String>builder()
							.event(response.event())
							.data(response.data())
							.build();
				})
				.takeUntil(response -> response.event().equals("failed") || response.event().equals("success"))
				.doOnError(error -> {
					error.printStackTrace();
				})
				.doOnTerminate(() -> {
					logger.info("SSE stream completed");
				});
	}

	@Override
	public Flux<ServerSentEvent<String>> getBuildStatus(String instanceId) {
		Optional<BuildLog> buildLogOpt = buildLogRepository.findByInstanceId(instanceId);
		// buildlog가 null이면 event: status, data:null 로 보내줌
		if (!buildLogOpt.isPresent()) {
			return Flux.just(
					ServerSentEvent.<String>builder()
							.event("status")
							.data("null")
							.build()
			);
		}
		BuildLog buildLog = buildLogOpt.get();
		if (buildLog.getStatus() == 0) {
			return Flux.interval(Duration.ofSeconds(3))
					.map(tick -> {
						BuildLog updatedLog = buildLogRepository.findByInstanceId(instanceId).orElse(null);
						return ServerSentEvent.<String>builder()
								.event("status")
								.data("ing")
								.build();
					});
		} else if (buildLog.getStatus() == 1) {
			return Flux.just(
					ServerSentEvent.<String>builder()
							.event("log-status")
							.data("success")
							.build()
			);
		} else if (buildLog.getStatus() == -1) {
			return Flux.just(
					ServerSentEvent.<String>builder()
							.event("log-status")
							.data("fail")
							.build()
			);
		} else {
			return Flux.empty();
		}
	}
}
