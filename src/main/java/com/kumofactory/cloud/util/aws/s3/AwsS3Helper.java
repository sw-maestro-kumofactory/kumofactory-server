package com.kumofactory.cloud.util.aws.s3;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

public interface AwsS3Helper {

    void putS3Object(MultipartFile svgFile, String keyName) throws S3Exception, IOException;

    String getPresignedUrl(String keyName) throws S3Exception;
}
