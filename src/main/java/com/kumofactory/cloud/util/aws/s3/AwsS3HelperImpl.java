package com.kumofactory.cloud.util.aws.s3;

import com.kumofactory.cloud.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AwsS3HelperImpl implements AwsS3Helper {

    private final S3Config s3Config;
    private final ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
    private final Logger logger = LoggerFactory.getLogger(AwsS3HelperImpl.class);

    public void putS3Object(MultipartFile svgFile, String keyName) throws S3Exception, IOException {

        S3Client s3Client = S3Client.builder()
                .region(Region.of(s3Config.getRegion()))
                .credentialsProvider(credentialsProvider)
                .build();

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(s3Config.getBucket())
                            .key(keyName)
                            .build(),
                    RequestBody.fromInputStream(svgFile.getInputStream(), svgFile.getSize()));

            logger.info("File uploaded to S3 bucket successfully");
        } catch (S3Exception e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            s3Client.close();
        }
    }

    public String getPresignedUrl(String keyName) {

        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(s3Config.getRegion()))
                .credentialsProvider(credentialsProvider)
                .build();

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Config.getBucket())
                    .key(keyName)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
            String presignedUrl = presignedGetObjectRequest.url().toString();
            logger.info("Presigned URL: {}", presignedUrl);
            return presignedUrl;

        } catch (S3Exception e) {
            logger.error(e.awsErrorDetails().errorMessage());
            throw e;
        } finally {
            presigner.close();
        }
    }
}
