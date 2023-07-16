package com.kumofactory.cloud.blueprint;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.service.AwsBlueprintService;
import com.kumofactory.cloud.global.middleware.auth.AuthorizationFromToken;
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
		
		@GetMapping("/aws/{id}")
		@AuthorizationFromToken
		public AwsBluePrintDto getAwsBlueprint(@PathVariable("id") Long id) {
				try {
						logger.info("aws blue print id: {}", id);
						AwsBluePrintDto awsBlueprint = awsBlueprintService.getAwsBlueprint(id);
						return awsBlueprint;
				} catch (RuntimeException e) {
						return null;
				}
		}

		@GetMapping("/aws/list")
		@AuthorizationFromToken
		public List<AwsBluePrintDto> getAwsBlueprintList(String userId) {
				logger.info("userId: {}", userId);
				List<AwsBluePrintDto> awsBlueprintList = awsBlueprintService.getMyAwsBlueprints(userId);
				return awsBlueprintList;
		}

		@PostMapping("/aws")
		public String createAwsBlueprint(@RequestBody AwsBluePrintDto awsBluePrintDto) {
				awsBlueprintService.store(awsBluePrintDto);
				return "hello-world";
		}

		@GetMapping("/test")
		@AuthorizationFromToken
		public String testMiddleware(String userId) {
				System.out.printf("userId: %s\n", userId);
				return userId;
		}
}
