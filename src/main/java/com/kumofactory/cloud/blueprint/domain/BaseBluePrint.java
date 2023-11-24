package com.kumofactory.cloud.blueprint.domain;

import com.kumofactory.cloud.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class BaseBluePrint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date created_at;

    @UpdateTimestamp
    private Date updated_at;

    private String uuid; // Client 에서 생성하는 uuid

    private String name; // 블루프린트 이름

    @Lob
    private String description; // 블루프린트 설명

    private String keyName; // 썸네일 이미지 파일명 (S3)

    @Column(columnDefinition = "int default 0")
    private Integer downloadCount; // 다운로드 횟수

    @Enumerated(EnumType.STRING)
    private ProvisionStatus status; // 배포 상태

    @Enumerated(EnumType.STRING)
    private BluePrintScope scope; // 블루프린트 공개 범위

    private Boolean isTemplate;

    @ManyToOne
    private Member member;

}
