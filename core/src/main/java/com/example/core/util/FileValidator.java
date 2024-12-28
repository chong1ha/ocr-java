package com.example.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 파일 검증 (Baseline)
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-11 오전 11:00
 */
public abstract class FileValidator {

    /**
     * 파일 유효성 검사
     *
     * @param inputStream 검증할 파일의 InputStream
     * @param fileType 파일 유형
     * @throws IOException 파일 입출력 예외
     * @return 매직넘버 일치하면 True
     */
    public boolean validate(InputStream inputStream, String fileType) throws IOException {

        FileSignature signature = getFileSignature(fileType);

        byte[] magicBytes = new byte[signature.getMagicNumber().length];
        if (inputStream.read(magicBytes) != magicBytes.length) {
            return false;
        }

        return Arrays.equals(magicBytes, signature.getMagicNumber());
    }

    /**
     * FileType에 맞는 FileSignature 반환
     *
     * @param fileType 파일 타입 (ex. "PNG", "JPEG")
     * @return FileSignature
     */
    protected abstract FileSignature getFileSignature(String fileType);
}
