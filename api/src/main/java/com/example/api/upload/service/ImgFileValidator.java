package com.example.api.upload.service;

import com.example.core.util.FileSignature;
import com.example.core.util.FileValidator;
import org.springframework.stereotype.Component;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-10-11 오전 11:11
 */
@Component
public class ImgFileValidator extends FileValidator {

    @Override
    protected FileSignature getFileSignature(String fileType) {

        switch (fileType.toUpperCase()) {
            case "PNG":
                return FileSignature.PNG;
            case "JPEG":
                return FileSignature.JPEG;
            case "GIF":
                return FileSignature.GIF;
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}
