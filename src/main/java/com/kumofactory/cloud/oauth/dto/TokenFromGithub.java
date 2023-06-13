package com.kumofactory.cloud.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenFromGithub {
  @JsonProperty
  private String access_token;
  @JsonProperty
  private String scope;
  @JsonProperty
  private String token_type;

}
