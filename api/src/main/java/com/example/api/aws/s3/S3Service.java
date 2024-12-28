package com.example.api.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-12-27 PM 2:48
 */
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;


    /**
     * S3 버킷에서 모든 파일 목록 조회
     *
     * @return 파일 목록
     * @throws SdkException AWS SDK 예외
     */
    public List<String> getFileList() throws SdkException {
        try {
            ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(listObjectsRequest);

            return response.contents().stream()
                    .map(S3Object::key)
                    .collect(Collectors.toList());
        } catch (SdkException e) {
            throw e;
        }
    }

    /**
     * 파일을 S3에 업로드
     *
     * @param file 업로드할 파일
     * @param key 키
     * @throws SdkException AWS SDK 예외
     * @throws IOException 파일 읽기 예외
     * @return File URL
     */
    public String uploadFile(MultipartFile file, String key) throws SdkException, IOException {

        try (InputStream inputStream = file.getInputStream()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(inputStream, file.getSize()));

            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
        } catch (SdkException | IOException e) {
            throw e;
        }
    }

    /**
     * 파일을 S3에서 삭제
     *
     * @param key 키
     * @throws SdkException AWS SDK 예외 처리
     */
    public void deleteFile(String key) throws SdkException {

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (SdkException e) {
            throw e;
        }
    }
}
