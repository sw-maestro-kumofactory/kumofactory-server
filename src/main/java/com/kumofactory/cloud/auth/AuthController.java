package com.kumofactory.cloud.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.auth.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                log.info("refreshToken: {}", cookie.getValue());
                refreshToken = cookie.getValue();
                break;
            }
        }
        TokenDto token = authService.refresh(refreshToken);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 30)
                .build();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        responseHeaders.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", token.getAccessToken());


        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new ObjectMapper().writeValueAsString(map));
    }
}
