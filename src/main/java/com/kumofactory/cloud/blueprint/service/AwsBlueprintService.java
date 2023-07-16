package com.kumofactory.cloud.blueprint.service;


import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;

import java.util.List;

public interface AwsBlueprintService {

		AwsBluePrintDto getAwsBlueprint(Long bluePrintId);

		List<AwsBluePrintDto> getMyAwsBlueprints(String userId);

		void store(AwsBluePrintDto awsBluePrintDto);
}
