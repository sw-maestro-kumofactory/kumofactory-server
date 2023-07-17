package com.kumofactory.cloud.auth.jwt.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRepository {
    private String email;
    private String userAgent;
    private String refreshToken;
}
