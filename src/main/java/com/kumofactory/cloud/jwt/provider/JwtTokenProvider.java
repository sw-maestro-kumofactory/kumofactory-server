package com.kumofactory.cloud.jwt.provider;

import com.kumofactory.cloud.jwt.dto.TokenDto;
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
    @Value("${jwt.token-validity-in-milliseconds}")
    private long VALIDITY;

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
                .setExpiration(new Date(now.getTime() + (VALIDITY * 365)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

        return TokenDto.builder()
                .userEmail(id)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateAccessToken(String token) {
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

    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshAccessToken(Claims claims) {
        String id = claims.getSubject();
        TokenDto tokenDto = create(id);
        return null;
    }

    public Claims getClaimsFormToken(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token).getBody();
    }


}
