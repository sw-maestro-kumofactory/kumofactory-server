package com.kumofactory.cloud.global.middleware.auth;

import com.kumofactory.cloud.auth.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationFromTokenAspect {
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(AuthorizationFromTokenAspect.class);

    @Around(value = "@annotation(AuthorizationFromToken)")
    public Object applyAuthorizationFromToken(ProceedingJoinPoint joinPoint, AuthorizationFromToken AuthorizationFromToken) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        String userId = getUserFromAccessToken(request.getHeader("Authorization"));
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        }
        // 추출된 사용자 정보를 매개변수로 전달
        Object[] args = joinPoint.getArgs();
        args[args.length - 1] = userId;
        return joinPoint.proceed(args);
    }

    // user oauth id 추출
    private String getUserFromAccessToken(String token) {
        String accessToken = token.split(" ")[1];
        logger.info("get From Client accessToken: {}", accessToken);
        try {
            boolean isValidate = jwtTokenProvider.validateToken(accessToken);
            if (isValidate) {
                return jwtTokenProvider.getClaimsFormToken(accessToken).getSubject();
            }
        } catch (ExpiredJwtException exception) { // Access Token 이 만료됐을 때
            return null;
        } catch (JwtException e) { // Access Token 이 유효하지 않을 때
            return null;
        } catch (NullPointerException e) { // Access Token 이 null 일 때
            return null;
        }
        return null;
    }
}
