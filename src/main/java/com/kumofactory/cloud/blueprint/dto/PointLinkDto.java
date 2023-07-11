package com.kumofactory.cloud.blueprint.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PointLinkDto {

    @NotNull
    private String id;
    // Component uuid 값
    @NotNull
    private String source;
    @NotNull
    private String target;
}
