package com.kumofactory.cloud.infra.service.aws_cdk.stack;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.ISubnet;
import software.amazon.awscdk.services.ec2.Subnet;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.constructs.Construct;

public class Ec2Stack extends Stack {
    public Ec2Stack(final Construct scope, String id) {
        super(scope, id);
        Vpc vpc = (Vpc) Vpc.fromLookup(this, "KumofactoryVPC", VpcLookupOptions.builder().vpcId("vpc-0719a1184fa5ccdcd").build());
        ISubnet kumofactorySubnet = Subnet.fromSubnetId(this, "KumofactorySubnet", "subnet-043aec505f84e38ac");
    }
}
