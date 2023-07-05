package com.kumofactory.cloud.infra.service.network;

import com.kumofactory.cloud.infra.constants.AwsAuth;
import com.kumofactory.cloud.infra.vo.CreateTagsRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class Ec2ServiceImpl implements Ec2Service {
		private final Logger logger = LoggerFactory.getLogger(Ec2ServiceImpl.class);
		private final AwsAuth awsAuth;

		@Override
		public Ec2Client getEc2Client() {
				// create credentials using root account
				AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(awsAuth.getAccessKey(), awsAuth.getSecretKey());
				StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);
				return Ec2Client.builder()
												.credentialsProvider(staticCredentialsProvider)
												.region(Region.AP_NORTHEAST_2)
												.build();
		}

		@Override
		public String createVpc(Ec2Client client, String vpcName, String cidr) {
				try {
						// create new vpc
						CreateVpcRequest request = CreateVpcRequest.builder().cidrBlock(cidr).build();
						CreateVpcResponse response = client.createVpc(request);

						// get vpc id and tag name for it
						String vpcId = response.vpc().vpcId();
						CreateTagsRequest createTagsRequest = new CreateTagsRequestVo(vpcId, vpcName).toCreateTagsRequest();
						client.createTags(createTagsRequest);

						logger.info("Success Created VPC ID: {}", vpcId);
						return vpcId;
				} catch (Exception e) {
						logger.error("Failed to create VPC: {}", e.getMessage());
						throw e;
				}

		}

		// todo : availabilityZone을 파라미터로 받아서 처리하도록 변경 필요
		@Override
		public String createSubnet(Ec2Client client, String vpcId, String cidr, String subnetName, String availabilityZone) {
				try {
						// create new subnet
						CreateSubnetRequest request = CreateSubnetRequest.builder().vpcId(vpcId).cidrBlock(cidr).availabilityZone(availabilityZone).build();
						CreateSubnetResponse response = client.createSubnet(request);

						// get subnet id and tag name
						String subnetId = response.subnet().subnetId();
						CreateTagsRequest createTagsRequest = new CreateTagsRequestVo(subnetId, subnetName).toCreateTagsRequest();
						client.createTags(createTagsRequest);

						logger.info("Success Created Subnet ID: {}", subnetId);
						return subnetId;
				} catch (Exception e) {
						logger.error("Failed to create Subnet: {}", e.getMessage());
						throw e;
				}

		}

		@Override
		public String createRouteTable(Ec2Client client, String vpcId) {
				return null;
		}
}
