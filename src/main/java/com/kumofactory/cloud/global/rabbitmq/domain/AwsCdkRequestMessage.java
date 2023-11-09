package com.kumofactory.cloud.global.rabbitmq.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.blueprint.dto.aws.AwsCdkDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AwsCdkRequestMessage {
    private String data; // cdk option 값
    private String pattern;

    public static AwsCdkRequestMessage buildAwsCdkRequestDto(CdkMessagePattern pattern, List<AwsCdkDto> body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(body);
        return AwsCdkRequestMessage.builder()
                .pattern(String.valueOf(pattern))
                .data(s)
                .build();
    }
}
