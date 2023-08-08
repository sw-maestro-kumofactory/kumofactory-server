package com.kumofactory.cloud.blueprint.repository.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwsBluePrintRepository extends JpaRepository<AwsBluePrint, Long> {
    AwsBluePrint findAwsBluePrintById(long id);

    AwsBluePrint findAwsBluePrintByUuid(String uuid);

    AwsBluePrint findAwsBluePrintByMemberId(long memberId);

    List<AwsBluePrint> findAwsBluePrintsByMember(Member member);

}
