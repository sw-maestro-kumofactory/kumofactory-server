package com.kumofactory.cloud.blueprint.domain;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.PointLinkDto;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class PointLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    @Column(unique = true)
    private String ui_id; // Client 에서 생성하는 uuid

    private String source_id;
    private String destination_id;

    @ManyToOne
    private AwsBluePrint bluePrint;

    // ============== 생성함수 ================= //
    public static PointLink createPointLink(PointLinkDto pointLinkDto, AwsBluePrint bluePrint) {
        PointLink pointLink = new PointLink();
        pointLink.setUi_id(pointLinkDto.getId());
        pointLink.setSource_id(pointLinkDto.getSource());
        pointLink.setDestination_id(pointLinkDto.getTarget());
        pointLink.setBluePrint(bluePrint);
        return pointLink;
    }
}
