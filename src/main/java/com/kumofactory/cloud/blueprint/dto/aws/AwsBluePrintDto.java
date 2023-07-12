package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.PointLink;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.PointLinkDto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;


@Data
public class AwsBluePrintDto {

		private String name;
		private List<AwsComponentDto> components;
		private List<PointLinkDto> links;

		public static List<AwsComponentDto> awsComponentDtosMapper(List<AwsComponent> awsComponentDtos) {
				List<AwsComponentDto> awsComponentDtoList = new ArrayList<>();
				for (AwsComponent awsComponent : awsComponentDtos) {
						awsComponentDtoList.add(AwsComponentDto.mapper(awsComponent));
				}
				return awsComponentDtoList;
		}

		public static List<PointLinkDto> pointLinkDtosMapper(List<PointLink> pointLinks) {
				List<PointLinkDto> pointLinkDtoList = new ArrayList<>();
				for (PointLink pointLink : pointLinks) {
						pointLinkDtoList.add(PointLinkDto.mapper(pointLink));
				}
				return pointLinkDtoList;
		}
}
