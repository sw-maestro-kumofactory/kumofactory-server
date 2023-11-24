package com.kumofactory.cloud.blueprint.dto.template;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class TemplatePreviewDto {
    private String uuid;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private String username;
    private BluePrintScope scope;
    private String presignedUrl;
    private String description;
    private Integer downloadCount;

    public static TemplatePreviewDto mapper(AwsBluePrint bluePrint, String thumbnailUrl) {
        return TemplatePreviewDto.builder()
                .uuid(bluePrint.getUuid())
                .name(bluePrint.getName())
                .createdAt(bluePrint.getCreated_at())
                .updatedAt(bluePrint.getUpdated_at())
                .username(bluePrint.getMember().getProfileName())
                .description(bluePrint.getDescription())
                .downloadCount(bluePrint.getDownloadCount())
                .scope(bluePrint.getScope())
                .presignedUrl(thumbnailUrl)
                .build();
    }
}
