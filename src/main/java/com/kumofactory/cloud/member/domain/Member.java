package com.kumofactory.cloud.member.domain;

import com.kumofactory.cloud.blueprint.domain.BluePrint;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long social_id;

    @OneToMany(mappedBy = "member")
    private List<BluePrint> bluePrints;
}
