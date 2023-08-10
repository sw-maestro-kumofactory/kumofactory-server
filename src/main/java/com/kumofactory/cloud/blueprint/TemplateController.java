package com.kumofactory.cloud.blueprint;

import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.dto.template.TemplatePreviewDto;
import com.kumofactory.cloud.blueprint.service.AwsTemplateService;
import com.kumofactory.cloud.global.annotation.auth.AuthorizationFromToken;
import com.kumofactory.cloud.global.dto.PagingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TemplateController", description = "TemplateController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
@Slf4j
public class TemplateController {
		private final AwsTemplateService templateService;
		private final Logger logger = LoggerFactory.getLogger(TemplateController.class);

		@Operation(
						summary = "Template 상세 정보 가져오기",
						description = "Requires authentication.",
						security = @SecurityRequirement(name = "bearerAuth")
		)
		@GetMapping("/template/{uuid}")
		@AuthorizationFromToken
		public AwsBluePrintDto getAwsBlueprint(@PathVariable String uuid, String userId) {
				return null;
		}

		@Operation(
						summary = "Template 전체 조회하기",
						description = "scope 가 PRIVATE 인 것은 조회하지 않음. example : ?page=0&size=10"
		)
		@GetMapping("/")
		public List<TemplatePreviewDto> getAll(PagingDto page) {
				Pageable pageable = PagingDto.createPageAble(page);
				return templateService.getAll(pageable);
		}

		@Operation(
						summary = "Kumofactory 에서 만든 Template 조회하기",
						description = "example : ?page=0&size=10"
		)
		@GetMapping("/kumofactory")
		public List<TemplatePreviewDto> searchTemplateFromKumofactory(PagingDto page) {
				Pageable pageable = PagingDto.createPageAble(page);
				return templateService.searchTemplateFromKumofactory(pageable);
		}

		@Operation(
						summary = "Template 이름으로 조회하기 ",
						description = "example : ?page=0&size=10&value=templateName"
		)
		@GetMapping("/name")
		public List<TemplatePreviewDto> searchTemplateByName(PagingDto page, @RequestParam("value") String templateName) {
				Pageable pageable = PagingDto.createPageAble(page);
				return templateService.searchTemplateFromTemplateName(pageable, templateName);
		}
}
