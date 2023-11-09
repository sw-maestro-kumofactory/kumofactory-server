package com.kumofactory.cloud.global.rabbitmq;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.repository.aws.AwsBluePrintRepository;
import com.kumofactory.cloud.global.config.RabbitMQConfig;
import com.kumofactory.cloud.global.rabbitmq.domain.AwsCdkResponseDto;
import com.kumofactory.cloud.global.rabbitmq.domain.AwsCdkResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageListener {
    private final ObjectMapper objectMapper;
    private final AwsBluePrintRepository awsBluePrintRepository;
    private Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = RabbitMQConfig.awsCdkResultQueue)
    public void handleMessage(String message) throws JsonProcessingException {
        logger.info("Received message: {}", message);
        AwsCdkResponseDto awsCdkResponseDto = objectMapper.readValue(message, AwsCdkResponseDto.class);
        AwsCdkResult awsCdkResult = objectMapper.convertValue(awsCdkResponseDto.getData(), AwsCdkResult.class);
        logger.info("awsCdkResult: {}", awsCdkResult.getId());
        AwsBluePrint blueprint = awsBluePrintRepository.findAwsBluePrintByUuid(awsCdkResult.getId());
        if (blueprint != null) {
            blueprint.setStatus(ProvisionStatus.valueOf(awsCdkResult.getStatus()));
            awsBluePrintRepository.save(blueprint);
        }
    }
}