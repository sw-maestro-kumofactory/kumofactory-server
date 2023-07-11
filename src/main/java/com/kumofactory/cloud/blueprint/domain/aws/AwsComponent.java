package com.kumofactory.cloud.blueprint.domain.aws;

import com.kumofactory.cloud.blueprint.domain.BluePrint;
import com.kumofactory.cloud.blueprint.domain.ComponentPoint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsComponentDto;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AwsComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    @Column(unique = true)
    private String ui_id; // Client 에서 생성하는 uuid

    @Enumerated(EnumType.STRING)
    private AwsComponentType type; // Component 타입 (vm, vpc, subnet, ...)

    // Component 의 좌측 상단 좌표
    private Integer position_x;
    private Integer position_y;

    @ManyToOne
    private BluePrint bluePrint;

    @OneToMany(mappedBy = "cspComponent", cascade = CascadeType.REMOVE)
    private List<ComponentPoint> componentPoint;

    // ============== 생성함수 ================= //
    public static AwsComponent createAwsComponent(AwsComponentDto awsComponentDto) {
        AwsComponent awsComponent = new AwsComponent();
        awsComponent.setUi_id(awsComponentDto.getId());
        awsComponent.setPosition_x(awsComponentDto.getX());
        awsComponent.setPosition_y(awsComponentDto.getY());
        awsComponent.setType(awsComponentDto.getType());
        return awsComponent;
    }
}
