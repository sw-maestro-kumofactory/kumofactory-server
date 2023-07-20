package com.kumofactory.cloud.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.auth.jwt.dto.TokenDto;
import com.kumofactory.cloud.global.middleware.auth.AuthorizationFromToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/refresh")
    public Object refresh(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        try {
            Cookie[] cookies = request.getCookies();
            String refreshToken = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    log.info("take refreshToken: {}", cookie.getValue());
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
            log.info("create accessToken: {}", token.getAccessToken());

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(new ObjectMapper().writeValueAsString(map));

        } catch (RuntimeException e) {
            log.info("Refresh Token is expired");
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token is expired");
                }
            }
        }

        return null;
    }

    @GetMapping("/logout")
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("refreshToken")) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                return ResponseEntity.ok().body("logout");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("logout");
    }

    @DeleteMapping("/withdrawal")
    @AuthorizationFromToken
    public Object withdrawal(String userId) {
        return authService.withdraw(userId);
    }
}
