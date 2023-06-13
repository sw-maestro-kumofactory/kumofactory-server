package com.kumofactory.cloud.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.oauth.dto.TokenFromGithub;
import com.kumofactory.cloud.oauth.github.GithubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService{
  private final GithubService githubService;
  @Override
  public String getAccessToken(String code) throws JsonProcessingException {
    TokenFromGithub accessTokenFromGithub = githubService.getAccessTokenFromGithub(code);
    if(accessTokenFromGithub != null) {
      // TODO : 유저 ID 가져오고 Jwt 토큰 만들어서 access token 은 body, refresh token 은 cookie 에 담아서 전달하기
    }
    // TODO : 에러처리 -> null 값
    assert accessTokenFromGithub != null;
    return accessTokenFromGithub.getAccess_token();
  }

}
