package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponentType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

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

    public static AwsComponentDto mapper(AwsComponent awsComponent) {
        AwsComponentDto awsComponentDto = new AwsComponentDto();
        awsComponentDto.setId(awsComponent.getUi_id());
        awsComponentDto.setX(awsComponent.getPosition_x());
        awsComponentDto.setY(awsComponent.getPosition_y());
        awsComponentDto.setType(awsComponent.getType());
        return awsComponentDto;
    }
}
