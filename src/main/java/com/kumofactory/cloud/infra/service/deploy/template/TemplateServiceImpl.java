package com.kumofactory.cloud.infra.service.deploy.template;

import com.kumofactory.cloud.infra.constants.AwsAuth;
import com.kumofactory.cloud.infra.service.network.Ec2Service;
import java.time.Clock;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateRouteRequest;
import software.amazon.awssdk.services.ec2.model.CreateRouteResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateServiceImpl implements TemplateService {

    private final Ec2Service ec2Service;
    private final AwsAuth awsAuth;
    private Logger logger = LoggerFactory.logger(TemplateServiceImpl.class);

    @Override
    public void createThreeTierArchitecture() {
        String vpcId = "vpc-0719a1184fa5ccdcd";
        Ec2Client client = ec2Service.getEc2Client();

        // 퍼블릭 서브넷 생성
        String publicSubnetId2a = ec2Service.createSubnet(client, vpcId, "10.0.1.0/24",
                                                          Clock.systemUTC().toString(),
                                                          "ap-northeast-2a");
        String publicSubnetId2c = ec2Service.createSubnet(client, vpcId, "10.0.2.0/24",
                                                          Clock.systemUTC().toString(),
                                                          "ap-northeast-2c");
        // 프라이빗 서브넷 생성
        String privateSubnetId2a = ec2Service.createSubnet(client, vpcId, "10.0.3.0/24",
                                                           Clock.systemUTC().toString(),
                                                           "ap-northeast-2a");
        String privateSubnetId2c = ec2Service.createSubnet(client, vpcId, "10.0.4.0/24",
                                                           Clock.systemUTC().toString(),
                                                           "ap-northeast-2c");

        // 라우팅 테이블 생성
        String routeTableId = ec2Service.createRouteTable(client, vpcId);
        connectGateWayToRouteTable(client, routeTableId, "igw-055b16ae312027a19", "0.0.0.0/0");
        // 서브넷과 라우팅 테이블 연결
        ec2Service.connectSubnetToRouteTable(client, routeTableId, publicSubnetId2a);
        ec2Service.connectSubnetToRouteTable(client, routeTableId, publicSubnetId2c);

        // 보안 그룹 생성
        //        String securityGroupId = ec2Service.createSecurityGroup(client, vpcId, "web-sg",
        //                                                                "web security group");
        String securityGroupId = "sg-06b283402ac4786ae";

        // 인스턴스 실행
        String amiId = "ami-0425f132103cb3ed8"; // linux ami id
        ec2Service.createEc2Instance(client, "test1", amiId, publicSubnetId2a, securityGroupId);
        ec2Service.createEc2Instance(client, "test2", amiId, publicSubnetId2c, securityGroupId);

        client.close();
    }

    private void connectGateWayToRouteTable(Ec2Client client, String routeTableId,
                                            String gatewayId, String ipRange) {
        CreateRouteRequest request = CreateRouteRequest.builder().routeTableId(routeTableId)
                                                       .gatewayId(gatewayId)
                                                       .destinationCidrBlock(ipRange).build();

        CreateRouteResponse response = client.createRoute(request);
    }


}
