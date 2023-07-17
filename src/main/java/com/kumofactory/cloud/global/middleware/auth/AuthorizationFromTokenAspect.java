package com.kumofactory.cloud.global.middleware.auth;

import com.kumofactory.cloud.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationFromTokenAspect {
    private final JwtTokenProvider jwtTokenProvider;

    @Around(value = "@annotation(AuthorizationFromToken)")
    public Object applyAuthorizationFromToken(ProceedingJoinPoint joinPoint, AuthorizationFromToken AuthorizationFromToken) throws Throwable {
        HttpServletRequest request = findHttpServletRequest(joinPoint.getArgs());
        HttpServletRequest header = findHttpServletRequest(joinPoint.getArgs());
        String accessToken = extractAccessTokenFromCookies(request.getCookies());
        String userId = getUserFromAccessToken(accessToken);

        // 추출된 사용자 정보를 매개변수로 전달
        Object[] args = joinPoint.getArgs();
        args[0] = userId;

        return joinPoint.proceed(args);
    }

    private HttpServletRequest findHttpServletRequest(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        throw new IllegalArgumentException("HttpServletRequest not found in method parameters");
    }

    private String extractAccessTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private String getUserFromAccessToken(String accessToken) {
        boolean isValidate = jwtTokenProvider.validateAccessToken(accessToken);
        if (isValidate) {
            return jwtTokenProvider.getClaimsFormToken(accessToken).getSubject();
        }
        return null;
    }
}
