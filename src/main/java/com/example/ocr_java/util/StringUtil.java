package com.example.ocr_java.util;

import java.util.Optional;

/**
 * @author gunha
 * @version 0.1
 * @since 2024-07-03 오후 4:26
 */
public class StringUtil {

    /**
     * 입력된 문자열이 null 이거나 비어 있는지 확인
     *
     * @param str 입력된 문자열
     * @return null 이거나 비어 있으면 true
     */
    public static boolean isEmpty(String str) {

        return Optional.ofNullable(str)
                .map(String::isEmpty)
                .orElse(true);
    }
}
