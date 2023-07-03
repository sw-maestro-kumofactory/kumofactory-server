package com.kumofactory.cloud.infra.service.deploy.network;

import com.kumofactory.cloud.infra.dto.CreateVpcDto;

public interface DeployVpcService {

  // create vpc and return vpc id
  String createVpc(CreateVpcDto createVpcDto);
}
