package com.kumofactory.cloud.global.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponentType;
import com.kumofactory.cloud.blueprint.dto.aws.AccessScope;
import com.kumofactory.cloud.blueprint.dto.aws.AwsCdkDto;
import com.kumofactory.cloud.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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
