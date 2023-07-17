package com.kumofactory.cloud.auth.jwt.provider;

import com.kumofactory.cloud.auth.jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.ac-token-validity-in-milliseconds}")
    private long VALIDITY;
    @Value("${jwt.re-token-validity-in-milliseconds}")
    private long REFRESH_VALIDITY;

    public TokenDto create(String id) {

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + VALIDITY))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (REFRESH_VALIDITY * 365)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

        return TokenDto.builder()
                .userEmail(id)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            return true;
        } catch (ExpiredJwtException exception) {
            logger.error("Token Expired");
            throw new ExpiredJwtException(exception.getHeader(), exception.getClaims(), exception.getMessage());
        } catch (JwtException exception) {
            logger.error("Token Tampered");
            return new JwtException(exception.getMessage()).getMessage().equals(exception.getMessage());
        } catch (NullPointerException exception) {
            logger.error("Token is null");
            return new NullPointerException().getMessage().equals(exception.getMessage());
        }
    }

    public Claims getClaimsFormToken(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token).getBody();
    }


}
