package com.example.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
public class DateUtil {

    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_FORMAT));

    /**
     * timestamp를 yyyyMMdd 형식의 날짜로 변환
     *
     * @param timestamp 변환할 timestamp (ms)
     * @throws IllegalArgumentException 타임스탬프가 유효하지 않거나 날짜 변환에 실패할 경우
     * @return yyyyMMdd 날짜 형식 문자열
     */
    public static String convertLongToDate(long timestamp) {

        try {
            return DATE_FORMATTER.get().format(new Date(timestamp));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid timestamp: " + timestamp, e);
        }
    }
}
