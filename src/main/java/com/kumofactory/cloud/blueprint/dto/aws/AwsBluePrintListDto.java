package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@Schema(name = "AwsBluePrintListDto", description = "AwsBluePrintListDto")
public class AwsBluePrintListDto {

    private Long id;
    private String uuid;
    private String name;
    private String description;
    private Integer downloadCount;
    private BluePrintScope scope;
    private Date createdAt;
    private Date updatedAt;
    private String presignedUrl;
    private ProvisionStatus status;
    private Boolean isTemplate;
    private String templateName;

    public static AwsBluePrintListDto fromAwsBluePrint(AwsBluePrint awsBluePrint, String presignedUrl) {
        return AwsBluePrintListDto.builder()
                .id(awsBluePrint.getId())
                .uuid(awsBluePrint.getUuid())
                .name(awsBluePrint.getName())
                .description(awsBluePrint.getDescription())
                .downloadCount(awsBluePrint.getDownloadCount())
                .scope(awsBluePrint.getScope())
                .scope(awsBluePrint.getScope())
                .createdAt(awsBluePrint.getCreated_at())
                .updatedAt(awsBluePrint.getUpdated_at())
                .presignedUrl(presignedUrl)
                .status(awsBluePrint.getStatus())
                .isTemplate(awsBluePrint.getIsTemplate() != null && awsBluePrint.getIsTemplate())
                .templateName(awsBluePrint.getTemplateName() == null ? "undefined" : awsBluePrint.getTemplateName())
                .build();
    }
}
