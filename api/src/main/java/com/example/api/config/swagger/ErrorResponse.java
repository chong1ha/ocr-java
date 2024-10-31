package com.example.api.config.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * API Response 에서 발생하는 오류 응답 정보
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-31 오후 2:18
 */
@Schema(description = "오류 응답")
@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {

    /** 상태 코드 */
    @Schema(description = "HTTP 상태 코드", example = "404")
    private int status;

    /** 오류 메시지 */
    @Schema(description = "오류 메시지", example = "Not Found")
    private String message;
}
