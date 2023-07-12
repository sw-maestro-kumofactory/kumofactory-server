package com.kumofactory.cloud.blueprint.dto;

import javax.validation.constraints.NotNull;

import com.kumofactory.cloud.blueprint.domain.ComponentPoint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentPointDto {

		@NotNull
		private String id;
		@NotNull
		private Integer x;
		@NotNull
		private Integer y;

		public static ComponentPointDto mapper(ComponentPoint componentPoint) {
				ComponentPointDto componentPointDto = new ComponentPointDto();
				componentPointDto.setId(componentPoint.getUi_id());
				componentPointDto.setX(componentPoint.getPosition_x());
				componentPointDto.setY(componentPoint.getPosition_y());
				return componentPointDto;
		}
}
