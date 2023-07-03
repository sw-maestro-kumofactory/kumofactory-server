package com.kumofactory.cloud.infra.service.deploy.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.kumofactory.cloud.infra.dto.CreateVpcDto;
import com.kumofactory.cloud.infra.service.deploy.credential.AwsCredentialService;
import com.kumofactory.cloud.infra.service.deploy.network.DeployVpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService{
    private final DeployVpcService deployVpcService;
    private final AwsCredentialService awsCredentialService;
    /**
     *  * 1. Creates an RSA key pair and saves the private key data as a .pem file.
     *  * 2. Lists key pairs.
     *  * 3. Creates a security group for the default VPC.
     *  * 4. Displays security group information.
     *  * 5. Gets a list of Amazon Linux 2 AMIs and selects one.
     *  * 6. Gets more information about the image.
     *  * 7. Gets a list of instance types that are compatible with the selected AMI’s architecture.
     *  * 8. Creates an instance with the key pair, security group, AMI, and an instance type.
     *  * 9. Displays information about the instance.
     *  * 10. Stops the instance and waits for it to stop.
     *  * 11. Starts the instance and waits for it to start.
     *  * 12. Allocates an Elastic IP address and associates it with the instance.
     *  * 13. Displays SSH connection info for the instance.
     */
    @Override
    public void createThreeTierArchitecture() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.put("region", "ap-north-east2");
        jsonObject.put("vpcName", "newVpc");
        jsonObject.put("cidrBlock", "10.0.0.0/16");
        jsonObject.put("accessKey", "");
        jsonObject.put("secretKey", "");

        CreateVpcDto createVpcDto = objectMapper.convertValue(jsonObject, CreateVpcDto.class);
        // create vpc and get vpc id
        String vpcId = deployVpcService.createVpc(createVpcDto);

        // create new ec2 client
        Ec2Client ec2 = Ec2Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(awsCredentialService.getRootCredential())
                .build();

        // create new ssm client
        SsmClient ssmClient = SsmClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(awsCredentialService.getRootCredential())
                .build();

        // create new key pair for new ec2
        createKeyPair(ec2, "newKeyPair", "newKeyPair");
        String sgId = createEC2SecurityGroup(ec2, "newSecurityGroup", "newSecurityGroup", vpcId);

        String instanceId = getParaValues(ssmClient);
        String amiValue = describeImage(ec2, instanceId);
        String instanceType = getInstanceTypes(ec2);

        String newInstanceId = runInstance(ec2, instanceType, "newKeyPair", "newSecurityGroup", amiValue );
    }

    private void createKeyPair(Ec2Client ec2, String keyName, String fileName) {
        try {
            CreateKeyPairRequest request = CreateKeyPairRequest.builder()
                    .keyName(keyName)
                    .build();

            CreateKeyPairResponse response = ec2.createKeyPair(request);
            String content = response.keyMaterial();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(content);
            writer.close();
            System.out.println("Successfully created key pair named "+keyName);

        } catch (Ec2Exception | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private String createEC2SecurityGroup( Ec2Client ec2,String groupName, String groupDesc, String vpcId) {
        try {

            // snippet-start:[ec2.java2.create_security_group.create]
            CreateSecurityGroupRequest createRequest = CreateSecurityGroupRequest.builder()
                    .groupName(groupName)
                    .description(groupDesc)
                    .vpcId(vpcId)
                    .build();

            CreateSecurityGroupResponse resp= ec2.createSecurityGroup(createRequest);
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
            System.exit(1);
        }
        return "";
    }

    // Display the Description field that corresponds to the instance Id value.
    private String describeImage(Ec2Client ec2, String instanceId) {
        try {
            DescribeImagesRequest imagesRequest = DescribeImagesRequest.builder()
                    .imageIds(instanceId)
                    .build();

            DescribeImagesResponse response = ec2.describeImages(imagesRequest);
            System.out.println("The description of the first image is "+response.images().get(0).description());
            System.out.println("The name of the first image is "+response.images().get(0).name());

            // Return the image Id value.
            return response.images().get(0).imageId();

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    // Get a list of instance types.
    private String getInstanceTypes(Ec2Client ec2) {
        String instanceType="";
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
            for (InstanceTypeInfo type: instanceTypes) {
                System.out.println("The memory information of this type is "+type.memoryInfo().sizeInMiB());
                System.out.println("Network information is "+type.networkInfo().toString());
                instanceType = type.instanceType().toString();
            }

            return instanceType;

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    // Get the Id value of an instance with amzn2 in the name.
    public static String getParaValues(SsmClient ssmClient) {
        try {
            GetParametersByPathRequest parameterRequest = GetParametersByPathRequest.builder()
                    .path("/aws/service/ami-amazon-linux-latest")
                    .build();

            GetParametersByPathIterable responses = ssmClient.getParametersByPathPaginator(parameterRequest);
            for (GetParametersByPathResponse response : responses) {
                System.out.println("Test "+response.nextToken());
                List<Parameter> parameterList = response.parameters();
                for (Parameter para: parameterList) {
                    System.out.println("The name of the para is: "+para.name());
                    System.out.println("The type of the para is: "+para.type());
                    if (filterName(para.name())) {
                        return para.value();
                    }
                }
            }

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "" ;
    }

    // Return true if the name has amzn2 in it. For example:
    // /aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-arm64-gp2
    private static boolean filterName(String name) {
        String[] parts = name.split("/");
        String myValue = parts[4];
        return myValue.contains("amzn2");
    }

    private String runInstance(Ec2Client ec2, String instanceType, String keyName, String groupName, String amiId ) {
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
            System.out.println("Successfully started EC2 instance "+instanceId +" based on AMI "+amiId);
            return instanceId;

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
}
