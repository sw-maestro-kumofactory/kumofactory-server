package com.kumofactory.cloud.appDeploy.service;

import com.kumofactory.cloud.appDeploy.domain.BuildLog;
import com.kumofactory.cloud.appDeploy.domain.CfnOutput;
import com.kumofactory.cloud.appDeploy.dto.SseResponseDto;
import com.kumofactory.cloud.appDeploy.repository.BuildLogRepository;
import com.kumofactory.cloud.appDeploy.repository.CfnOutputRepository;
import com.kumofactory.cloud.member.MemberRepository;
import com.kumofactory.cloud.member.domain.Member;
import com.fasterxml.jackson.databind.JsonNode;
import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
		logger.info("RequestBuildAsync");

		WebClient webClient = WebClient.create();
		Member member = memberRepository.findMemberByOauthId(oauthId);
		this.token = member.getGithubAccessToken();
		String url = buildServerUri + "/api/v1/deployAsync";
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
					if(response.event().equals("fail")) {
						logger.info("#@#@#@#@#@ fail");
						buildLog.setStatus(-1);
						buildLogRepository.save(buildLog);
					} else if(response.event().equals("success")) {
						logger.info("#@#@#@#@#@ success");
						buildLog.setStatus(1);
						buildLogRepository.save(buildLog);
					}
					return ServerSentEvent.<String>builder()
							.event(response.event())
							.data(response.data())
							.build();
				})
				.takeUntil(response -> response.event().equals("fail") || response.event().equals("success"))
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
			return Flux.just(createSSE("status", "null"));
		}

		BuildLog buildLog = buildLogOpt.get();

		if (buildLog.getStatus() == -1) {
			return Flux.just(createSSE("status", "fail"));
		} else if (buildLog.getStatus() == 0 || buildLog.getStatus() == 1) {
			return Flux.interval(Duration.ofSeconds(3))
					.map(tick -> {
						BuildLog updatedLog = buildLogRepository.findByInstanceId(instanceId).orElse(null);
						if (updatedLog == null) {
							return createSSE("status", "null");
						} else if (updatedLog.getStatus() == 2) {
							return createSSE("status", "success");
						} else if (updatedLog.getStatus() == 0) {
							return createSSE("status", "building");
						} else if (updatedLog.getStatus() == 1) {
							return createSSE("status", "deploying");
						} else {
							return createSSE("status", "unknown");
						}
					})
					.takeUntil(sse -> "success".equals(sse.data()));
		} else if (buildLog.getStatus() == 2) {
			return Flux.just(createSSE("status", "success"));
		} else {
			return Flux.empty();
		}
	}


	@Override
	public void RequestBuildAsync2(BuildRequestDto request, String oauthId) {
		logger.info("RequestBuildAsync");

		WebClient webClient = WebClient.create(buildServerUri);
		String endpoint = "/api/v1/deployAsync";

		Member member = memberRepository.findMemberByOauthId(oauthId);
		this.token = member.getGithubAccessToken();

		request.setDockerfile(isDockerfileExist(request.user(), request.repo()));
		request.setgithubToken(token);

		saveBuildLog(request, 0);

		Flux<ServerSentEvent<String>> events = webClient.post()
				.uri(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.TEXT_EVENT_STREAM)
				.bodyValue(request)
				.retrieve()
				.bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {});

		events.takeWhile(event -> !event.event().equals("finish"))
				.subscribe(event -> {
					logger.info("Received SSE: " + event.event() + " " + event.data());
					if ("fail".equals(event.event()) || "error".equals(event.event())) {
						saveBuildLog(request, -1);
					} else if ("success".equals(event.event())) {
						saveBuildLog(request, 1);
					}
				});
	}

	private void saveBuildLog(BuildRequestDto request, int status) {
		BuildLog buildLog = new BuildLog();
		buildLog.set_id(request.instanceId());
		buildLog.setStatus(status);
		buildLog.setRepository(request.user()+'/'+request.repo());
		buildLog.setBranch(request.branch());
		buildLogRepository.save(buildLog);
	}

	private ServerSentEvent<String> createSSE(String event, String data) {
		return ServerSentEvent.<String>builder()
				.event(event)
				.data(data)
				.build();
	}

	@Override
	public String getInstanceInfo(String instanceId) {
		Optional<BuildLog> buildLogOpt = buildLogRepository.findByInstanceId(instanceId);
		if (!buildLogOpt.isPresent()) {
			return null;
		}
		BuildLog buildLog = buildLogOpt.get();
		return buildLog.getRepository() + " " + buildLog.getBranch();
	}
}
