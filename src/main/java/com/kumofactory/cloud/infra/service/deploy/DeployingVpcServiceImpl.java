package com.kumofactory.cloud.infra.service.deploy;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.model
@Service
@Slf4j
public class DeployingVpcServiceImpl implements DeployingService{
    private final Logger logger = LoggerFactory.getLogger(DeployingVpcServiceImpl.class);
    @Override
    public void create() {
        Vpc.Biulder
    }
}
