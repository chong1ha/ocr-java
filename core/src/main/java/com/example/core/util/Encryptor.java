package com.example.core.util;

import lombok.Setter;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-12-27 PM 1:28
 */
public class Encryptor {

    /** Secret Key */
    @Setter
    private static String key;
    /** Initialize Vector */
    @Setter
    private static String iv;
    /** Cipher Mode */
    @Setter
    private static String cipherMode = "AES/CBC";
    /** Padding Mode */
    @Setter
    private static String paddingMode = "PKCS5Padding";

    public static String encrypt(String msg) throws Exception {
        return EncryptionUtil.encrypt(msg, key, iv, cipherMode, paddingMode);
    }
}
