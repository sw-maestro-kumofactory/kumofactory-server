package com.kumofactory.cloud.infra.service.aws_cdk;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awscdk.services.ec2.BastionHostLinux;
import software.amazon.awscdk.services.ec2.BastionHostLinuxProps;
import software.amazon.awscdk.services.ec2.SubnetFilter;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetSelection.Builder;
import software.amazon.awscdk.services.ec2.Instance;

/**
 * AWS CDK를 이용한 EC2 서비스
 */
@Service
@Slf4j
public class Ec2ServiceImpl implements Ec2Service {

    private final Logger logger = LoggerFactory.getLogger(Ec2ServiceImpl.class);

    @Override
    public void createBastionHostLinux() {
        logger.info("================ Create BastionHostLinux Instance ================");
        List<String> subnetsId = new ArrayList<>();
        List<SubnetFilter> subnetFilters = new ArrayList<>();
        subnetsId.add("subnet-043aec505f84e38ac");
        SubnetFilter subnetFilter = SubnetFilter.byIds(subnetsId);
        subnetFilters.add(subnetFilter);
        BastionHostLinuxProps.builder().subnetSelection(SubnetSelection.builder().subnetFilters(subnetFilters).build());
    }

    @Override
    public void createEc2Instance() {
        logger.info("================ Create EC2 Instance ================");


    }
}
