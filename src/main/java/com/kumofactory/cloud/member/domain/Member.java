package com.kumofactory.cloud.member.domain;

import com.kumofactory.cloud.blueprint.domain.BaseBluePrint;
import com.kumofactory.cloud.oauth.dto.UserInfoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthId;

    private String provider;

    private String githubAccessToken;

    private String profileName;

    @OneToMany(mappedBy = "member")
    private List<BaseBluePrint> bluePrints;

    // =========== 생성함수 =========== //
    public static Member createMember(UserInfoDto userInfoDto) {
        Member member = new Member();
        member.setOauthId(userInfoDto.id());
        member.setProvider(userInfoDto.provider());
        member.setGithubAccessToken(userInfoDto.accessToken());
        member.setProfileName(userInfoDto.profileName());
        return member;
    }
}
