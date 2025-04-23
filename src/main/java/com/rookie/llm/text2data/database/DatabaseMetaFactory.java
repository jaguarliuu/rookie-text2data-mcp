package com.rookie.llm.text2data.database;

/**
 * Name：DatabaseMetaFactory
 * Author：eumenides
 * Created on: 2025/4/23
 * Description:
 */
public class DatabaseMetaFactory {
    public static DatabaseMetaService getMetaService(String dbType) {
        return switch (dbType.toUpperCase()) {
            case "MYSQL" -> new MySQLMetaService();
            // 未来支持 PostgreSQL、SQLite 等可在此扩展
            // case "POSTGRES" -> new PostgreSQLMetaService();
            default -> throw new UnsupportedOperationException("暂不支持的数据库类型: " + dbType);
        };
    }
}
