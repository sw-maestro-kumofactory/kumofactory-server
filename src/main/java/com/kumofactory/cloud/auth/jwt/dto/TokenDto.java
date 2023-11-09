package com.kumofactory.cloud.auth.jwt.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String userEmail;
    private String accessToken;
    private String refreshToken;
}