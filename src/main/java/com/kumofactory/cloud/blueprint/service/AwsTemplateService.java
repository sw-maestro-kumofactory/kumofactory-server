package com.kumofactory.cloud.blueprint.service;

import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.dto.template.TemplatePreviewDto;
import com.kumofactory.cloud.global.dto.ResultDto;
import org.springframework.data.domain.Pageable;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;

public interface AwsTemplateService {
    AwsBluePrintDto getAwsBlueprint(String uuid);


    ResultDto deployTemplate(AwsBluePrintDto dto, String templateName, boolean provision, String userId) throws IOException;


    // 전체 조회하기
    List<TemplatePreviewDto> getAll(Pageable pageable) throws S3Exception;

    // blueprint 이름으로 조회
    List<TemplatePreviewDto> searchTemplateFromKumofactory(Pageable pageable) throws S3Exception;

    List<TemplatePreviewDto> searchTemplateFromTemplateName(Pageable pageable, String templateName) throws S3Exception;
}
