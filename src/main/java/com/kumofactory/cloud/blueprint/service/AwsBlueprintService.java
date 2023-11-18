package com.kumofactory.cloud.blueprint.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintListDto;
import com.kumofactory.cloud.global.rabbitmq.domain.CdkMessagePattern;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AwsBlueprintService {

    AwsBluePrintDto getAwsBlueprint(String uuid, String userId);

    List<AwsBluePrintListDto> getMyAwsBlueprints(String userId);

    void store(AwsBluePrintDto awsBluePrintDto, String provision, CdkMessagePattern pattern, String userId) throws IOException;

    boolean delete(String uuid); // blueprint uuid 로 모든 정보 삭세

    boolean updateBluePrintScope(BluePrintScope scope, String uuid, String userId);

    ProvisionStatus getProvisionStatus(String uuid, String userId);

    Object getInfraCost(String uuid, String userId);
}
