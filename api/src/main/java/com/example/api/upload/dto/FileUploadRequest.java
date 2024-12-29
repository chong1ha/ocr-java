package com.example.api.upload.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
public record FileUploadRequest(
        Long userId,
        String category,
        long timestamp,
        List<MultipartFile> files
) {}
