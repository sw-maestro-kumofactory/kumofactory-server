package com.kumofactory.cloud.blueprint.service;

import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.template.TemplatePreviewDto;
import com.kumofactory.cloud.blueprint.repository.aws.AwsBluePrintRepository;
import com.kumofactory.cloud.util.aws.s3.AwsS3Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsTemplateServiceImpl implements AwsTemplateService {
		private final AwsBluePrintRepository templateRepository;
		private final AwsS3Helper s3;
		private final Logger logger = LoggerFactory.getLogger(AwsTemplateServiceImpl.class);

		@Override
		public List<TemplatePreviewDto> searchTemplateFromKumofactory(Pageable pageable) throws S3Exception {
				List<AwsBluePrint> all = templateRepository.findAllByScope(BluePrintScope.KUMOFACTORY, pageable);
				List<TemplatePreviewDto> dtos = new ArrayList<>();
				try {
						for (AwsBluePrint awsBluePrint : all) {
								String url = s3.getPresignedUrl(awsBluePrint.getUuid());
								TemplatePreviewDto dto = TemplatePreviewDto.mapper(awsBluePrint, url);
								dtos.add(dto);
						}
						return dtos;
				} catch (S3Exception e) {
						logger.error("S3Exception: {}", e.getMessage());
						throw S3Exception.builder().build();
				}
		}

		@Override
		public List<TemplatePreviewDto> searchTemplateFromTemplateName(Pageable pageable, String templateName) {

				return null;
		}
}
