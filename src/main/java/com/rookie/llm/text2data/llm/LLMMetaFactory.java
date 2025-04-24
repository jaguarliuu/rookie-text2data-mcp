package com.rookie.llm.text2data.llm;

import com.rookie.llm.text2data.config.AppConfig;
import com.rookie.llm.text2data.database.MySQLMetaService;

/**
 * Name：LLMMetaFactory
 * Author：eumenides
 * Created on: 2025/4/23
 * Description:
 */
public class LLMMetaFactory {

    public static LLMMetaService getMetaService(String provider) {
        return switch (provider.toUpperCase()) {
            case "OPENAI" -> new OpenAIService();
            case "DEEPSEEK" -> new DeepSeekService();
            // 未来支持 PostgreSQL、SQLite 等可在此扩展
            // case "POSTGRES" -> new PostgreSQLMetaService();
            default -> throw new UnsupportedOperationException("暂不支持的LLM类型: " + provider);
        };
    }



}
