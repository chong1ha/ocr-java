package com.example.api.upload.service;

import com.example.core.util.DateUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 파일 이름 생성
 *
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
public class FileNameGenerator {

    private static final String TEMPLATE = "{userId}/{category}/year={year}/month={month}/day={day}/receipt_{date}_{randomSuffix}.{extension}";

    /**
     * 파일 이름 생성 <br>
     *
     * (TimeStamp + Random) 조합
     *
     * @param userId 사용자 ID
     * @param category 카테고리
     * @param timestamp 업로드 시간
     * @param extension 파일 확장자
     * @return 고유한 파일이름
     */
    public static String generate(String userId, String category, long timestamp, String extension) {

        String date = DateUtil.convertLongToDate(timestamp);
        String randomSuffix = generateRandomSuffix();

        // 파일이름 템플릿 생성
        FileNameTemplate template = new FileNameTemplate(TEMPLATE);

        Map<String, String> variables = new HashMap<>();
        variables.put("userId", userId);
        variables.put("category", category);
        variables.put("year", date.substring(0, 4));      // "yyyy"
        variables.put("month", date.substring(4, 6));     // "MM"
        variables.put("day", date.substring(6, 8));       // "dd"
        variables.put("date", date);                      // "yyyyMMdd"
        variables.put("randomSuffix", randomSuffix);      // 랜덤값 (8자리)
        variables.put("extension", extension);            // 파일 확장자

        // ex. /receipts/USER01/식비/2024/12/29/receipt_20241229_ab12cd34.jpg
        return template.generateFileName(variables);
    }

    /**
     * 랜덤값 생성 <br>
     *
     * UUID 에서 랜덤 8자리 추출
     *
     * @return 랜덤값
     */
    private static String generateRandomSuffix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
