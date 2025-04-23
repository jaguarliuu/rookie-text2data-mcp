package com.rookie.llm.text2data;

import com.rookie.llm.text2data.config.AppConfig;
import com.rookie.llm.text2data.service.Text2DataService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class RookieText2dataMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(RookieText2dataMcpApplication.class, args);
    }
    @Bean
    public List<ToolCallback> test2DataTools(Text2DataService text2DataService) {
        return List.of(ToolCallbacks.from(text2DataService));
    }

}
