package com.kumofactory.cloud.infra.service.deploy;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeployingAwsServiceImpl implements DeployingService{
    private final Logger logger = LoggerFactory.getLogger(DeployingAwsServiceImpl.class);
    @Override
    public void create() {

    }
}
