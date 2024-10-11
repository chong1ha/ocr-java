package com.example.core.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 파일 검증 (Baseline)
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-11 오전 11:00
 */
public abstract class FileValidator {

    protected abstract FileSignature getFileSignature();

    /**
     * 파일 유효성 검사
     *
     * @param filePath 검증할 파일경로
     * @throws IOException 파일 입출력 예외
     * @return 매직넘버 일치하면 True
     */
    public boolean validate(String filePath) throws IOException {

        FileSignature signature = getFileSignature();

        try (FileInputStream stream = new FileInputStream(filePath)) {

            byte[] magicBytes = new byte[signature.getMagicNumber().length];
            if (stream.read(magicBytes) != magicBytes.length) {
                return false;
            }

            return Arrays.equals(magicBytes, signature.getMagicNumber());
        }
    }
}
