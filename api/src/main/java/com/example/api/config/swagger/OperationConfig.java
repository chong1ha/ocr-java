package com.example.api.config.swagger;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API 응답 공통 설정
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-31 오후 2:37
 */
@Configuration
public class OperationConfig {

    /**
     * API의 각 엔드포인트의 공통 응답 형식 <br>
     *
     * ex. 404(Not Found) 및 500(Internal Server Error) 등 응답
     *
     * @return 공통 응답이 포함된 OperationCustomizer 객체
     */
    @Bean
    public OperationCustomizer operationCommon() {

        return (operation, handlerMethod) -> {

            ApiResponses apiResponses = operation.getResponses();

            if (apiResponses == null) {
                apiResponses = new ApiResponses();
                operation.setResponses(apiResponses);
            }
            apiResponses.putAll(getCommonResponses());

            return operation;
        };
    }

    /**
     * 공통 응답 메시지
     *
     * @return Map 객체
     */
    private Map<String, ApiResponse> getCommonResponses() {
        LinkedHashMap<String, ApiResponse> responses = new LinkedHashMap<>();
        responses.put("404", notFoundResponse());
        responses.put("500", internalServerErrorResponse());

        return responses;
    }

    /**
     * 404, Not Found
     *
     * @return 해당 에러 관련 ApiResponse 객체
     */
    private ApiResponse notFoundResponse() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription("""
            Not Found
            - 요청한 URI가 올바른지
            """);
        addContent(apiResponse, 404, "Not Found");

        return apiResponse;
    }

    /**
     * 500, Internal Server Error
     *
     * @return 해당 에러 관련 ApiResponse 객체
     */
    private ApiResponse internalServerErrorResponse() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription("""
            Internal Server Error (Unchecked Exception)
            - API 관리자에게 요청
            """);
        addContent(apiResponse, 500, "Internal Server Error");

        return apiResponse;
    }

    /**
     * ApiResponse에 JSON 예제 및 스키마 추가
     *
     * @param apiResponse ApiResponse 객체
     * @param status 응답 상태 코드
     * @param message 예제(더미) 메시지
     */
    @SuppressWarnings("rawtypes")
    private void addContent(ApiResponse apiResponse, int status, String message) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        // 응답 데이터 구조
        Schema schema = new Schema<>();
        schema.$ref("#/components/schemas/ErrorResponse");
        mediaType.schema(schema).example(new ErrorResponse(status, message));
        // 응답 데이터 구조 정의
        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);
    }
}
