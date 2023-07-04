package com.kumofactory.cloud.infra.dto;

import lombok.Getter;

@Getter
public class SuccessBuildTemplate {

  private String message;

  public SuccessBuildTemplate(String message) {
    this.message = message;
  }
}
