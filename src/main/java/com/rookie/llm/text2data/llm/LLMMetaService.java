package com.rookie.llm.text2data.llm;

import com.rookie.llm.text2data.config.AppConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;

/**
 * Name：LLMMetaService
 * Author：eumenides
 * Created on: 2025/4/23
 * Description:
 */
public interface LLMMetaService {


    ChatModel initChatModel(AppConfig.LlmConfig llmConfig);


}
