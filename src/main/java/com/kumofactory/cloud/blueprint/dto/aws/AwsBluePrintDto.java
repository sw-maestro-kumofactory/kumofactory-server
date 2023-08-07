package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.ComponentLineDto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "AwsBluePrintDto", description = "AwsBluePrintDto")
public class AwsBluePrintDto {
    private String uuid;
    private String name;
    private ProvisionStatus status;
    private List<AwsComponentDto> components;
    private List<ComponentLineDto> links;

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
