package com.kumofactory.cloud.infra.service.network;

import software.amazon.awssdk.services.ec2.Ec2Client;

public interface Ec2Service {

    Ec2Client getEc2Client();

    String createVpc(Ec2Client client, String vpcName, String cidr);

    String createSubnet(Ec2Client client, String vpcId, String cidr, String subnetName,
                        String availabilityZone);

    String createRouteTable(Ec2Client client, String vpcId);

    void connectSubnetToRouteTable(Ec2Client client, String routeTableId, String subnetId);

    String createSecurityGroup(Ec2Client client, String vpcId, String securityGroupName,
                               String description);

    String createEc2Instance(Ec2Client ec2, String name, String amiId, String subnetId,
                             String securityGroupName);
}
