package com.kumofactory.cloud.infra.service.deploy.credential;

import com.kumofactory.cloud.infra.constants.AwsAuth;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Service
public class AwsCredentialServiceImpl implements AwsCredentialService {

  @Override
  public StaticCredentialsProvider getRootCredential() {
    // AWS SDK 인증 정보 생성
    System.out.printf("accessKeyeeeeee: %s, secretKey: %s\n", AwsAuth.accessKey, AwsAuth.secretKey);
    AwsBasicCredentials credentials = AwsBasicCredentials.create(AwsAuth.accessKey,
                                                                 AwsAuth.accessKey);
    return StaticCredentialsProvider.create(credentials);
  }

  @Override
  public StaticCredentialsProvider getUserCredential(String accessKey, String secretKey) {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    return StaticCredentialsProvider.create(credentials);
  }
}
