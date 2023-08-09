package com.kumofactory.cloud.blueprint.service;

import com.kumofactory.cloud.blueprint.dto.template.TemplatePreviewDto;
import org.springframework.data.domain.Pageable;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.List;

public interface AwsTemplateService {
    // blueprint 이름으로 조회
    List<TemplatePreviewDto> searchTemplateFromKumofactory(Pageable pageable) throws S3Exception;

//    void searchTemplateFromUser();

    List<TemplatePreviewDto> searchTemplateFromTemplateName(Pageable pageable, String templateName) throws S3Exception;
}
