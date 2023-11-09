package com.kumofactory.cloud.global.rabbitmq;

import com.kumofactory.cloud.blueprint.dto.aws.AwsCdkDto;
import com.kumofactory.cloud.global.rabbitmq.domain.AwsCdkRequestMessage;
import com.kumofactory.cloud.global.rabbitmq.domain.CdkMessagePattern;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageProducer {
    private final AmqpTemplate rabbitTemplate;
    private final String queueName = "kumofactory-queue";

    public void sendAwsCdkOption(CdkMessagePattern pattern, List<AwsCdkDto> list) throws JsonProcessingException {
        AwsCdkRequestMessage message = AwsCdkRequestMessage.buildAwsCdkRequestDto(pattern, list);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(message);
//        String format = String.format("{\"pattern\" : \"%s\", %s}", pattern, s);
        System.out.println("sendAwsCdkOption: " + s);
        rabbitTemplate.convertAndSend(queueName, s);
    }
}
