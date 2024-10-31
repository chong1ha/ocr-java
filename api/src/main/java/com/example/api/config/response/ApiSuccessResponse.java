package com.example.api.config.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-10-31 오후 3:24
 */
@Getter
@Setter
public class ApiSuccessResponse<T> extends ApiResponse {

    /** 성공 */
    @NonNull
    private T data;

    public ApiSuccessResponse(T data) {
        super(null);
        this.data = data;
    }
}
