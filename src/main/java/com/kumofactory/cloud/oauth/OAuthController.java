package com.kumofactory.cloud.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.config.OAuthProvider;
import com.kumofactory.cloud.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

  private final OAuthService oauthService;

  @GetMapping("/{socialLoginType}")
  public ResponseEntity<String> socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath,
                                                    @RequestParam("code") String code)
          throws JsonProcessingException {
    OAuthProvider provider = OAuthProvider.valueOf(SocialLoginPath.toUpperCase());
    return oauthService.request(provider, code);
  }
}
