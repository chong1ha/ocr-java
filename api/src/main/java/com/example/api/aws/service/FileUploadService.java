package com.example.api.aws.service;

import com.example.api.common.FileNameGenerator;
import com.example.api.common.ImgFileValidator;
import com.example.api.db.domain.FileUpload;
import com.example.api.db.domain.User;
import com.example.api.db.repository.FileUploadRepository;
import com.example.api.db.repository.UserRepository;
import com.example.api.upload.dto.FileUploadRequest;
import com.example.core.util.StringUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

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
    private final UserRepository userRepository;
    private final FileUploadRepository fileUploadRepository;

    /**
     * 다중 파일 업로드 처리
     *
     * @param uploadData 파일 업로드 관련 데이터
     * @return 성공 메시지
     */
    @Transactional
    public String uploadFiles(FileUploadRequest uploadData) throws Exception {

        if (uploadData == null || uploadData.files().isEmpty()) {
            return "UploadData is empty or null";
        }

        // User 조회
        User user = userRepository.findById(uploadData.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                    String.format("%s/%s/year=2024/month=12/", uploadData.userId(), uploadData.category()), 1000));

            // DB save
            Long fileSize = file.getSize();

            FileUpload fileUpload = new FileUpload();
            fileUpload.setUser(user);
            fileUpload.setFileName(fileName);
            fileUpload.setS3Path(fileUrl);
            fileUpload.setFileSize(fileSize);
            fileUpload.setFileType(fileType);

            fileUploadRepository.save(fileUpload);
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
