package com.kumofactory.cloud.auth;

import com.kumofactory.cloud.auth.jwt.dto.TokenDto;
import com.kumofactory.cloud.auth.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenDto refresh(String refreshToken) {
        if (refreshToken == null) {
            throw new RuntimeException("Token is null");
        }

        try {
            boolean isValidToken = jwtTokenProvider.validateToken(refreshToken);
            if (isValidToken) {
                Claims claims = jwtTokenProvider.getClaimsFormToken(refreshToken);
                String oauthId = claims.getSubject();
                return jwtTokenProvider.create(oauthId);
            }
        } catch (JwtException e) {
            return null;
        }


        return null;
    }
}
