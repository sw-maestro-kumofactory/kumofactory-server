package com.kumofactory.cloud.oauth.service.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.oauth.dto.TokenFromGoogle;

public interface
GoogleService {
    TokenFromGoogle getAccessTokenFormGoogle(String code) throws JsonProcessingException;
}
