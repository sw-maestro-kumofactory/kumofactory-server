package com.kumofactory.cloud.blueprint.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ComponentPointDto {

    @NotNull
    private String id;
    @NotNull
    private Integer x;
    @NotNull
    private Integer y;
}
