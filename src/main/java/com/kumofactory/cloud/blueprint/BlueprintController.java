package com.kumofactory.cloud.blueprint;

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
		private final MessageProducer sender;

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
		public String testMiddleware() {
				String message = "Hello World I'm test Message";
				try {
						sender.sendMessage(message);
						return "send message success";
				} catch (Exception e) {
						System.out.println(e);
						return "send message fail";
				}
		}

		@GetMapping("/test2")
		public String testMiddleware2() {
				String message = "Hello World I'm test Message2";
				try {
						sender.sendMessage2(message);
						return "send message success";
				} catch (Exception e) {
						System.out.println(e);
						return "send message fail";
				}
		}
}
