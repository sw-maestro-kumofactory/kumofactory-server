package com.kumofactory.cloud.blueprint.dto;

import javax.validation.constraints.NotNull;

import com.kumofactory.cloud.blueprint.domain.PointLink;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointLinkDto {

		@NotNull
		private String id;
		// Component uuid 값
		@NotNull
		private String source;
		@NotNull
		private String target;

		public static PointLinkDto mapper(PointLink pointLink) {
				PointLinkDto pointLinkDto = new PointLinkDto();
				pointLinkDto.setId(pointLink.getUi_id());
				pointLinkDto.setSource(pointLink.getSource_id());
				pointLinkDto.setTarget(pointLink.getDestination_id());
				return pointLinkDto;
		}
}
