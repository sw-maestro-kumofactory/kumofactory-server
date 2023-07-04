package com.kumofactory.cloud.infra.service.deploy.credential;

import com.kumofactory.cloud.infra.constants.AwsAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Service
@RequiredArgsConstructor
public class AwsCredentialServiceImpl implements AwsCredentialService {

  private final AwsAuth awsAuth;

  @Override
  public StaticCredentialsProvider getRootCredential() {
    // AWS SDK 인증 정보 생성
    AwsBasicCredentials credentials = AwsBasicCredentials.create(awsAuth.getAccessKey(),
                                                                 awsAuth.getAccessKey());
    return StaticCredentialsProvider.create(credentials);
  }

  @Override
  public StaticCredentialsProvider getUserCredential(String accessKey, String secretKey) {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    return StaticCredentialsProvider.create(credentials);
  }
}
