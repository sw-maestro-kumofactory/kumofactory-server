package com.kumofactory.cloud.infra.controller;

import com.kumofactory.cloud.infra.dto.SuccessBuildTemplate;
import com.kumofactory.cloud.infra.service.deploy.template.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aws/template")
@RequiredArgsConstructor
public class AwsTemplateController {

  private final TemplateService templateService;

  @PostMapping("/build")
  public ResponseEntity<SuccessBuildTemplate> buildTemplate() {
    try {
      templateService.createThreeTierArchitecture();
      SuccessBuildTemplate successBuildTemplate = new SuccessBuildTemplate("Success");
      return ResponseEntity.ok().body(successBuildTemplate);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().build();
    }
  }
}
