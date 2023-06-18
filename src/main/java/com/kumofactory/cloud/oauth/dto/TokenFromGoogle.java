package com.kumofactory.cloud.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenFromGoogle(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("scope") String scope,
        @JsonProperty("token_type") String tokenType
) {}
