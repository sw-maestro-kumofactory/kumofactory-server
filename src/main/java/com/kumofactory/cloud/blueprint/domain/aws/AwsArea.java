package com.kumofactory.cloud.blueprint.domain.aws;

import com.kumofactory.cloud.blueprint.domain.AccessScope;
import com.kumofactory.cloud.blueprint.dto.aws.AwsAreaDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AwsArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date created_at;

    @UpdateTimestamp
    private Date updated_at;

    @Enumerated(EnumType.STRING)
    private AwsAreaType type;

    @Enumerated(EnumType.STRING)
    private AccessScope scope;

    @Enumerated(EnumType.STRING)
    private AvailabilityZone az;

    // ==== Diagram 에서 사용하는 정보 ==== //
    private String ui_id; // Client 에서 생성하는 uuid
    private Integer position_x;
    private Integer position_y;
    private Integer width;
    private Integer height;

    @ManyToOne
    private AwsBluePrint bluePrint;

    public static AwsArea createAwsArea(AwsAreaDto dto, AwsBluePrint bluePrint) {
        AwsArea area = new AwsArea();
        area.setUi_id(dto.getId());
        area.setPosition_x(dto.getX());
        area.setPosition_y(dto.getY());
        area.setWidth(dto.getWidth());
        area.setHeight(dto.getHeight());
        area.setType(dto.getType());
        area.setScope(dto.getScope());
        area.setAz(dto.getAz());
        area.setBluePrint(bluePrint);
        return area;
    }

}
