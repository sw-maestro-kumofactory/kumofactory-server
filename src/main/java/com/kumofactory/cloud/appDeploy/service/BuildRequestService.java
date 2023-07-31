package com.kumofactory.cloud.appDeploy.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class BuildRequestService {
    private final Logger logger = LoggerFactory.getLogger(BuildRequestService.class);
    private String token;
    private String baseUri = "https://api.github.com";

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
