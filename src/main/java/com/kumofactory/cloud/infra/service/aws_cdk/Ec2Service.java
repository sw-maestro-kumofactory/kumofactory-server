package com.kumofactory.cloud.infra.service.aws_cdk;

public interface Ec2Service {

    void createBastionHostLinux();

    void createEc2Instance();
}
