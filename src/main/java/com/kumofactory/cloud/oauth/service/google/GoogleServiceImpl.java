package com.kumofactory.cloud.oauth.service.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.config.OauthConfig;
import com.kumofactory.cloud.oauth.dto.TokenFromGoogle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {

    private final OauthConfig oauthConfig;
    private final Logger logger = LoggerFactory.getLogger(GoogleServiceImpl.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public TokenFromGoogle getAccessTokenFormGoogle(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("client_id", oauthConfig.getGoogleClientId());
        queryParams.add("Client_secret", oauthConfig.getGoogleClientSecretId());
        queryParams.add("code", code);
        queryParams.add("redirect_uri", oauthConfig.getGoogleOauthRedirectUrl());
        queryParams.add("grant_type", "authorization_code");

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(
                                                    oauthConfig.getGetAccessTokenUrlFromGoogle())
                                                .queryParams(queryParams)
                                                .build();

        ResponseEntity<Map> response = restTemplate
                .postForEntity(uri.toUriString(), httpEntity, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("response : {}", response.getBody());
            return new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(response.getBody()),
                                                TokenFromGoogle.class);
        }
        logger.error("response : {}", response.getBody());
        logger.error("response : {}", response.getStatusCode());
        return null;
    }
}
