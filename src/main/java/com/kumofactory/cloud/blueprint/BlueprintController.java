package com.kumofactory.cloud.blueprint;

import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintListDto;
import com.kumofactory.cloud.blueprint.service.AwsBlueprintService;
import com.kumofactory.cloud.global.rabbitmq.domain.CdkMessagePattern;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import com.kumofactory.cloud.global.annotation.auth.AuthorizationFromToken;
import com.kumofactory.cloud.global.dto.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprint")
@Slf4j
public class BlueprintController {
    private final Logger logger = LoggerFactory.getLogger(BlueprintController.class);
    private final AwsBlueprintService awsBlueprintService;

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AwsBluePrintDto.class)))
    @GetMapping("/aws/{uuid}")
    @AuthorizationFromToken
    public AwsBluePrintDto getAwsBlueprint(@PathVariable("uuid") String uuid, String userId) {
        return awsBlueprintService.getAwsBlueprint(uuid, userId);
    }

    @GetMapping("/aws/status/{uuid}")
    @AuthorizationFromToken
    public ProvisionStatus getProvisionStatus(@PathVariable("uuid") String uuid, String userId) {
        return awsBlueprintService.getProvisionStatus(uuid, userId);
    }

    @Operation(
            summary = "BluePrint scope 업데이트",
            description = "Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/aws/{uuid}")
    @AuthorizationFromToken
    public ResultDto updateBluePrintScope(@PathVariable("uuid") String uuid, @RequestParam("scope") BluePrintScope scope, String userId) {
        boolean result = awsBlueprintService.updateBluePrintScope(scope, uuid, userId);
        return ResultDto.builder()
                .result(result)
                .message(!result ? "Not Found or Not Authorized" : "")
                .build();
    }

    @PostMapping("/aws")
    @AuthorizationFromToken
    public String createAwsBlueprint(@RequestBody AwsBluePrintDto awsBluePrintDto, String userId) {
        logger.info(userId);
        awsBlueprintService.store(awsBluePrintDto, userId);
        return "hello-world";
    }

    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/aws/list")
    @AuthorizationFromToken
    public List<AwsBluePrintListDto> getAwsBlueprintList(String userId) {
        try {
            return awsBlueprintService.getMyAwsBlueprints(userId);
        } catch (Exception e) {
            logger.error("getAwsBlueprintList error", e);
            throw e;
        }

    }

    @PostMapping("/aws")
    @AuthorizationFromToken
    public ResultDto createAwsBlueprint(@RequestBody AwsBluePrintDto awsBluePrintDto, @RequestParam String provision, String userId) throws JsonProcessingException {
        try {
            awsBlueprintService.store(awsBluePrintDto, provision, CdkMessagePattern.USER, userId);
            return ResultDto.builder()
                    .result(true)
                    .build();
        } catch (Exception e) {
            logger.error("createAwsBlueprint error", e);
            return ResultDto.builder()
                    .result(false)
                    .message(e.getMessage())
                    .build();
        }
    }
}
