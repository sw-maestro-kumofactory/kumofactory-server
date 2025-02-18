package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import com.kumofactory.cloud.blueprint.domain.aws.AwsArea;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.ComponentLineDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AwsBluePrintDto", description = "AwsBluePrintDto")
public class AwsBluePrintDto {

    private String uuid;
    private String name;
    private String description;
    private ProvisionStatus status;
    private BluePrintScope scope;
    private Integer downloadCount;
    private List<AwsAreaDto> areas;
    private List<AwsComponentDto> components;
    private List<ComponentLineDto> links;
    private String svgFile;
    private Boolean isTemplate;
    private String templateName; 

    public static AwsBluePrintDto build(AwsBluePrint blueprint, List<AwsArea> areas, List<AwsComponent> components, List<ComponentLine> links) {
        return AwsBluePrintDto.builder()
                .uuid(blueprint.getUuid())
                .name(blueprint.getName())
                .description(blueprint.getDescription())
                .status(blueprint.getStatus())
                .scope(blueprint.getScope())
                .areas(awsAreaDtosMapper(areas))
                .downloadCount(blueprint.getScope() == BluePrintScope.PUBLIC ? blueprint.getDownloadCount() : -1)
                .components(awsComponentDtosMapper(components))
                .links(componentLinkDtoListMapper(links))
                .isTemplate(blueprint.getIsTemplate() != null && blueprint.getIsTemplate())
                .templateName(blueprint.getTemplateName() == null ? "undefined" : blueprint.getTemplateName()) 
                .build();
    }

    public static List<AwsAreaDto> awsAreaDtosMapper(List<AwsArea> awsAreaDtos) {
        List<AwsAreaDto> awsAreaDtoList = new ArrayList<>();
        for (AwsArea awsAreaDto : awsAreaDtos) {
            awsAreaDtoList.add(AwsAreaDto.mapper(awsAreaDto));
        }
        return awsAreaDtoList;
    }

    public static List<AwsComponentDto> awsComponentDtosMapper(List<AwsComponent> awsComponentDtos) {
        List<AwsComponentDto> awsComponentDtoList = new ArrayList<>();
        for (AwsComponent awsComponent : awsComponentDtos) {
            awsComponentDtoList.add(AwsComponentDto.mapper(awsComponent));
        }
        return awsComponentDtoList;
    }

    public static List<ComponentLineDto> componentLinkDtoListMapper(List<ComponentLine> componentLinks) {
        List<ComponentLineDto> componentLinkDtoList = new ArrayList<>();
        for (ComponentLine pointLink : componentLinks) {
            componentLinkDtoList.add(ComponentLineDto.mapper(pointLink));
        }
        return componentLinkDtoList;
    }
}
