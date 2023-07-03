package com.kumofactory.cloud.infra.service.deploy.network;

import com.kumofactory.cloud.infra.dto.CreateVpcDto;
import com.kumofactory.cloud.infra.service.deploy.credential.AwsCredentialService;
import com.kumofactory.cloud.infra.vo.CreateTagsRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.CreateVpcRequest;
import software.amazon.awssdk.services.ec2.model.CreateVpcResponse;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Vpc;
import org.springframework.util.StringUtils;


@Service
@Slf4j
@RequiredArgsConstructor
public class DeployVpcServiceImpl implements DeployVpcService {

  private final Logger logger = LoggerFactory.getLogger(DeployVpcServiceImpl.class);
  private final AwsCredentialService awsCredentialService;

  @Override
  public String createVpc(CreateVpcDto createVpcDto) {
    Ec2Client ec2Client = getEc2Client(createVpcDto.getRegion(),
                                       createVpcDto.getAccessKey(),
                                       createVpcDto.getSecretKey());

    String vpcId = buildVpc(ec2Client, createVpcDto.getCidrBlock(), createVpcDto.getVpcName());
    // 생성한 VPC 정보 출력
    DescribeVpcsRequest describeVpcsRequest = DescribeVpcsRequest.builder()
                                                                 .vpcIds(vpcId)
                                                                 .build();

    DescribeVpcsResponse describeVpcsResponse = ec2Client.describeVpcs(describeVpcsRequest);
    Vpc vpc2 = describeVpcsResponse.vpcs().get(0);

    logger.info("Created VPC:");
    logger.info("VPC ID: " + vpc2.vpcId());
    logger.info("CIDR Block: " + vpc2.cidrBlock());
    logger.info("State: " + vpc2.state());
    logger.info("Tags: " + vpc2.tags());
    return vpc2.vpcId();
  }

  private Ec2Client getEc2Client(String region, String accessKey, String secretKey) {
    if (accessKey == null || secretKey == null || accessKey.equals("") || secretKey.equals("")) {
      accessKey = "";
      secretKey = "";
    }
    if (!StringUtils.hasText(accessKey)) {
      return Ec2Client.builder()
                      .credentialsProvider(awsCredentialService.getRootCredential())
                      .region(Region.of(region))
                      .build();
    }
    return Ec2Client.builder()
                    .credentialsProvider(
                        awsCredentialService.getUserCredential(accessKey, secretKey))
                    .region(Region.of(region))
                    .build();
  }

  // vpc 배포하기
  private String buildVpc(Ec2Client client, String cidr, String vpcName) {
    CreateVpcRequest vpc = CreateVpcRequest.builder()
                                           .cidrBlock(cidr)
                                           .build(); // vpc 요청 인스턴스 생성
    CreateVpcResponse vpcResponse = client.createVpc(vpc); // vpc 생성
    String vpcId = vpcResponse.vpc().vpcId();
    // 생성된 VPC 에 태그 추가
    CreateTagsRequest createTagsRequest = new CreateTagsRequestVo(vpcId,
                                                                  vpcName).toCreateTagsRequest();
    client.createTags(createTagsRequest);
    return vpcId;
  }
}
