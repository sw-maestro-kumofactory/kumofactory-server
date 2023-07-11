package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.dto.PointLinkDto;
import java.util.List;
import lombok.Getter;

@Getter
public class AwsBluePrintDto {

    private List<AwsComponentDto> components;
    private List<PointLinkDto> links;
}
