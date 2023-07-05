package com.kumofactory.cloud.infra.service.network;

import software.amazon.awssdk.services.ec2.Ec2Client;

public interface Ec2Service {
		Ec2Client getEc2Client();

		/**
		 * @param client
		 * @param vpcName
		 * @param cidr
		 * @return 생성된 VPC ID 반환
		 */
		String createVpc(Ec2Client client, String vpcName, String cidr);

		String createSubnet(Ec2Client client, String vpcId, String cidr, String subnetName, String availabilityZone);

		String createRouteTable(Ec2Client client, String vpcId);
}
