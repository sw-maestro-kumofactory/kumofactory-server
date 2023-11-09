package com.kumofactory.cloud.blueprint.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintListDto;
import com.kumofactory.cloud.global.rabbitmq.domain.CdkMessagePattern;

import java.util.List;

public interface AwsBlueprintService {

    AwsBluePrintDto getAwsBlueprint(String uuid);

		List<AwsBluePrintListDto> getMyAwsBlueprints(String userId);

    void store(AwsBluePrintDto awsBluePrintDto, String provision, String userId) throws JsonProcessingException;

    boolean delete(String uuid); // blueprint uuid 로 모든 정보 삭세

    boolean updateBluePrintScope(BluePrintScope scope, String uuid, String userId);
}
