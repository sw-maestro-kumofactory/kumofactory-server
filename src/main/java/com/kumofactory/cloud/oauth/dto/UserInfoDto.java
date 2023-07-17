package com.kumofactory.cloud.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInfoDto(
        @JsonProperty("id") String id,
        @JsonProperty("provider") String provider
){}
