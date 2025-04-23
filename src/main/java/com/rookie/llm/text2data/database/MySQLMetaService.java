package com.rookie.llm.text2data.database;

import com.rookie.llm.text2data.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Name：MySQLMetaService
 * Author：eumenides
 * Created on: 2025/4/23
 * Description:
 */
public class MySQLMetaService implements DatabaseMetaService{
    private static final Logger logger = LoggerFactory.getLogger(MySQLMetaService.class);
    @Override
    public Connection connect(AppConfig.DatabaseConfig db) throws SQLException {
        String url = "jdbc:mysql://" + db.getHost() + ":" + db.getPort() + "/" + db.getDbName() + "?useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(url, db.getUsername(), db.getPassword());
    }

    @Override
    public List<Map<String, String>> getAllTableNamesAndComments(AppConfig.DatabaseConfig config) {
        List<Map<String, String>> tables = new ArrayList<>();
        String sql = "SELECT table_name, table_comment FROM information_schema.tables WHERE table_schema = ?";
        try (Connection conn = connect(config);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, config.getDbName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> tableInfo = new HashMap<>();
                tableInfo.put("name", rs.getString("table_name"));
                tableInfo.put("comment", rs.getString("table_comment"));
                tables.add(tableInfo);
            }
        } catch (SQLException e) {
            logger.error("❌ 获取 MySQL 表信息失败", e);
        }
        return tables;
    }

    @Override
    public Map<String, String> getAllTablesDDL(AppConfig.DatabaseConfig config) {
        Map<String, String> ddlMap = new LinkedHashMap<>();
        List<Map<String, String>> tables = getAllTableNamesAndComments(config);
        for (Map<String, String> table : tables) {
            String tableName = table.get("name");
            String ddl = getSingleTableDDL(config, tableName);
            ddlMap.put(tableName, ddl);
        }
        return ddlMap;
    }

    @Override
    public String getSingleTableDDL(AppConfig.DatabaseConfig config, String tableName) {
        String ddl = "";
        try (Connection conn = connect(config);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE `" + tableName + "`")) {
            if (rs.next()) {
                ddl = rs.getString("Create Table");
            }
        } catch (SQLException e) {
            logger.error("❌ 获取 MySQL 表 DDL 失败: " + tableName, e);
        }
        return ddl;
    }
}
