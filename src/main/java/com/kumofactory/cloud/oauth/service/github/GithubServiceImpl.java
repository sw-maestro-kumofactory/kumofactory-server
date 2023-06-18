package com.kumofactory.cloud.oauth.service.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.config.OauthConfig;
import com.kumofactory.cloud.oauth.dto.TokenFromGithub;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

  private final OauthConfig oauthConfig;
  private final Logger logger = LoggerFactory.getLogger(GithubServiceImpl.class);
  private final RestTemplate restTemplate = new RestTemplate();

  @Override
  public TokenFromGithub getAccessTokenFromGithub(String code) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/json");
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("client_id", oauthConfig.getGithubClientId());
    queryParams.add("client_secret", oauthConfig.getGithubClientSecretId());
    queryParams.add("code", code);

    UriComponents uri = UriComponentsBuilder.fromHttpUrl(
                                                oauthConfig.getGetAccessTokenUrlFromGithub())
                                            .queryParams(queryParams)
                                            .build();
    ResponseEntity<Map> response = restTemplate
        .postForEntity(uri.toUriString(), httpEntity, Map.class);
    if (response.getStatusCode() == HttpStatus.OK){
      logger.info("response : {}", response.getBody());
      return new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(response.getBody()),
                                          TokenFromGithub.class);
    }
    logger.error("response : {}", response.getBody());
    logger.error("response : {}", response.getStatusCode());
    return null;
  }
}
