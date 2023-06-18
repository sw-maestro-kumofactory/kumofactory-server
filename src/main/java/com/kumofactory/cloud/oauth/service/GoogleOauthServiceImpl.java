package com.kumofactory.cloud.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.oauth.dto.TokenFromGoogle;
import com.kumofactory.cloud.oauth.service.google.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleOauthServiceImpl implements OauthService {
    private final GoogleService googleService;

    @Override
    public String getAccessToken(String code) throws JsonProcessingException{
        TokenFromGoogle accessTokenFromGoogle = googleService.getAccessTokenFormGoogle(code);
        if(accessTokenFromGoogle != null) {
            // TODO : 유저 ID 가져오고 Jwt 토큰 만들어서 access token 은 body, refresh token 은 cookie 에 담아서 전달하기
        }
        // TODO : 에러처리 -> null 값
        assert accessTokenFromGoogle != null;
        return accessTokenFromGoogle.accessToken();
    }
}
