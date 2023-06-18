package com.kumofactory.cloud.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.config.SocialLoginType;
import com.kumofactory.cloud.oauth.service.OauthService;
import com.kumofactory.cloud.oauth.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

  private final SocialLoginService socialLoginService;

  @GetMapping("/{socialLoginType}")
  public String socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath,
                                    @RequestParam("code") String code)
          throws JsonProcessingException {
    SocialLoginType socialLoginType = SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
    return socialLoginService.request(socialLoginType, code);
  }
}
