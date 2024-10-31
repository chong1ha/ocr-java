package com.example.api.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Swagger UI 및 OpenAPI 문서화 관련 설정
 *
 * @author gunha
 * @version 1.0
 * @since 2024-10-31 오후 2:12
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 객체 생성 <br>
     *
     * API 문서의 메타 정보와 컴포넌트 설정
     *
     * @return API 정보와 컴포넌트가 설정된 OpenAPI 객체
     */
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSchemas("ErrorResponse", createErrorResponseSchema()));
    }

    /**
     * API 기본 정보 설정 <br>
     *
     * API 제목, 설명, 버전, 연락처 정보 및 라이선스 정보 등을 포함
     *
     * @return API 메타 정보가 설정된 Info 객체
     */
    private Info apiInfo() {

        return new Info()
                .title("예제. Springdoc 기반 API 문서")
                .description("이 API 문서는 Springdoc을 사용하여 자동 생성된 Swagger UI와 OpenAPI 3.0 명세를 제공")
                .version("1.0")
                // sample
                .contact(new Contact()
                        .name("API Support Team")
                        .email("support@example.com")
                        .url("https://example.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));
    }

    /**
     * ErrorResponse 스키마
     *
     * @return Schema 객체
     */
    private Schema<ErrorResponse> createErrorResponseSchema() {
        Schema<ErrorResponse> schema = new Schema<>();
        schema.setType("object");
        schema.addProperty("status", new Schema().type("integer").example(404));
        schema.addProperty("message", new Schema().type("string").example("Not Found"));
        return schema;
    }
}
