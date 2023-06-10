package com.kumofactory.cloud.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.oauth.dto.TokenFromGithub;

public interface OauthService {
  String getAccessToken(String code) throws JsonProcessingException;
}
