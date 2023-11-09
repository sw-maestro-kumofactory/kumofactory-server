package com.kumofactory.cloud.auth;

import com.kumofactory.cloud.auth.dto.ResultDto;
import com.kumofactory.cloud.auth.jwt.dto.TokenDto;
import com.kumofactory.cloud.auth.jwt.provider.JwtTokenProvider;
import com.kumofactory.cloud.member.MemberRepository;
import com.kumofactory.cloud.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public TokenDto refresh(String refreshToken) {
        if (refreshToken == null) {
            throw new RuntimeException("Token is null");
        }

        try {
            boolean isValidToken = jwtTokenProvider.validateToken(refreshToken);
            if (isValidToken) {
                Claims claims = jwtTokenProvider.getClaimsFormToken(refreshToken);
                String oauthId = claims.getSubject();
                return jwtTokenProvider.create(oauthId);
            }
        } catch (JwtException e) {
            throw new RuntimeException("Token is not valid");
        }

        return null;
    }

    @Override
    public ResultDto withdraw(String accessToken) {
        if (accessToken == null) {
            throw new RuntimeException("Token is null");
        }

        try {
            Claims claims = jwtTokenProvider.getClaimsFormToken(accessToken);
            String oauthId = claims.getSubject();
            Member member = memberRepository.findMemberByOauthId(oauthId);

            if (member != null) {
                memberRepository.delete(member);
                return ResultDto.builder()
                        .result(true)
                        .build();
            }

        } catch (JwtException e) {
            return ResultDto.builder().result(false).message("Not Found").build();
        }

        return null;
    }
}
