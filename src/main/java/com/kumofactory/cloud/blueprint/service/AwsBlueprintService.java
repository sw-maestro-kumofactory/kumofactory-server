package com.kumofactory.cloud.blueprint.service;


import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;

import java.util.List;

public interface AwsBlueprintService {

    AwsBluePrintDto getAwsBlueprint();

    List<AwsBluePrintDto> getMyAwsBlueprints();

    void store(AwsBluePrintDto awsBluePrintDto);
}
