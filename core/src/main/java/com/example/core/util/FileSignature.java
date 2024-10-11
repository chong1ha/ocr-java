package com.example.core.util;

/**
 * 파일별, 매직넘버
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-11 오전 11:03
 */
public enum FileSignature {

    PNG(new byte[] { (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47 }),
    JPEG(new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF }),
    GIF(new byte[] { (byte) 0x47, (byte) 0x49, (byte) 0x46 });

    private final byte[] magicNumber;

    FileSignature(byte[] magicNumber) {
        this.magicNumber = magicNumber;
    }

    /**
     * 매직 넘버 반환
     */
    public byte[] getMagicNumber() {
        return magicNumber;
    }
}
