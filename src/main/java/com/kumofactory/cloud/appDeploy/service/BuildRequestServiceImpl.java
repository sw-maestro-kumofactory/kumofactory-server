package com.kumofactory.cloud.appDeploy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;
import com.kumofactory.cloud.member.MemberRepository;
import com.kumofactory.cloud.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@Repository
@RequiredArgsConstructor
public class BuildRequestServiceImpl implements BuildRequestService {

    private final MemberRepository memberRepository;
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

        logger.info(request.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<BuildRequestDto> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.POST, httpEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("response : {}", response.getBody());
        } else {
            logger.error("response : {}", response.getBody());
            logger.error("response : {}", response.getStatusCode());
        }
    }

    private Boolean isDockerfileExist(String userName ,String repoName) {
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
        if( StringUtils.hasText(token) ) {
            headers.setBearerAuth(token);
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        return new RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, JsonNode.class);
    }
}
