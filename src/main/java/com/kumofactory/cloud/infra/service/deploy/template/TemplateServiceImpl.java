package com.kumofactory.cloud.infra.service.deploy.template;

import com.kumofactory.cloud.infra.constants.AwsAuth;

import com.kumofactory.cloud.infra.service.network.Ec2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathRequest;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;
import software.amazon.awssdk.services.ssm.model.SsmException;
import software.amazon.awssdk.services.ssm.paginators.GetParametersByPathIterable;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import software.amazon.awssdk.services.sts.model.StsException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateServiceImpl implements TemplateService {
		private final Ec2Service ec2Service;
		private final AwsAuth awsAuth;
		private Logger logger = LoggerFactory.logger(TemplateServiceImpl.class);

		/**
		 * * 1. Creates an RSA key pair and saves the private key data as a .pem file. * 2. Lists key
		 * pairs. * 3. Creates a security group for the default VPC. * 4. Displays security group
		 * information. * 5. Gets a list of Amazon Linux 2 AMIs and selects one. * 6. Gets more
		 * information about the image. * 7. Gets a list of instance types that are compatible with the
		 * selected AMI’s architecture. * 8. Creates an instance with the key pair, security group, AMI,
		 * and an instance type. * 9. Displays information about the instance. * 10. Stops the instance
		 * and waits for it to stop. * 11. Starts the instance and waits for it to start. * 12. Allocates
		 * an Elastic IP address and associates it with the instance. * 13. Displays SSH connection info
		 * for the instance.
		 */
		/**
		 * //    ObjectMapper objectMapper = new ObjectMapper();
		 * //    ObjectNode jsonObject = objectMapper.createObjectNode();
		 * //    jsonObject.put("region", Region.AP_NORTHEAST_2.toString());
		 * //    jsonObject.put("vpcName", "newVpc");
		 * //    jsonObject.put("cidrBlock", "10.0.0.0/16");
		 * //    jsonObject.put("accessKey", "");
		 * //    jsonObject.put("secretKey", "");
		 * //
		 * //    CreateVpcDto createVpcDto = objectMapper.convertValue(jsonObject, CreateVpcDto.class);
		 * //    logger.info("CreateVpcDto : " + createVpcDto.toString());
		 *
		 * @throws Exception
		 */

		@Override
		public void createThreeTierArchitecture() throws Exception {
				try {
						Ec2Client client = ec2Service.getEc2Client();
						String vpcId = ec2Service.createVpc(client, "newVpc", "10.0.0.0/16");

						// 가용영역 2군데에 각각 public, private 서브넷 생성
						String publicSubnet2aId = ec2Service.createSubnet(client, vpcId, "10.0.1.0/24", "newPublicSubnet2a", "ap-northeast-2a");
						String privateSubnet2aId = ec2Service.createSubnet(client, vpcId, "10.0.3.0/24", "newPrivateSubnet2a", "ap-northeast-2a");
						String publicSubnet2bId = ec2Service.createSubnet(client, vpcId, "10.0.2.0/24", "newPublicSubnet2b", "ap-northeast-2b");
						String privateSubnet2bId = ec2Service.createSubnet(client, vpcId, "10.0.4.0/24", "newPrivateSubnet2b", "ap-northeast-2b");

						
				} catch (Exception e) {
						System.err.println(e.getMessage());
						System.exit(1);
				}
		}

		private void createKeyPair(Ec2Client ec2, String keyName, String fileName) throws Exception {
				try {
						CreateKeyPairRequest request = CreateKeyPairRequest.builder()
																															 .keyName(keyName)
																															 .build();

						CreateKeyPairResponse response = ec2.createKeyPair(request);
						String content = response.keyMaterial();
						BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
						writer.write(content);
						writer.close();
						System.out.println("Successfully created key pair named " + keyName);

				} catch (Ec2Exception | IOException e) {
						System.err.println(e.getMessage());
						throw new Exception("createKeyPair", e);
				}
		}

		private String createEC2SecurityGroup(Ec2Client ec2, String groupName, String groupDesc,
																					String vpcId) throws Exception {
				try {

						// snippet-start:[ec2.java2.create_security_group.create]
						CreateSecurityGroupRequest createRequest = CreateSecurityGroupRequest.builder()
																																								 .groupName(groupName)
																																								 .description(groupDesc)
																																								 .vpcId(vpcId)
																																								 .build();

						CreateSecurityGroupResponse resp = ec2.createSecurityGroup(createRequest);
						// snippet-end:[ec2.java2.create_security_group.create]

						// snippet-start:[ec2.java2.create_security_group.config]
						IpRange ipRange = IpRange.builder()
																		 .cidrIp("0.0.0.0/0").build();

						IpPermission ipPerm = IpPermission.builder()
																							.ipProtocol("tcp")
																							.toPort(80)
																							.fromPort(80)
																							.ipRanges(ipRange)
																							.build();

						IpPermission ipPerm2 = IpPermission.builder()
																							 .ipProtocol("tcp")
																							 .toPort(22)
																							 .fromPort(22)
																							 .ipRanges(ipRange)
																							 .build();

						AuthorizeSecurityGroupIngressRequest authRequest =
										AuthorizeSecurityGroupIngressRequest.builder()
																												.groupName(groupName)
																												.ipPermissions(ipPerm, ipPerm2)
																												.build();

						AuthorizeSecurityGroupIngressResponse authResponse =
										ec2.authorizeSecurityGroupIngress(authRequest);
						System.out.printf("Successfully added ingress policy to Security Group %s", groupName);
						return resp.groupId();

				} catch (Ec2Exception e) {
						System.err.println(e.awsErrorDetails().errorMessage());
						throw new Exception("createEC2SecurityGroup", e);
				}
		}

		// Display the Description field that corresponds to the instance Id value.
		private String describeImage(Ec2Client ec2, String instanceId) throws Exception {
				try {
						DescribeImagesRequest imagesRequest = DescribeImagesRequest.builder()
																																			 .imageIds(instanceId)
																																			 .build();

						DescribeImagesResponse response = ec2.describeImages(imagesRequest);
						System.out.println(
										"The description of the first image is " + response.images().get(0).description());
						System.out.println("The name of the first image is " + response.images().get(0).name());

						// Return the image Id value.
						return response.images().get(0).imageId();

				} catch (SsmException e) {
						System.err.println(e.getMessage());
						throw new Exception("describeImage", e);
				}
		}

		// Get a list of instance types.
		private String getInstanceTypes(Ec2Client ec2) throws Exception {
				String instanceType = "";
				try {
						List<Filter> filters = new ArrayList<>();
						Filter filter = Filter.builder()
																	.name("processor-info.supported-architecture")
																	.values("arm64")
																	.build();

						filters.add(filter);
						DescribeInstanceTypesRequest typesRequest = DescribeInstanceTypesRequest.builder()
																																										.filters(filters)
																																										.maxResults(10)
																																										.build();

						DescribeInstanceTypesResponse response = ec2.describeInstanceTypes(typesRequest);
						List<InstanceTypeInfo> instanceTypes = response.instanceTypes();
						for (InstanceTypeInfo type : instanceTypes) {
								System.out.println(
												"The memory information of this type is " + type.memoryInfo().sizeInMiB());
								System.out.println("Network information is " + type.networkInfo().toString());
								instanceType = type.instanceType().toString();
						}

						return instanceType;

				} catch (SsmException e) {
						System.err.println(e.getMessage());
						throw new Exception("getInstanceTypes", e);
				}
		}

		// Get the Id value of an instance with amzn2 in the name.
		private String getParaValues(SsmClient ssmClient) throws Exception {
				try {
						GetParametersByPathRequest parameterRequest = GetParametersByPathRequest.builder()
																																										.path(
																																														"/aws/service/ami-amazon-linux-latest")
																																										.build();

						GetParametersByPathIterable responses = ssmClient.getParametersByPathPaginator(
										parameterRequest);
						for (GetParametersByPathResponse response : responses) {
								System.out.println("Test " + response.nextToken());
								List<Parameter> parameterList = response.parameters();
								for (Parameter para : parameterList) {
										System.out.println("The name of the para is: " + para.name());
										System.out.println("The type of the para is: " + para.type());
										if (filterName(para.name())) {
												return para.value();
										}
								}
						}

				} catch (SsmException e) {
						System.err.println(e.getMessage());
						throw new Exception("getParaValues", e);
				}
				return null;
		}

		// Return true if the name has amzn2 in it. For example:
		// /aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-arm64-gp2
		private boolean filterName(String name) {
				String[] parts = name.split("/");
				String myValue = parts[4];
				return myValue.contains("amzn2");
		}

		private String runInstance(Ec2Client ec2, String instanceType, String keyName, String groupName,
															 String amiId) throws Exception {
				try {
						RunInstancesRequest runRequest = RunInstancesRequest.builder()
																																.instanceType(instanceType)
																																.keyName(keyName)
																																.securityGroups(groupName)
																																.maxCount(1)
																																.minCount(1)
																																.imageId(amiId)
																																.build();

						RunInstancesResponse response = ec2.runInstances(runRequest);
						String instanceId = response.instances().get(0).instanceId();
						System.out.println(
										"Successfully started EC2 instance " + instanceId + " based on AMI " + amiId);
						return instanceId;

				} catch (SsmException e) {
						System.err.println(e.getMessage());
						throw new Exception("runInstance", e);
				}
		}
}
