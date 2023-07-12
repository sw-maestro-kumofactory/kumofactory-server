package com.kumofactory.cloud.member.domain;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long social_id;

    @OneToMany(mappedBy = "member")
    private List<AwsBluePrint> bluePrints;
}
