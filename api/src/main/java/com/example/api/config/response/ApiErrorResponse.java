package com.example.api.config.response;

import java.util.List;

/**
 * @author gunha
 * @version 1.0
 * @since 2024-10-31 오후 3:27
 */
public class ApiErrorResponse<T> extends ApiResponse {

    public ApiErrorResponse(List<String> errors) {
        super(errors);
    }
}
