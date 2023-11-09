package com.kumofactory.cloud.blueprint.service;

import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.dto.template.TemplatePreviewDto;
import org.springframework.data.domain.Pageable;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.List;

public interface AwsTemplateService {
    AwsBluePrintDto getAwsBlueprint(String uuid);

    // 전체 조회하기
    List<TemplatePreviewDto> getAll(Pageable pageable) throws S3Exception;

    // blueprint 이름으로 조회
    List<TemplatePreviewDto> searchTemplateFromKumofactory(Pageable pageable) throws S3Exception;

    List<TemplatePreviewDto> searchTemplateFromTemplateName(Pageable pageable, String templateName) throws S3Exception;
}
