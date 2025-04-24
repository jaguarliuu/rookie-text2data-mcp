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

    /**
     * 执行 SQL 语句并返回结果
     * @param config 数据库配置
     * @param sql 要执行的 SQL 语句
     * @param params SQL 参数（可选）
     * @return 查询结果列表，每行数据以 Map 形式返回
     */
    List<Map<String, Object>> executeQuery(AppConfig.DatabaseConfig config, String sql);
}
