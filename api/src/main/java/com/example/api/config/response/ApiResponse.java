package com.example.api.config.response;

import lombok.*;

import java.util.List;

/**
 * API 응답 공통 구조 (추상)
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-31 오후 3:18
 */
@Getter
@RequiredArgsConstructor
public abstract class ApiResponse<T> {

    /** 요청 성공 여부 */
    private final boolean success;

    /** HTTP 상태 코드 */
    private final int statusCode;

    /** 에러 메시지 리스트 */
    private List<String> errors;

    /**
     * 생성자
     *
     * @param statusCode HTTP 상태 코드
     */
    protected ApiResponse(int statusCode) {
        this.success = true;
        this.errors = null;
        this.statusCode = statusCode;
    }

    /**
     * 생성자
     *
     * @param statusCode HTTP 상태 코드
     * @param errors 에러 메시지 리스트
     */
    protected ApiResponse(int statusCode, List<String> errors) {
        this.success = false;
        this.errors = errors;
        this.statusCode = statusCode;
    }
}
