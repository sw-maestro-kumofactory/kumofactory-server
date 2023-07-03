package com.kumofactory.cloud.infra.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AwsAuth {

  @Value("${aws.accessKey}")
  public static String accessKey;
  @Value("${aws.secretKey}")
  public static String secretKey;

  public enum Type {
    ROOT, USEr
  }
}
