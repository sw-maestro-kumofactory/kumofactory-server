package com.kumofactory.cloud.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OauthService {
  String getAccessToken(String code) throws JsonProcessingException;
}
