package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsComponentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AwsCdkDto {
    String id;
    AwsComponentType type;
    Map<String, Object> options;

    public static AwsCdkDto createAwsCdkDto(AwsComponentDto awsComponentDto) {
        return AwsCdkDto.builder()
                .id(awsComponentDto.getId())
                .type(awsComponentDto.getType())
                .options(awsComponentDto.getOptions())
                .build();
    }
}
