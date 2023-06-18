package com.kumofactory.cloud.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.config.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final GoogleOauthServiceImpl googleOauthService;
    private final GithubOauthServiceImpl githubOauthService;

    public String request(SocialLoginType socialLoginType, String code)
            throws JsonProcessingException {

        switch (socialLoginType){
            case GOOGLE:{
                return googleOauthService.getAccessToken(code);
            }
            case GITHUB:{
                return githubOauthService.getAccessToken(code);
            }
            default:{
                throw new IllegalArgumentException("Unknown type of social login.");
            }
        }
    }
}