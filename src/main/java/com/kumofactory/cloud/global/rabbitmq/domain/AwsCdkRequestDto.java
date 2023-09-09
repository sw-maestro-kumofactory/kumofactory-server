package com.kumofactory.cloud.global.rabbitmq.domain;

import com.kumofactory.cloud.blueprint.dto.aws.AwsCdkDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AwsCdkRequestDto {
    private String pattern;
    private List<AwsCdkDto> body;

    public static AwsCdkRequestDto buildAwsCdkRequestDto(String pattern, List<AwsCdkDto> body) {
        return AwsCdkRequestDto.builder()
                .pattern(pattern)
                .body(body)
                .build();
    }
}
