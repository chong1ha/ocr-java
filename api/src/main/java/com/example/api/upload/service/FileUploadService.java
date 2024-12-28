package com.example.api.upload.service;

import com.example.api.aws.s3.S3Service;
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
     * @param files 업로드된 파일들
     * @return 성공 메시지
     */
    public String uploadFiles(List<MultipartFile> files) throws IOException {

        if (files == null || files.isEmpty()) {
            return "File is empty or null";
        }

        // 다중 파일 처리
        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                return "File is empty";
            }

            String fileName = file.getOriginalFilename();
            if (StringUtil.isEmpty(fileName)) {
                return "FileName is empty or null";
            }

            // Check MagicNumber
            try (InputStream inputStream = file.getInputStream()) {

                String fileType = extractFileType(file);
                if (!imgFileValidator.validate(inputStream, fileType)) {
                    return "Invalid Image Type";
                }
            }

            // S3 업로드
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String fileUrl = s3Service.uploadFile(file, uniqueFileName);

            // 업로드된 파일 URL
            System.out.println("File '" + fileName + "' has been successfully uploaded. S3 URL: " + fileUrl);
            System.out.println(s3Service.getFileList());
        }

        return files.size() + " files have been successfully uploaded.";
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
