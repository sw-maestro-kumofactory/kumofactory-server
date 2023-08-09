package com.kumofactory.cloud.blueprint.dto.template;

import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TemplatePreviewDto {
    private String uuid;
    private String name;
    private BluePrintScope scope;
    private String thumbnail_url;

    public static TemplatePreviewDto mapper(AwsBluePrint bluePrint, String thumbnailUrl) {
        return TemplatePreviewDto.builder()
                .uuid(bluePrint.getUuid())
                .name(bluePrint.getName())
                .scope(bluePrint.getScope())
                .thumbnail_url(thumbnailUrl)
                .build();
    }
}
