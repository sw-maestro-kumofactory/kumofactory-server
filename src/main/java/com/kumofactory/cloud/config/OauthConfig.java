package com.kumofactory.cloud.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "oauth.github")
public class OauthConfig {
  @Value("${oauth.github.client_id}")
  private String githubClientId;
  @Value("${oauth.github.client_secret_id}")
  private String githubClientSecretId;
  @Value("${oauth.github.get_access_token_url}")
  private String getAccessTokenUrlFromGithub;
}
