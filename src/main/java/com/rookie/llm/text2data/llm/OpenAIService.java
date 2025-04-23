package com.rookie.llm.text2data.llm;

import com.rookie.llm.text2data.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.ApiKey;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

/**
 * Name：OpenAIService
 * Author：eumenides
 * Created on: 2025/4/23
 * Description:
 */
public class OpenAIService implements LLMMetaService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    @Override
    public ChatModel initChatModel(AppConfig.LlmConfig llmConfig) {
        var openAiApi = OpenAiApi.builder()
                .apiKey(llmConfig.getApiKey())
                .baseUrl(llmConfig.getBaseUrl())
                .build();
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model(llmConfig.getModel())
                .temperature(0.4)
                .maxTokens(200)
                .build();
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(openAiChatOptions)
                .build();
    }
}
