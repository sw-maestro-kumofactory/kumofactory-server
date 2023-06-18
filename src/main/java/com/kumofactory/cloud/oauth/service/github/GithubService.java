package com.kumofactory.cloud.oauth.service.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.oauth.dto.TokenFromGithub;

public interface
GithubService {
  TokenFromGithub getAccessTokenFromGithub(String code) throws JsonProcessingException;
}
