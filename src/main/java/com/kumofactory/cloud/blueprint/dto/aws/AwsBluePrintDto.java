package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import com.kumofactory.cloud.blueprint.domain.aws.AwsArea;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.ComponentLineDto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@Builder
@Schema(name = "AwsBluePrintDto", description = "AwsBluePrintDto")
public class AwsBluePrintDto {

		private String uuid;
		private String name;
		private ProvisionStatus status;
		private BluePrintScope scope;
		private List<AwsAreaDto> areas;
		private List<AwsComponentDto> components;
		private List<ComponentLineDto> links;
		private String svgFile;

		public static AwsBluePrintDto build(AwsBluePrint blueprint, List<AwsArea> areas, List<AwsComponent> components, List<ComponentLine> links) {
				return AwsBluePrintDto.builder()
															.uuid(blueprint.getUuid())
															.name(blueprint.getName())
															.status(blueprint.getStatus())
															.scope(blueprint.getScope())
															.areas(awsAreaDtosMapper(areas))
															.components(awsComponentDtosMapper(components))
															.links(componentLinkDtoListMapper(links))
															.build();
		}

		public static List<AwsAreaDto> awsAreaDtosMapper(List<AwsArea> awsAreaDtos) {
				List<AwsAreaDto> awsAreaDtoList = new ArrayList<>();
				for (AwsArea awsAreaDto : awsAreaDtos) {
						awsAreaDtoList.add(AwsAreaDto.mapper(awsAreaDto));
				}
				return awsAreaDtoList;
		}

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
