package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.ComponentPoint;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponentType;
import com.kumofactory.cloud.blueprint.dto.ComponentPointDto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class AwsComponentDto {

		@NotNull
		private String id;

		@NotNull
		private Integer x;

		@NotNull
		private Integer y;

		@NotNull
		private AwsComponentType type;

		private List<ComponentPointDto> points;

		public static AwsComponentDto mapper(AwsComponent awsComponent) {
				AwsComponentDto awsComponentDto = new AwsComponentDto();
				awsComponentDto.setId(awsComponent.getUi_id());
				awsComponentDto.setX(awsComponent.getPosition_x());
				awsComponentDto.setY(awsComponent.getPosition_y());
				awsComponentDto.setType(awsComponent.getType());
				List<ComponentPointDto> points = new ArrayList<>();
				for (ComponentPoint componentPoint : awsComponent.getComponentPoint()) {
						points.add(ComponentPointDto.mapper(componentPoint));
				}
				awsComponentDto.setPoints(points);
				return awsComponentDto;
		}
}
