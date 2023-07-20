package com.kumofactory.cloud.blueprint;

import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.service.AwsBlueprintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprint")
@Slf4j
public class BlueprintController {
    private final Logger logger = LoggerFactory.getLogger(BlueprintController.class);
    private final AwsBlueprintService awsBlueprintService;

    @GetMapping("/aws/{id}")
    @AuthorizationFromToken
    public Object getAwsBlueprint(@PathVariable("id") Long id, String userId) {
        logger.info("aws blue print id: {}", id);
        AwsBluePrintDto awsBlueprint = awsBlueprintService.getAwsBlueprint(id);
        return awsBlueprint;
    }

    @GetMapping("/aws/list")
    @AuthorizationFromToken
    public Object getAwsBlueprintList(String userId) {
        logger.info("userId: {}", userId);
        return awsBlueprintService.getMyAwsBlueprints(userId);
    }

    @PostMapping("/aws")
    @AuthorizationFromToken
    public Object createAwsBlueprint(@RequestBody AwsBluePrintDto awsBluePrintDto, String userId) {
        logger.info(userId);
        awsBlueprintService.store(awsBluePrintDto, userId);
        return "hello-world";
    }

    @GetMapping("/test")
    @AuthorizationFromToken
    public String testMiddleware(String userId) {
        System.out.printf("userId: %s\n", userId);
        return userId;
    }
}
