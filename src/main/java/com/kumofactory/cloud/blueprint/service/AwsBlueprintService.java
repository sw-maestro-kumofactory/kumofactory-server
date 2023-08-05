package com.kumofactory.cloud.blueprint.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintListDto;

import java.util.List;

public interface AwsBlueprintService {

		AwsBluePrintDto getAwsBlueprint(Long bluePrintId);

		List<AwsBluePrintListDto> getMyAwsBlueprints(String userId);

		void store(AwsBluePrintDto awsBluePrintDto, String userId) throws JsonProcessingException;
}
