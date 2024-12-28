package com.example.core.util;

import lombok.Setter;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-12-27 PM 1:28
 */
public class Decryptor {

    /** Secret Key */
    @Setter
    private static String key;
    /** Initialize Vector */
    @Setter
    private static String iv;
    /** Cipher Mode */
    @Setter
    private static String cipherMode = "AES";
    /** Padding Mode */
    @Setter
    private static String paddingMode = "CBC";

    public static String decrypt(String msg) throws Exception {
        return EncryptionUtil.decrypt(msg, key, iv, cipherMode, paddingMode);
    }
}