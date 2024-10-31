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
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class ApiResponse {

    /** 에러 메시지 리스트 */
    private List<String> errors;
}
