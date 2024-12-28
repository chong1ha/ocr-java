package com.example.api.config.response;

import lombok.Getter;
import lombok.NonNull;

/**
 * Success Response
 * @param <T> 성공 데이터 타입
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-31 오후 3:24
 */
@Getter
public class ApiSuccessResponse<T> extends  ApiResponse<T> {

    /** 성공 */
    @NonNull
    private final T data;

    public ApiSuccessResponse(int statusCode, T data) {
        super(statusCode);
        this.data = data;
    }
}
