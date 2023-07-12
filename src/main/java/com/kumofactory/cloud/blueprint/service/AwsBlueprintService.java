package com.kumofactory.cloud.blueprint.service;


import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;

public interface AwsBlueprintService {

		AwsBluePrintDto getAwsBlueprint();

		void store(AwsBluePrintDto awsBluePrintDto);
}
