package com.kumofactory.cloud.infra.service.deploy.credential;

import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

public interface AwsCredentialService {

  StaticCredentialsProvider getRootCredential(); // root 계정 정보로 credential 생성

  StaticCredentialsProvider getUserCredential(String accessKey,
                                              String secretKey); // user 계정 정보로 credential 생성
}
