package com.rookie.llm.text2data.service;

import com.rookie.llm.text2data.config.AppConfig;
import com.rookie.llm.text2data.database.DatabaseMetaFactory;
import com.rookie.llm.text2data.database.DatabaseMetaService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import java.sql.Connection;
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

    private final AppConfig appConfig;

    public Text2DataService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    private DatabaseMetaService getMetaService() {
        return DatabaseMetaFactory.getMetaService(appConfig.getDatabase().getDbType());
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
}
