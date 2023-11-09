package com.kumofactory.cloud.oauth.service.github;

import com.kumofactory.cloud.oauth.dto.UserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.oauth.dto.OAuthDto.GitHubToken;

public interface
GitHubService {
    GitHubToken requestAccessToken(String code) throws JsonProcessingException;

    UserInfoDto requestUserInfo(String code) throws JsonProcessingException;

}

