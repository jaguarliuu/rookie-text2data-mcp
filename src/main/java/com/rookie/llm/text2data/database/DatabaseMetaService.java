package com.rookie.llm.text2data.database;

import com.rookie.llm.text2data.config.AppConfig;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface DatabaseMetaService {
    /**
     * 获取数据库连接
     */
    Connection connect(AppConfig.DatabaseConfig config) throws SQLException;
    /**
     * 获取所有表名及注释
     */
    List<Map<String, String>> getAllTableNamesAndComments(AppConfig.DatabaseConfig config);
    /**
     * 获取所有表的 DDL
     */
    Map<String, String> getAllTablesDDL(AppConfig.DatabaseConfig config);
    /**
     * 获取指定表的 DDL
     */
    String getSingleTableDDL(AppConfig.DatabaseConfig config, String tableName);
}
