package com.rookie.llm.text2data.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Name：AppConfig
 * Author：eumenides
 * Created on: 2025/4/23
 * Description:
 */
@Configuration
@ConfigurationProperties
@Validated
@Data
public class AppConfig {

    @NotNull(message = "LLM configuration is required")
    private LlmConfig llm;

    @NotNull(message = "Database configuration is required")
    private DatabaseConfig database;


    // 内部类用于llm配置
    @Data
    @Validated
    public static class LlmConfig {

        @NotBlank(message = "Provider is required")
        private String provider;

        @NotBlank(message = "LLM mode is required")
        private String model;

        @NotBlank(message = "API key is required")
        private String apiKey;

        private String baseUrl; // 可选
    }

    @Data
    @Validated
    // 内部类用于database配置
    public static class DatabaseConfig {
        @NotBlank(message = "Database type is required")
        private String dbType;

        @NotBlank(message = "Database host is required")
        private String host;

        @NotNull(message = "Database port is required")
        private Integer port;

        @NotBlank(message = "Database username is required")
        private String username;

        @NotBlank(message = "Database password is required")
        private String password;

        @NotBlank(message = "Database name is required")
        private String dbName;
    }



}
