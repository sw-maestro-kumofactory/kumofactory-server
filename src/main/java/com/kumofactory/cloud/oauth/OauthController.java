package com.kumofactory.cloud.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

  private final OauthService oauthService;

  @GetMapping("/github")
  public String getGithubAccessToken(@RequestParam("code") String code)
      throws JsonProcessingException {
    return oauthService.getAccessToken(code);
  }
}
