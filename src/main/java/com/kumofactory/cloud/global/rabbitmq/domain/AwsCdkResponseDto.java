package com.kumofactory.cloud.global.rabbitmq.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AwsCdkResponseDto {
    private String pattern;
    private Object data;
    private String id;

    public static AwsCdkResponseDto createResponse(String pattern, Object data, String id) {
        return AwsCdkResponseDto.builder()
                .pattern(pattern)
                .data(data)
                .id(id)
                .build();
    }

}

