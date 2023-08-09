package com.kumofactory.cloud.blueprint;

import com.kumofactory.cloud.blueprint.dto.template.TemplatePreviewDto;
import com.kumofactory.cloud.blueprint.service.AwsTemplateService;
import com.kumofactory.cloud.global.dto.PagingDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "TemplateController", description = "TemplateController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
@Slf4j
public class TemplateController {
		private final AwsTemplateService templateService;
		private final Logger logger = LoggerFactory.getLogger(TemplateController.class);

		@GetMapping("/kumofactory")
		public List<TemplatePreviewDto> searchTemplateFromKumofactory(PagingDto page) {
				Pageable pageable = PagingDto.createPageAble(page);
				return templateService.searchTemplateFromKumofactory(pageable);
		}
}
