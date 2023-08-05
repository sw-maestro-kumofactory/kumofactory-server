package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsComponentType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class AwsCdkDto {
		String id;
		AwsComponentType type;
		AccessScope scope;
		Map<String, Object> options;

		public static AwsCdkDto createAwsCdkDto(AwsComponentDto awsComponentDto) {
				AwsCdkDto awsCdkDto = new AwsCdkDto();
				awsCdkDto.setId(UUID.randomUUID().toString());
				awsCdkDto.setType(awsComponentDto.getType());
				awsCdkDto.setScope(awsComponentDto.getScope());
				awsCdkDto.setOptions(awsComponentDto.getOptions());
				return awsCdkDto;
		}
}
