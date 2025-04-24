package com.rookie.llm.text2data.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rookie.llm.text2data.config.AppConfig;
import com.rookie.llm.text2data.database.DatabaseMetaFactory;
import com.rookie.llm.text2data.database.DatabaseMetaService;
import com.rookie.llm.text2data.llm.LLMMetaFactory;
import com.rookie.llm.text2data.llm.LLMMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Name：Text2DataService
 * Author：eumenides
 * Created on: 2025/4/23
 * Description:
 */
@Service
public class Text2DataService {

    private static Logger logger = LoggerFactory.getLogger(Text2DataService.class);

    private final AppConfig appConfig;

    @Value("classpath:/prompt/mysql_generate.st")
    private Resource mysqlPrompt;

    public Text2DataService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    private DatabaseMetaService getMetaService() {
        return DatabaseMetaFactory.getMetaService(appConfig.getDatabase().getDbType());
    }

    private LLMMetaService getLLMMetaService() {
        return LLMMetaFactory.getMetaService(appConfig.getLlm().getProvider());
    }

    public Connection connectToDatabase() throws Exception {
        return getMetaService().connect(appConfig.getDatabase());
    }
    @Tool(name = "get_tables_comments", description = "获取数据库所有的表名与注释")
    public List<Map<String, String>> getAllTableNamesAndComments() {
        return getMetaService().getAllTableNamesAndComments(appConfig.getDatabase());
    }
    @Tool(name = "get_tables_ddl", description = "获取库中所有表的 ddl,了解库表结构")
    public Map<String, String> getAllTablesDDL() {
        return getMetaService().getAllTablesDDL(appConfig.getDatabase());
    }
    @Tool(name = "get_table_by_name", description = "根据表名获取该表的 ddl")
    public String getSingleTableDDL(String tableName) {
        return getMetaService().getSingleTableDDL(appConfig.getDatabase(), tableName);
    }
    @Tool(name = "excute_sql", description = "执行 SQL 获取数据")
    public List<Map<String, Object>> executeQuery(String sql) {
        return getMetaService().executeQuery(appConfig.getDatabase(), sql);
    }
    @Tool(name = "get_sql_by_ddl_query",description = "根据相关表的 ddl 以及用户的问题生成 SQL 语句")
    public String getSqlByDDLAndQuery(String ddl, String query) {
        ChatModel chatModel = getLLMMetaService().initChatModel(appConfig.getLlm());
        PromptTemplate promptTemplate = new PromptTemplate(mysqlPrompt);
        Map<String, Object> map = new HashMap<>();
        map.put("meta_data", ddl);
        map.put("query", query);
        Prompt prompt = promptTemplate.create(map);
        ChatResponse call = chatModel.call(prompt);
        return call.getResult().getOutput().getText();
    }

    /**
     * 将SQL查询结果转换为ECharts所需的options JSON
     * @param resultList SQL查询结果列表
     * @param chartType 图表类型，如'bar', 'line', 'pie'等
     * @param categoryKey 作为类别轴的键名
     * @param seriesKeys 作为数据系列的键名列表
     * @return ECharts的options JSON字符串
     */
    @Tool(name = "convert_data_to_chart", description = "将数据库查询结果转化为 echarts 图表")
    public String convertToEChartsOptions(List<Map<String, Object>> resultList,
                                          String chartType,
                                          String categoryKey,
                                          List<String> seriesKeys) {
        if (resultList == null || resultList.isEmpty()) {
            return "{}";
        }

        // 提取类别数据
        List<String> categories = new ArrayList<>();
        for (Map<String, Object> row : resultList) {
            categories.add(String.valueOf(row.get(categoryKey)));
        }

        // 准备系列数据
        List<Map<String, Object>> series = new ArrayList<>();
        for (String seriesKey : seriesKeys) {
            Map<String, Object> seriesMap = new HashMap<>();
            seriesMap.put("name", seriesKey);
            seriesMap.put("type", chartType);

            List<Object> data = new ArrayList<>();
            for (Map<String, Object> row : resultList) {
                data.add(row.get(seriesKey));
            }
            seriesMap.put("data", data);
            series.add(seriesMap);
        }

        // 构建完整的options对象
        Map<String, Object> options = new HashMap<>();

        // 标题配置
        Map<String, Object> title = new HashMap<>();
        title.put("text", "SQL查询结果图表");
        options.put("title", title);

        // 提示框配置
        Map<String, Object> tooltip = new HashMap<>();
        tooltip.put("trigger", "axis");
        options.put("tooltip", tooltip);

        // 图例配置
        Map<String, Object> legend = new HashMap<>();
        legend.put("data", seriesKeys);
        options.put("legend", legend);

        // x轴配置
        Map<String, Object> xAxis = new HashMap<>();
        xAxis.put("type", "category");
        xAxis.put("data", categories);
        options.put("xAxis", xAxis);

        // y轴配置
        Map<String, Object> yAxis = new HashMap<>();
        yAxis.put("type", "value");
        options.put("yAxis", yAxis);

        // 数据系列
        options.put("series", series);

        // 转换为JSON字符串
        return convertToJsonString(options);
    }


    // 使用你喜欢的JSON库将Map转换为JSON字符串
    private String convertToJsonString(Map<String, Object> map) {
        // 这里可以使用Jackson, Gson或其他JSON库
        // 例如使用Jackson:
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            logger.error("转换为JSON字符串失败", e);
            return "{}";
        }
    }
}
