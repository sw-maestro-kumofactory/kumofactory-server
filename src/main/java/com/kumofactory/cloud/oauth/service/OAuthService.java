package com.kumofactory.cloud.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.global.config.OAuthProvider;
import com.kumofactory.cloud.auth.jwt.dto.TokenDto;
import com.kumofactory.cloud.auth.jwt.provider.JwtTokenProvider;
import com.kumofactory.cloud.member.MemberRepository;
import com.kumofactory.cloud.member.domain.Member;
import com.kumofactory.cloud.oauth.dto.UserInfoDto;
import com.kumofactory.cloud.oauth.service.github.GitHubService;
import com.kumofactory.cloud.oauth.service.google.GoogleService;

import static com.kumofactory.cloud.oauth.dto.OAuthDto.*;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthService {
		private final GoogleService googleService;
		private final GitHubService githubService;

		private final JwtTokenProvider jwtTokenProvider;
		private final MemberRepository memberRepository;

		private TokenDto token;

		public ResponseEntity<String> request(OAuthProvider provider, String code)
						throws JsonProcessingException {

				HttpHeaders responseHeaders = new HttpHeaders();

				Pair<TokenDto, UserInfoDto> pair = null;
				switch (provider) {
						case GOOGLE: {
								pair = getAccessTokenFromGoogle(code);
								break;
						}
						case GITHUB: {
								pair = getAccessTokenFromGitHub(code);
								break;
						}
						default: {
								throw new IllegalArgumentException("Unknown type of social login.");
						}
				}
				token = pair.getFirst();
				UserInfoDto userInfo = pair.getSecond();

				responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");

				ResponseCookie refreshTokenCookie = ResponseCookie
								.from("refreshToken", token.getRefreshToken())
								.httpOnly(true)
								.path("/")
								.maxAge(Duration.ofDays(365))
								.build();

				responseHeaders.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

				Map<String, String> map = new HashMap<>();
				map.put("accessToken", token.getAccessToken());
				map.put("profileName", userInfo.profileName());

				return ResponseEntity.ok()
														 .headers(responseHeaders)
														 .body(new ObjectMapper().writeValueAsString(map));
		}

		public TokenDto generateTestToken() {
				UserInfoDto userInfoDto = new UserInfoDto("1234", "github", "access", "coding-convention");
				return jwtTokenProvider.create(userInfoDto.id());
		}

		// TODO : 중복된 코드 간소화 (getAccessTokenFromXXX)
		private Pair<TokenDto, UserInfoDto> getAccessTokenFromGoogle(String code)
						throws JsonProcessingException {

				GoogleToken accessTokenFromGoogle = googleService.requestAccessToken(code);
				if (accessTokenFromGoogle != null) {

						UserInfoDto userInfo = googleService.requestUserInfo(accessTokenFromGoogle.accessToken());
						saveMember(userInfo);

						return Pair.of(jwtTokenProvider.create(userInfo.id()), userInfo);
				}
				return null;
		}

		private Pair<TokenDto, UserInfoDto> getAccessTokenFromGitHub(String code)
						throws JsonProcessingException {

				GitHubToken accessTokenFromGithub = githubService.requestAccessToken(code);
				if (accessTokenFromGithub != null) {

						UserInfoDto userInfo = githubService.requestUserInfo(accessTokenFromGithub.accessToken());
						saveMember(userInfo);

						return Pair.of(jwtTokenProvider.create(userInfo.id()), userInfo);
				}
				return null;
		}

		// 처음 가입한 member 일때만 저장
		private void saveMember(UserInfoDto userInfo) {
			Member member = memberRepository.findMemberByOauthId(userInfo.id());
			if (member == null) {
				member = Member.createMember(userInfo);
				memberRepository.save(member);
			} else if (member.getGithubAccessToken() != userInfo.accessToken()) {
				member.setGithubAccessToken(userInfo.accessToken());
				memberRepository.save(member);
			}
		}

		private String getProfileName(UserInfoDto userInfo) {
			return memberRepository.findMemberByOauthId(userInfo.id()).getProfileName();
		}
}
