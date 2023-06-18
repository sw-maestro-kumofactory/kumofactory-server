package com.kumofactory.cloud.oauth.service.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.oauth.dto.TokenFromGithub;

public interface
GithubService {
  TokenFromGithub requestAccessToken(String code) throws JsonProcessingException;
}
