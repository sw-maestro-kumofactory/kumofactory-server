package com.kumofactory.cloud.global.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.blueprint.dto.aws.AwsCdkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageProducer {
    private final AmqpTemplate rabbitTemplate;
    private final String queueName = "kumofactory-queue";

    public void sendAwsCdkOption(List<AwsCdkDto> list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(list);
        rabbitTemplate.convertAndSend(queueName, s);
    }
}
