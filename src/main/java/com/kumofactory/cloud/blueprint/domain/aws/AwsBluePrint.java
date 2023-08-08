package com.kumofactory.cloud.blueprint.domain.aws;

import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import com.kumofactory.cloud.member.domain.Member;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AwsBluePrint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date created_at;

    @UpdateTimestamp
    private Date updated_at;

    private String uuid; // Client 에서 생성하는 uuid

    private String name; // 블루프린트 이름

    @Enumerated(EnumType.STRING)
    private ProvisionStatus status; // 배포 상태

    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "bluePrint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AwsArea> areas;

    @OneToMany(mappedBy = "bluePrint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AwsComponent> cspComponents;

    @OneToMany(mappedBy = "bluePrint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ComponentLine> lines;

}
