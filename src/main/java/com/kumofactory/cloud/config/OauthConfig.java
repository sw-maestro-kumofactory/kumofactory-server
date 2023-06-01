package com.kumofactory.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OauthConfig {
  @Value("${oauth.github.client.id}")
  private String githubClientId;
  @Value("${oauth.github.client.secret}")
  private String githubClientSecretId;
}
