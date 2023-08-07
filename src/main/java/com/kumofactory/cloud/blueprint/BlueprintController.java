package com.kumofactory.cloud.blueprint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.service.AwsBlueprintService;
import com.kumofactory.cloud.global.annotation.auth.AuthorizationFromToken;
import com.kumofactory.cloud.global.rabbitmq.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprint")
@Slf4j
public class BlueprintController {
    private final Logger logger = LoggerFactory.getLogger(BlueprintController.class);
    private final AwsBlueprintService awsBlueprintService;

    @GetMapping("/aws/{uuid}")
    @AuthorizationFromToken
    public Object getAwsBlueprint(@PathVariable("uuid") String uuid, String userId) {
        return awsBlueprintService.getAwsBlueprint(uuid);
    }

    @GetMapping("/aws/list")
    @AuthorizationFromToken
    public Object getAwsBlueprintList(String userId) {
        logger.info("userId: {}", userId);
        return awsBlueprintService.getMyAwsBlueprints(userId);
    }

    @PostMapping("/aws")
    @AuthorizationFromToken
    public Object createAwsBlueprint(@RequestBody AwsBluePrintDto awsBluePrintDto, @RequestParam String provision, String userId) throws JsonProcessingException {
        logger.info(userId);
        awsBlueprintService.store(awsBluePrintDto, provision, userId);
        return "hello-world";
    }
}
