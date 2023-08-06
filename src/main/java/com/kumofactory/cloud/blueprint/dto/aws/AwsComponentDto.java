package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponentType;
import com.kumofactory.cloud.global.annotation.validation.EnumValidator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

// todo : Area Component Dto 추가
@Getter
@Setter
public class AwsComponentDto {

    @NotNull
    private String id;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    @NotNull
    private AwsComponentType type;

    @EnumValidator(enumClass = AwsComponentType.class)
    private AccessScope scope;

    @EnumValidator(enumClass = AvailabilityZone.class)
    private AvailabilityZone az;

    private Map<String, Object> options;

    public static AwsComponentDto mapper(AwsComponent awsComponent) {
        AwsComponentDto awsComponentDto = new AwsComponentDto();
        awsComponentDto.setId(awsComponent.getUi_id());
        awsComponentDto.setX(awsComponent.getPosition_x());
        awsComponentDto.setY(awsComponent.getPosition_y());
        awsComponentDto.setType(awsComponent.getType());
        awsComponentDto.setAz(awsComponent.getAz());
        awsComponentDto.setScope(awsComponent.getScope());
        awsComponentDto.setOptions(awsComponent.getOptions());
        return awsComponentDto;
    }
}
