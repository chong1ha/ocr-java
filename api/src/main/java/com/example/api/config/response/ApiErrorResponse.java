package com.example.api.config.response;

import lombok.Getter;

import java.util.List;

/**
 * Error Response
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-31 오후 3:27
 */
@Getter
public class ApiErrorResponse<T> extends ApiResponse<Void> {

    /**
     * 생성자
     *
     * @param statusCode HTTP 상태 코드
     * @param errors 에러 메시지 리스트
     */
    public ApiErrorResponse(int statusCode, List<String> errors) {
        super(statusCode, errors);
    }
}
