package com.kumofactory.cloud.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "oauth")
public class OauthConfig {
    @Value("${oauth.github.client_id}")
    private String githubClientId;
    @Value("${oauth.github.client_secret_id}")
    private String githubClientSecretId;
    @Value("${oauth.github.get_access_token_url}")
    private String getAccessTokenUrlFromGithub;

    @Value("${oauth.github.user_info_endpoint}")
    private String getUserInfoUrlFromGitHub;

    @Value("${oauth.google.client_id}")
    private String googleClientId;
    @Value("${oauth.google.client_secret_id}")
    private String googleClientSecretId;
    @Value("${oauth.google.redirect_url}")
    private String googleOauthRedirectUrl;
    @Value("${oauth.google.get_access_token_url}")
    private String getAccessTokenUrlFromGoogle;

    @Value("${oauth.google.user_info_endpoint}")
    private String getUserInfoUrlFromGoogle;
}
