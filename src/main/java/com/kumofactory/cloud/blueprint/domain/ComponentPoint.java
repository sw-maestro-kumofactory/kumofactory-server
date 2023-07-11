package com.kumofactory.cloud.blueprint.domain;

import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.ComponentPointDto;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ComponentPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    @Column(unique = true)
    private String ui_id; // Client 에서 생성하는 uuid

    // Component 의 찍힌 point 위치
    private Integer position_x;
    private Integer position_y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "csp_component_id")
    private AwsComponent cspComponent;

    // ============== 생성함수 ================= //
    public static ComponentPoint createComponentPoint(ComponentPointDto pointLinkDto,
                                                      AwsComponent cspComponent) {
        ComponentPoint componentPoint = new ComponentPoint();
        componentPoint.setUi_id(pointLinkDto.getId());
        componentPoint.setPosition_x(pointLinkDto.getX());
        componentPoint.setPosition_y(pointLinkDto.getY());
        componentPoint.setCspComponent(cspComponent);
        return componentPoint;
    }
}
