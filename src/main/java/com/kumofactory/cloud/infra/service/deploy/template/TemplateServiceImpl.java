package com.kumofactory.cloud.infra.service.deploy.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.kumofactory.cloud.infra.constants.AwsAuth;
import com.kumofactory.cloud.infra.dto.CreateVpcDto;
import com.kumofactory.cloud.infra.service.deploy.credential.AwsCredentialService;
import com.kumofactory.cloud.infra.service.deploy.network.DeployVpcService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
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
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;
import software.amazon.awssdk.services.sts.model.StsException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateServiceImpl implements TemplateService {

  private final DeployVpcService deployVpcService;
  private final AwsCredentialService awsCredentialService;
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
  @Override
  public void createThreeTierArchitecture() throws Exception {
    //    ObjectMapper objectMapper = new ObjectMapper();
    //    ObjectNode jsonObject = objectMapper.createObjectNode();
    //    jsonObject.put("region", Region.AP_NORTHEAST_2.toString());
    //    jsonObject.put("vpcName", "newVpc");
    //    jsonObject.put("cidrBlock", "10.0.0.0/16");
    //    jsonObject.put("accessKey", "");
    //    jsonObject.put("secretKey", "");
    //
    //    CreateVpcDto createVpcDto = objectMapper.convertValue(jsonObject, CreateVpcDto.class);
    //    logger.info("CreateVpcDto : " + createVpcDto.toString());
    try {
      StsClient stsClient = StsClient.builder()
                                     .region(Region.AP_NORTHEAST_2)
                                     .build();

      AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                                                       .roleArn(
                                                           "arn:aws:iam::aws:policy/AdministratorAccess")
                                                       .roleSessionName("helloworld")
                                                       .build();

      AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
      Credentials myCreds = roleResponse.credentials();

      // Display the time when the temp creds expire.
      Instant exTime = myCreds.expiration();
      String tokenInfo = myCreds.sessionToken();

      // Convert the Instant to readable date.
      DateTimeFormatter formatter =
          DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                           .withLocale(Locale.KOREA)
                           .withZone(ZoneId.systemDefault());

      formatter.format(exTime);
      System.out.println("The token " + tokenInfo + "  expires on " + exTime);

    } catch (StsException e) {
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
