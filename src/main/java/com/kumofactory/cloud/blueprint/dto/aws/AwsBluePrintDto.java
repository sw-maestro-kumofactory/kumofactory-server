package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.dto.PointLinkDto;

import java.util.List;

import lombok.Data;
import lombok.Getter;


@Data
public class AwsBluePrintDto {

    private String name;
    private List<AwsComponentDto> components;
    private List<PointLinkDto> links;
}
