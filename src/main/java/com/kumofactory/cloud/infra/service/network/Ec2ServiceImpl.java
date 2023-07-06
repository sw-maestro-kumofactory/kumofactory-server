package com.kumofactory.cloud.infra.service.network;

import com.kumofactory.cloud.infra.constants.AwsAuth;
import com.kumofactory.cloud.infra.vo.CreateTagsRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.AllocateAddressRequest;
import software.amazon.awssdk.services.ec2.model.AllocateAddressResponse;
import software.amazon.awssdk.services.ec2.model.AssociateAddressRequest;
import software.amazon.awssdk.services.ec2.model.AssociateAddressResponse;
import software.amazon.awssdk.services.ec2.model.AssociateRouteTableRequest;
import software.amazon.awssdk.services.ec2.model.CreateRouteRequest;
import software.amazon.awssdk.services.ec2.model.CreateRouteTableRequest;
import software.amazon.awssdk.services.ec2.model.CreateRouteTableResponse;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupResponse;
import software.amazon.awssdk.services.ec2.model.CreateSubnetRequest;
import software.amazon.awssdk.services.ec2.model.CreateSubnetResponse;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.CreateVpcRequest;
import software.amazon.awssdk.services.ec2.model.CreateVpcResponse;
import software.amazon.awssdk.services.ec2.model.DomainType;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.IpRange;
import software.amazon.awssdk.services.ec2.model.NetworkInterface;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Tag;

@Service
@RequiredArgsConstructor
@Slf4j
public class Ec2ServiceImpl implements Ec2Service {

    private final Logger logger = LoggerFactory.getLogger(Ec2ServiceImpl.class);
    private final AwsAuth awsAuth;

    @Override
    public Ec2Client getEc2Client() {
        // create credentials using root account
        logger.info("ACCESS KEY {}", awsAuth.getAccessKey());
        logger.info("SECRET KEY {}", awsAuth.getSecretKey());
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(awsAuth.getAccessKey(),
                                                                             awsAuth.getSecretKey());
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(
            awsBasicCredentials);
        try {
            return Ec2Client.builder()
                            .credentialsProvider(ProfileCredentialsProvider.create())
                            .region(Region.AP_NORTHEAST_2)
                            .build();
        } catch (Ec2Exception e) {
            logger.error("Failed to create EC2 Client: {}", e.getMessage());
            throw e;
        }

    }

    @Override
    public String createVpc(Ec2Client client, String vpcName, String cidr) {
        try {
            // create new vpc
            CreateVpcRequest request = CreateVpcRequest.builder().cidrBlock(cidr).build();
            CreateVpcResponse response = client.createVpc(request);

            // get vpc id and tag name for it
            String vpcId = response.vpc().vpcId();
            CreateTagsRequest createTagsRequest = new CreateTagsRequestVo(vpcId,
                                                                          vpcName).toCreateTagsRequest();
            client.createTags(createTagsRequest);

            logger.info("Success Created VPC ID: {}", vpcId);
            return vpcId;
        } catch (Exception e) {
            logger.error("Failed to create VPC: {}", e.getMessage());
            throw e;
        }

    }

    @Override
    public String createSubnet(Ec2Client client, String vpcId, String cidr, String subnetName,
                               String availabilityZone) {
        try {
            // create new subnet
            CreateSubnetRequest request = CreateSubnetRequest.builder().vpcId(vpcId).cidrBlock(cidr)
                                                             .availabilityZone(availabilityZone)
                                                             .build();
            CreateSubnetResponse response = client.createSubnet(request);

            // get subnet id and tag name
            String subnetId = response.subnet().subnetId();
            CreateTagsRequest createTagsRequest = new CreateTagsRequestVo(subnetId,
                                                                          subnetName).toCreateTagsRequest();
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
        CreateRouteTableRequest request = CreateRouteTableRequest.builder().vpcId(vpcId).build();
        CreateRouteTableResponse routeTable = client.createRouteTable(request);
        return routeTable.routeTable().routeTableId();
    }

    @Override
    public void connectSubnetToRouteTable(Ec2Client client, String routeTableId, String subnetId) {
        AssociateRouteTableRequest request = AssociateRouteTableRequest.builder()
                                                                       .routeTableId(routeTableId)
                                                                       .subnetId(subnetId)
                                                                       .build();

        client.associateRouteTable(request);
    }

    @Override
    public String createSecurityGroup(Ec2Client client, String vpcId, String securityGroupName,
                                      String description) {
        CreateSecurityGroupRequest request = CreateSecurityGroupRequest.builder().vpcId(vpcId)
                                                                       .groupName(securityGroupName)
                                                                       .description(
                                                                           description)
                                                                       .build();
        CreateSecurityGroupResponse securityGroup = client.createSecurityGroup(request);
        String securityGroupId = securityGroup.groupId();
        IpRange ipRange = IpRange.builder()
                                 .cidrIp("0.0.0.0/0")
                                 .build();

        IpPermission sshPermission = IpPermission.builder()
                                                 .ipProtocol("tcp")
                                                 .fromPort(22)
                                                 .toPort(22)
                                                 .ipRanges(ipRange)
                                                 .build();

        IpPermission http = IpPermission.builder()
                                        .ipProtocol("tcp")
                                        .toPort(80)
                                        .fromPort(80)
                                        .ipRanges(ipRange)
                                        .build();
        client.authorizeSecurityGroupIngress(
            r -> r.groupId(securityGroupId).ipPermissions(sshPermission, http));
        return securityGroupId;
    }

    @Override
    public String createEc2Instance(Ec2Client ec2, String name, String amiId, String subnetId,
                                    String securityGroupName) {
        AllocateAddressResponse allocateAddressResponse = ec2.allocateAddress(
            AllocateAddressRequest.builder().domain(DomainType.VPC).build()
        );
        String allocationId = allocateAddressResponse.allocationId();
        RunInstancesRequest runRequest = RunInstancesRequest.builder()
                                                            .imageId(amiId)
                                                            .subnetId(subnetId)
                                                            .instanceType(InstanceType.T2_MICRO)
                                                            .keyName("kumo-key")
                                                            .securityGroupIds(securityGroupName)
                                                            .maxCount(1)
                                                            .minCount(1)
                                                            .build();

        RunInstancesResponse response = ec2.runInstances(runRequest);
        String instanceId = response.instances().get(0).instanceId();
        Tag tag = Tag.builder()
                     .key("Name")
                     .value(name)
                     .build();
        CreateTagsRequest tagRequest = CreateTagsRequest.builder()
                                                        .resources(instanceId)
                                                        .tags(tag)
                                                        .build();

        AssociateAddressRequest associateRequest = AssociateAddressRequest.builder()
                                                                          .allocationId(
                                                                              allocationId)
                                                                          .instanceId(
                                                                              instanceId)
                                                                          .build();
        AssociateAddressResponse associateResponse = ec2.associateAddress(associateRequest);

        try {
            ec2.createTags(tagRequest);
            System.out.printf("Successfully started EC2 Instance %s based on AMI %s", instanceId,
                              amiId);

            return instanceId;

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }

        return "";
    }
}
