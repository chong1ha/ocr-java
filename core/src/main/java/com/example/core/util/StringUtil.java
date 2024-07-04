package com.example.core.util;

import java.util.Optional;

/**
 * 문자열 관련 공통 유틸리티
 *
 * @author gunha
 * @version 1.0
 * @since 2024-07-04 오전 9:35
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
