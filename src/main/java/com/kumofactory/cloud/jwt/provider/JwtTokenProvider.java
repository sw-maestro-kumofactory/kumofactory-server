package com.kumofactory.cloud.jwt.provider;

import com.kumofactory.cloud.jwt.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

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

		public String getUserId(String token) {
				return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token).getBody().getSubject();
		}

		public boolean validateAccessToken(String token) {
				try {
						Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token);
						return !claims.getBody().getExpiration().before(new Date());
				} catch (Exception e) {
						return false;
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

		public String refreshAccessToken() {

				return null;
		}


}
