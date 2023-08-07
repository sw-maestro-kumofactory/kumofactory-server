package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.AccessScope;
import com.kumofactory.cloud.blueprint.domain.aws.AvailabilityZone;
import com.kumofactory.cloud.blueprint.domain.aws.AwsArea;
import com.kumofactory.cloud.blueprint.domain.aws.AwsAreaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AwsAreaDto", description = "AwsAreaDto")
public class AwsAreaDto {
    private String id;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    private AwsAreaType type;
    private AccessScope scope;
    private AvailabilityZone az;

    // todo : 필요하면 option 추가

    public static AwsAreaDto mapper(AwsArea area) {
        AwsAreaDto dto = new AwsAreaDto();
        dto.setId(area.getUi_id());
        dto.setX(area.getPosition_x());
        dto.setY(area.getPosition_y());
        dto.setWidth(area.getWidth());
        dto.setHeight(area.getHeight());
        dto.setType(area.getType());
        dto.setScope(area.getScope());
        dto.setAz(area.getAz());
        return dto;
    }
}
