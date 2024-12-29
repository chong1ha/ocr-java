package com.example.api.common;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
public class FileNameTemplate {

    private final String template;
    public FileNameTemplate(@NotNull String template) {
        this.template = template;
    }

    /**
     * 템플릿을 이용, 파일 이름 생성 <br>
     *
     * 주어진 변수 값을 템플릿에 치환
     *
     * @param variables 파일 이름에 치환할 변수명과 값이 담긴 Map
     * @return 파일이름
     */
    public String generateFileName(@NotNull Map<String, String> variables) {

        String result = template;

        // 변수명에 맞게 치환
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return result;
    }
}
