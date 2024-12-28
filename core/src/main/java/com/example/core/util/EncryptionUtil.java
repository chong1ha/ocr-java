package com.example.core.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 암복호화
 *
 * @author gunha
 * @version 1.0
 * @since 2024-12-27 PM 1:22
 */
public class EncryptionUtil {

    /**
     * 암호화
     *
     * @param msg 암호화할 원본 메시지
     * @param secretKey 비밀키
     * @param iv 초기화 벡터
     * @param cipherMode 암호화 모드
     * @param paddingMode 패딩 모드
     * @throws Exception 암호화 시, 발생할 수 있는 예외
     * @return 암호화된 문자열
     */
    public static String encrypt(String msg, String secretKey, String iv, String cipherMode, String paddingMode) throws Exception {

        Cipher cipher = Cipher.getInstance(cipherMode + "/" + paddingMode + "/PKCS5Padding");

        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] encryptedBytes = cipher.doFinal(msg.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 복호화
     *
     * @param msg 암호화된 메시지
     * @param secretKey 비밀키
     * @param iv 초기화 벡터
     * @param cipherMode 암호화 모드
     * @param paddingMode 패딩 모드
     * @throws Exception 복호화 시, 발생할 수 있는 예외
     * @return 복호화된 문자열
     */
    public static String decrypt(String msg, String secretKey, String iv, String cipherMode, String paddingMode) throws Exception {

        Cipher cipher = Cipher.getInstance(cipherMode + "/" + paddingMode + "/PKCS5Padding");

        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(msg);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes);
    }

    /**
     * 비밀키 생성 (AES 암호화용)
     *
     * @return 생성된 비밀키
     */
    public static String generateSecretKey() throws Exception {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);  // 128 비트 키 길이
        SecretKey secretKey = keyGenerator.generateKey();

        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 랜덤한 초기화 벡터(IV) 생성
     *
     * @return 생성된 IV
     */
    public static String generateIV() {

        // 16바이트 크기의 랜덤 IV 생성
        byte[] iv = new byte[16];
        for (int i = 0; i < iv.length; i++) {
            iv[i] = (byte) (Math.random() * 256);
        }

        return Base64.getEncoder().encodeToString(iv);
    }
}
