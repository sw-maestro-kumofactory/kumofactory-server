package com.kumofactory.cloud.blueprint.service;

import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.dto.template.TemplatePreviewDto;
import com.kumofactory.cloud.blueprint.repository.ComponentLineRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsBluePrintRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsComponentRepository;
import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.aws.AwsArea;
import com.kumofactory.cloud.blueprint.repository.aws.AwsAreaRepository;
import com.kumofactory.cloud.util.aws.s3.AwsS3Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ComponentLineRepository componentLineRepository;
    private final AwsAreaRepository awsAreaRepository;
    private final AwsComponentRepository awsComponentRepository;
    private final AwsS3Helper s3;
    private final Logger logger = LoggerFactory.getLogger(AwsTemplateServiceImpl.class);

    @Override
    public AwsBluePrintDto getAwsBlueprint(String uuid) {
        AwsBluePrint awsBluePrintById = templateRepository.findAwsBluePrintByUuid(uuid);
        // Download 횟수 증가
        awsBluePrintById.setDownloadCount(awsBluePrintById.getDownloadCount() + 1);
        templateRepository.save(awsBluePrintById);

        List<AwsArea> awsAreas = awsAreaRepository.findAllByBluePrint(awsBluePrintById);
        List<AwsComponent> awsComponents = awsComponentRepository.findAllByBluePrint(awsBluePrintById);
        List<ComponentLine> componentLines = componentLineRepository.findAllByBluePrint(awsBluePrintById);

        return AwsBluePrintDto.build(awsBluePrintById, awsAreas, awsComponents, componentLines);
    }

    @Override
    public List<TemplatePreviewDto> getAll(Pageable pageable) throws S3Exception {
        List<AwsBluePrint> all = templateRepository.findAllByScopeNot(BluePrintScope.PRIVATE, pageable);
        return mapToTemplatePreviewDto(all);
    }

    @Override
    public List<TemplatePreviewDto> searchTemplateFromKumofactory(Pageable pageable) throws S3Exception {
        List<AwsBluePrint> all = templateRepository.findAllByScope(BluePrintScope.KUMOFACTORY, pageable);
        return mapToTemplatePreviewDto(all);
    }

    @Override
    public List<TemplatePreviewDto> searchTemplateFromTemplateName(Pageable pageable, String templateName) {
        List<AwsBluePrint> all = templateRepository.findAllByNameContainsAndScope(templateName, pageable, BluePrintScope.PUBLIC);
        return mapToTemplatePreviewDto(all);
    }

    // S3 에서 url 받아온 후 TemplatePreviewDto 로 변환
    private List<TemplatePreviewDto> mapToTemplatePreviewDto(List<AwsBluePrint> all) {
        List<TemplatePreviewDto> dtos = new ArrayList<>();
        try {
            for (AwsBluePrint awsBluePrint : all) {
                String url = s3.getPresignedUrl(awsBluePrint.getKeyName()); // get url from s3
                TemplatePreviewDto dto = TemplatePreviewDto.mapper(awsBluePrint, url);
                dtos.add(dto);
            }
            return dtos;
        } catch (S3Exception e) {
            logger.error("S3Exception: {}", e.getMessage());
            throw S3Exception.builder().build();
        }
    }
}
