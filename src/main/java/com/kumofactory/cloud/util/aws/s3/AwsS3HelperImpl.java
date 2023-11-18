package com.kumofactory.cloud.util.aws.s3;

import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.global.config.S3Config;
import com.kumofactory.cloud.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AwsS3HelperImpl implements AwsS3Helper {

    private final S3Config s3Config;
    private final Logger logger = LoggerFactory.getLogger(AwsS3HelperImpl.class);

    public void putS3Object(MultipartFile svgFile, String keyName) throws S3Exception, IOException {

        S3Client s3Client = S3Client.builder()
                .region(Region.of(s3Config.getRegion()))
                .credentialsProvider(s3Config.getCredentialsProvider())
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
                .credentialsProvider(s3Config.getCredentialsProvider())
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

    // thumbnail 저장
    public String saveThumbnail(AwsBluePrintDto bluePrint, Member member) {
        String objectKey = _getObjectKey(member.getOauthId(), bluePrint.getUuid());
        logger.info("thumbnail upload start: {}", objectKey);
        try {
            byte[] svgContent = Base64.getDecoder().decode(bluePrint.getSvgFile().split(",")[1]);
            MultipartFile svgFile = new MockMultipartFile("file", objectKey, "image/svg+xml", svgContent);
            putS3Object(svgFile, objectKey);
            logger.info("thumbnail upload success: {}", objectKey);
            logger.info("thumbnail url: {}", getPresignedUrl(objectKey));
        } catch (Exception e) {
            logger.error("thumbnail upload failed: {}", e.getMessage());
        }

        return objectKey;
    }

    private String _getObjectKey(String memberId, String blueprintId) {
        return memberId + "/" + blueprintId + ".svg";
    }
}
