package com.kumofactory.cloud.oauth.service.github;

import com.kumofactory.cloud.global.config.OAuthProvider;
import com.kumofactory.cloud.global.config.OauthConfig;
import com.kumofactory.cloud.oauth.dto.OAuthDto.GitHubToken;
import com.kumofactory.cloud.oauth.dto.UserInfoDto;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class GitHubServiceImpl implements GitHubService {

    private final OauthConfig oauthConfig;
    private final Logger logger = LoggerFactory.getLogger(GitHubServiceImpl.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public GitHubToken requestAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("client_id", oauthConfig.getGithubClientId());
        queryParams.add("client_secret", oauthConfig.getGithubClientSecretId());
        queryParams.add("code", code);
        logger.error("code : {}", code);
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(
                        oauthConfig.getGetAccessTokenUrlFromGithub())
                .queryParams(queryParams)
                .build();
        ResponseEntity<Map> response = restTemplate
                .postForEntity(uri.toUriString(), httpEntity, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("response : {}", response.getBody());
            return new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(response.getBody()),
                    GitHubToken.class);
        }
        logger.error("response : {}", response.getBody());
        logger.error("response : {}", response.getStatusCode());
        return null;
    }

    @Override
    public UserInfoDto requestUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(
                "https://api.github.com/user").build();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri.toUri())
                .headers(headers)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.exchange(requestEntity, JsonNode.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();
            logger.info("response : {}", response.getBody());
            if (responseBody != null) {
                String owner = responseBody.get("login").asText();
                String id = responseBody.get("id").asText();
                String provider = String.valueOf(OAuthProvider.GITHUB);
                return new UserInfoDto(id, provider, accessToken, owner);
            }
        }
        logger.error("response : {}", response.getBody());
        logger.error("response : {}", response.getStatusCode());
        return null;
    }
}
