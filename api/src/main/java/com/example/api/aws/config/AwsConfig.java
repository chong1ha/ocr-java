package com.example.api.aws.config;

import com.example.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.Duration;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-12-27 PM 1:00
 */
@Configuration
public class AwsConfig {

    @Value("${aws.access.key:#{null}}")
    private String accessKey;

    @Value("${aws.secret.key:#{null}}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;


    /**
     * s3 client configuration
     */
    @Bean
    public S3Client amazonS3() {

        return S3Client.builder()
                .credentialsProvider(getCredentialsProvider())
                .region(Region.of(region))
                .overrideConfiguration(clientOverrideConfiguration())
                .build();
    }

    /**
     * 자격 증명
     */
    private StaticCredentialsProvider getStaticCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }

    /**
     * 키가 없으면, IAM Role 기반 인증 수행
     */
    private AwsCredentialsProvider getCredentialsProvider() {

        if (StringUtil.isEmpty(accessKey) || StringUtil.isEmpty(secretKey)) {
            return DefaultCredentialsProvider.create();
        }

        return getStaticCredentialsProvider();
    }

    /**
     * 클라이언트 설정 재정의
     */
    private ClientOverrideConfiguration clientOverrideConfiguration() {
        return ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofMinutes(1))  // 전체 API 호출 시간 (1분)
                .apiCallAttemptTimeout(Duration.ofSeconds(10))  // 개별 재시도 호출 시간 (10초)
                .retryPolicy(RetryPolicy.defaultRetryPolicy())  // 재시도 정책
                .build();
    }
}
