package com.kumofactory.cloud.oauth.service.google;

import com.kumofactory.cloud.oauth.dto.UserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import static com.kumofactory.cloud.oauth.dto.OAuthDto.GoogleToken;

public interface
GoogleService {
    GoogleToken requestAccessToken(String code) throws JsonProcessingException;

    UserInfoDto requestUserInfo(String code) throws JsonProcessingException;
}
