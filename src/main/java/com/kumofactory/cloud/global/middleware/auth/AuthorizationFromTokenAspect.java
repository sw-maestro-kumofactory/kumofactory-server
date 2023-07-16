package com.kumofactory.cloud.global.middleware.auth;

import com.kumofactory.cloud.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFromTokenAspect {
		private final JwtTokenProvider jwtTokenProvider;
		private final Logger logger = LoggerFactory.getLogger(AuthorizationFromTokenAspect.class);

		@Around(value = "@annotation(AuthorizationFromToken)")
		public Object applyAuthorizationFromToken(ProceedingJoinPoint joinPoint, AuthorizationFromToken AuthorizationFromToken) throws Throwable {
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
				String userId = getUserFromAccessToken(request.getHeader("Authorization"));
				if (userId == null) {
						throw new RuntimeException("유효하지 않은 토큰입니다.");
				}
				// 추출된 사용자 정보를 매개변수로 전달
				Object[] args = joinPoint.getArgs();
				args[0] = userId;

				return joinPoint.proceed(args);
		}

		// user oauth id 추출
		private String getUserFromAccessToken(String token) {
				String accessToken = token.split(" ")[1];
				boolean isValidate = jwtTokenProvider.validateAccessToken(accessToken);
				if (isValidate) {
						return jwtTokenProvider.getClaimsFormToken(accessToken).getSubject();
				}
				return null;
		}
}
