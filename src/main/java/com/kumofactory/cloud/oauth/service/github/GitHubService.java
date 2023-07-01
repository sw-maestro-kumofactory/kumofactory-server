package com.kumofactory.cloud.oauth.service.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.oauth.dto.OAuthDto.GitHubToken;
import com.kumofactory.cloud.oauth.dto.UserInfoDto;

public interface
GitHubService {
  GitHubToken requestAccessToken(String code) throws JsonProcessingException;
  UserInfoDto requestUserInfo(String code) throws JsonProcessingException;

}

