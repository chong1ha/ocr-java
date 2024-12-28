package com.example.api.upload.service;

import com.example.api.aws.s3.S3Service;
import com.example.api.upload.dto.FileUploadRequest;
import com.example.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 28.
 */
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Service s3Service;
    private final ImgFileValidator imgFileValidator;

    /**
     * 다중 파일 업로드 처리
     *
     * @param uploadData 파일 업로드 관련 데이터
     * @return 성공 메시지
     */
    public String uploadFiles(FileUploadRequest uploadData) throws IOException {

        if (uploadData == null || uploadData.files().isEmpty()) {
            return "UploadData is empty or null";
        }

        // 다중 파일 처리
        for (MultipartFile file : uploadData.files()) {

            if (file.isEmpty()) {
                return "File is empty";
            }

            String fileName = file.getOriginalFilename();
            if (StringUtil.isEmpty(fileName)) {
                return "FileName is empty or null";
            }

            // Check MagicNumber
            String fileType = extractFileType(file);
            try (InputStream inputStream = file.getInputStream()) {

                if (!imgFileValidator.validate(inputStream, fileType)) {
                    return "Invalid Image Type";
                }
            }

            // 파일 이름 생성
            String generatedFileName = FileNameGenerator.generate(
                    uploadData.userId(),
                    uploadData.category(),
                    uploadData.timestamp(),
                    fileType
            );

            // S3 업로드
            String fileUrl = s3Service.uploadFile(file, generatedFileName);

            // 업로드된 파일 URL
            System.out.println("File '" + fileName + "' has been successfully uploaded. S3 URL: " + fileUrl);
            s3Service.printAllFiles();
            System.out.println("-----------");
            System.out.println(s3Service.getFileList(
                    String.format("%s/%s/2024/12/", uploadData.userId(), uploadData.category()), 1000));
        }

        return uploadData.files().size() + " files have been successfully uploaded.";
    }

    /**
     * File 확장자 확인
     *
     * @param file
     * @return file 확장자
     */
    private String extractFileType(MultipartFile file) {

        String fileName = file.getOriginalFilename();

        if (!StringUtil.isEmpty(fileName) && fileName.contains(".")) {
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            return fileExtension.toUpperCase();
        }

        return "UNKNOWN";
    }
}
