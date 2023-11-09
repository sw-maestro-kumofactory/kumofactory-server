package com.kumofactory.cloud.blueprint.domain.aws;

import com.kumofactory.cloud.blueprint.dto.aws.AwsComponentDto;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
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

    @Enumerated(STRING)
    private AwsComponentType type; // Component 타입 (vm, vpc, subnet, ...)

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> options;

    // Component 의 좌측 상단 좌표
    private Integer position_x;
    private Integer position_y;

    @ManyToOne
    private AwsBluePrint bluePrint;

    // ============== 생성함수 ================= //
    public static AwsComponent createAwsComponent(AwsComponentDto awsComponentDto, AwsBluePrint awsBluePrint) {
        AwsComponent awsComponent = new AwsComponent();
        awsComponent.setUi_id(awsComponentDto.getId());
        awsComponent.setPosition_x(awsComponentDto.getX());
        awsComponent.setPosition_y(awsComponentDto.getY());
        awsComponent.setType(awsComponentDto.getType());
        awsComponent.setOptions(awsComponentDto.getOptions());
        awsComponent.setBluePrint(awsBluePrint);
        return awsComponent;
    }
}
