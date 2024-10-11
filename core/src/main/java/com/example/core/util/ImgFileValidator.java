package com.example.core.util;

import java.io.IOException;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-10-11 오전 11:11
 */
public class ImgFileValidator extends FileValidator {

    @Override
    protected FileSignature getFileSignature() {
        // default : png
        return FileSignature.PNG;
    }

    /**
     * 이미지 파일 유효성 검사
     *
     * @param filePath 검증할 파일경로
     * @throws IOException 파일 입출력 예외
     * @return 매직넘버 일치하면 True
     */
    public boolean isValidImg(String filePath) throws IOException {
        return validate(filePath);
    }
}
