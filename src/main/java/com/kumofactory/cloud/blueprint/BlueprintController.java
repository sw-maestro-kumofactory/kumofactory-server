package com.kumofactory.cloud.blueprint;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.service.AwsBlueprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprint")
public class BlueprintController {

		private final AwsBlueprintService awsBlueprintService;

		// TODO : 토큰에서 유저 정보 가져오는 로직 추가
		@GetMapping("/aws")
		public AwsBluePrintDto getAwsBlueprint() {
				try {
						AwsBluePrintDto awsBlueprint = awsBlueprintService.getAwsBlueprint();
						return awsBlueprint;
				} catch (RuntimeException e) {
						return null;
				}
		}

		@PostMapping("/aws")
		public String createAwsBlueprint(@RequestBody AwsBluePrintDto awsBluePrintDto) {
				awsBlueprintService.store(awsBluePrintDto);
				return "hello-world";
		}
}
