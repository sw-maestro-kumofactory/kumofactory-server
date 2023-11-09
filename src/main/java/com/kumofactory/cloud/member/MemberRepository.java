package com.kumofactory.cloud.member;

import com.kumofactory.cloud.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findMemberById(Long id);

    Member findMemberByOauthId(String oauth_id);
}
