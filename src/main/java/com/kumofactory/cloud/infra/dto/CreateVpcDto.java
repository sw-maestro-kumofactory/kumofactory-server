package com.kumofactory.cloud.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateVpcDto {

  @JsonProperty("region")
  private String region = "ap-northeast-2";
  @JsonProperty("vpcName")
  private String vpcName = "kumo-vpc";
  @JsonProperty("cidrBlock")
  private String cidrBlock = "10.0.0.0/16";
  @JsonProperty("accessKey")
  private String accessKey;
  @JsonProperty("secretKey")
  private String secretKey;
}
