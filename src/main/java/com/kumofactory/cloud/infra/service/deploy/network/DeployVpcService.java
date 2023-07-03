package com.kumofactory.cloud.infra.service.deploy.network;

import com.kumofactory.cloud.infra.dto.CreateVpcDto;

public interface DeployVpcService {

  void createVpc(CreateVpcDto createVpcDto);
}
