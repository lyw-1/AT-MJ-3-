package com.mold.digitalization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.annotation.PostConstruct;

@Configuration
public class DeviceModuleSchemaInitializer {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        if (jdbcTemplate == null) return;
        try {
            // 使用H2兼容的SQL语法，去除MySQL特定的ENGINE和CHARSET子句
            jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS equipment_maintenance_record (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                    "equipment_id BIGINT NOT NULL," +
                    "maintenance_time DATETIME NULL," +
                    "content VARCHAR(1000) NULL," +
                    "operator_user_id BIGINT NULL," +
                    "remark VARCHAR(255) NULL" +
                    ")"
            );
            // 单独创建索引，使用MySQL兼容的语法，忽略已存在的索引
            createIndexIfNotExists("idx_emr_equipment_id", "equipment_maintenance_record", "equipment_id");
            createIndexIfNotExists("idx_emr_maintenance_time", "equipment_maintenance_record", "maintenance_time");

            jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS equipment_accessory_replacement_record (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                    "equipment_id BIGINT NOT NULL," +
                    "accessory_id BIGINT NULL," +
                    "accessory_name VARCHAR(255) NULL," +
                    "accessory_code VARCHAR(255) NULL," +
                    "quantity INT NULL," +
                    "replace_time DATETIME NULL," +
                    "reason VARCHAR(500) NULL," +
                    "operator_user_id BIGINT NULL," +
                    "remark VARCHAR(255) NULL" +
                    ")"
            );
            // 单独创建索引，使用MySQL兼容的语法，忽略已存在的索引
            createIndexIfNotExists("idx_earr_equipment_id", "equipment_accessory_replacement_record", "equipment_id");
            createIndexIfNotExists("idx_earr_replace_time", "equipment_accessory_replacement_record", "replace_time");

            jdbcTemplate.execute(
                    "CREATE TABLE IF NOT EXISTS equipment_maintenance_cycle (" +
                    "equipment_id BIGINT PRIMARY KEY," +
                    "cycle_days INT NOT NULL" +
                    ")"
            );
        } catch (Exception e) {
            // 忽略表已存在等异常
            System.out.println("初始化设备模块表结构失败: " + e.getMessage());
        }
    }
    
    /**
     * 使用MySQL兼容的方式创建索引，忽略已存在的索引
     */
    private void createIndexIfNotExists(String indexName, String tableName, String columnName) {
        try {
            // 先检查索引是否存在，不存在才创建
            String checkIndexSql = "SELECT COUNT(*) FROM information_schema.statistics " +
                                 "WHERE table_schema = DATABASE() AND table_name = ? AND index_name = ?";
            Integer count = jdbcTemplate.queryForObject(checkIndexSql, Integer.class, tableName, indexName);
            if (count == null || count == 0) {
                String createIndexSql = "CREATE INDEX " + indexName + " ON " + tableName + "(" + columnName + ")";
                jdbcTemplate.execute(createIndexSql);
            }
        } catch (Exception e) {
            // 忽略索引创建失败的异常
            System.out.println("创建索引" + indexName + "失败: " + e.getMessage());
        }
    }
}

