package com.kumofactory.cloud.auth;

import com.kumofactory.cloud.auth.dto.ResultDto;
import com.kumofactory.cloud.auth.jwt.dto.TokenDto;

public interface AuthService {
    TokenDto refresh(String refreshToken);

    ResultDto withdraw(String accessToken);
}
