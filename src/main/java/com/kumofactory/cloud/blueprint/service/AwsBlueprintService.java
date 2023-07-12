package com.kumofactory.cloud.blueprint.service;


import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;

public interface AwsBlueprintService {

    void getAwsBlueprint();

    void store(AwsBluePrintDto awsBluePrintDto);
}
