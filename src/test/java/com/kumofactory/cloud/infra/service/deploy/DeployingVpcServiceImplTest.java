package com.kumofactory.cloud.infra.service.deploy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumofactory.cloud.CloudApplication;
import com.kumofactory.cloud.infra.dto.CreateVpcDto;
import com.kumofactory.cloud.infra.service.deploy.credential.AwsCredentialService;
import com.kumofactory.cloud.infra.service.deploy.credential.AwsCredentialServiceImpl;
import com.kumofactory.cloud.infra.service.deploy.network.DeployVpcService;
import com.kumofactory.cloud.infra.service.deploy.network.DeployVpcServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {CloudApplication.class, AwsCredentialServiceImpl.class,
    DeployVpcServiceImpl.class})
@TestPropertySource(locations = "classpath:application-test.yml")
class DeployingVpcServiceImplTest {

  AwsCredentialService awsCredentialService = new AwsCredentialServiceImpl();

  @Test
  @DisplayName("VPC 생성 테스트 - Success")
  void buildVpc() {
    DeployVpcService vpcService = new DeployVpcServiceImpl(awsCredentialService);
    try {
      JSONObject response = new JSONObject();
      response.put("region", "ap-northeast-2");
      response.put("vpcName", "testVpcFromSpringBoot");
      ObjectMapper mapper = new ObjectMapper();
      mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
      CreateVpcDto createVpcDto = mapper.readValue(mapper.writeValueAsString(response),
                                                   CreateVpcDto.class);
      System.out.println(createVpcDto.toString());
      System.out.printf(response.toString());
      vpcService.createVpc(createVpcDto);
    } catch (JSONException exception) {
      exception.printStackTrace();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}