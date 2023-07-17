package com.kumofactory.cloud.blueprint.dto;

import com.kumofactory.cloud.blueprint.domain.ComponentDot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentDotDto {
    private Integer x;
    private Integer y;
    private String componentId;

    public static ComponentDotDto mapper(ComponentDot componentDot) {
        ComponentDotDto componentDotDto = new ComponentDotDto();
        componentDotDto.setX(componentDot.getX());
        componentDotDto.setY(componentDot.getY());
        componentDotDto.setComponentId(componentDot.getComponentId());
        return componentDotDto;
    }
}
