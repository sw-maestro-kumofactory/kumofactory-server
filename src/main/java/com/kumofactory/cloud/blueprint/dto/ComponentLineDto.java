package com.kumofactory.cloud.blueprint.dto;

import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentLineDto {
    private String id; // Client 에서 생성하는 uuid
    private ComponentDotDto src;
    private ComponentDotDto dst;

    public static ComponentLineDto mapper(ComponentLine componentLink) {
        ComponentLineDto componentLinkDto = new ComponentLineDto();
        componentLinkDto.setId(componentLink.getUi_id());
        componentLinkDto.setSrc(ComponentDotDto.mapper(componentLink.getSource()));
        componentLinkDto.setDst(ComponentDotDto.mapper(componentLink.getDestination()));
        return componentLinkDto;
    }
}
