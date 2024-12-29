package com.example.api.config;

import com.example.core.util.Decryptor;
import com.example.core.util.Encryptor;
import com.example.core.util.StringUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
@Configuration
public class AppConfig {

    /** 애플리케이션 SecretKey */
    @Value("${app.secret-key}")
    private String appKey;

    /** 애플리케이션 IV */
    @Value("${app.iv}")
    private String appIv;


    /**
     * 암,복호화 키 세팅
     */
    @PostConstruct
    public void init() throws Exception {

        if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(appIv)) {
            throw new IllegalArgumentException("'app.secret-key' and 'app.iv' must be provided");
        }

        try {
            // validate
            validEnv();

            // setting (base64로 인코딩된 상태)
            Encryptor.setKey(appKey);
            Encryptor.setIv(appIv);
            Decryptor.setKey(appKey);
            Decryptor.setIv(appIv);
        } catch (IllegalArgumentException e) {
           throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 환경 변수 유효성 검사
     *
     * @throws IllegalArgumentException
     */
    private void validEnv() throws IllegalArgumentException {

        if (!StringUtils.hasText(appKey)) {
            throw new IllegalArgumentException("'app.secret-key' cannot be empty");
        }
        if (!StringUtils.hasText(appIv)) {
            throw new IllegalArgumentException("'app.iv' cannot be empty");
        }
        if (appKey.length() != 24) {
            throw new IllegalArgumentException("'app.secret-key' should be 24 bytes");
        }
        if (appIv.length() != 24) {
            throw new IllegalArgumentException("'app.iv' should be 24 bytes");
        }
    }
}
