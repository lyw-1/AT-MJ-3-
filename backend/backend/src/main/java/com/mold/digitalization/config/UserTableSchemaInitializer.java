package com.mold.digitalization.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 开发环境：启动时自动为 user 表补齐扩展列
 */
@Component
@Profile("dev")
public class UserTableSchemaInitializer implements org.springframework.boot.CommandLineRunner {
    private final DataSource dataSource;
    public UserTableSchemaInitializer(DataSource dataSource) { this.dataSource = dataSource; }

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            ensureColumn(stmt, conn, "user", "real_name", "ALTER TABLE `user` ADD COLUMN `real_name` VARCHAR(50) NULL COMMENT '真实姓名' AFTER `password`");
            ensureColumn(stmt, conn, "user", "department_name", "ALTER TABLE `user` ADD COLUMN `department_name` VARCHAR(100) NULL COMMENT '部门名称' AFTER `real_name`");
            ensureColumn(stmt, conn, "user", "phone", "ALTER TABLE `user` ADD COLUMN `phone` VARCHAR(20) NULL COMMENT '手机号' AFTER `department_name`");
            ensureColumn(stmt, conn, "user", "email", "ALTER TABLE `user` ADD COLUMN `email` VARCHAR(100) NULL COMMENT '邮箱' AFTER `phone`");
        } catch (Exception ignored) {}
    }

    private void ensureColumn(Statement stmt, Connection conn, String table, String column, String alterSql) {
        try (Statement check = conn.createStatement()) {
            String sql = "SELECT COUNT(*) AS cnt FROM information_schema.columns WHERE table_schema = (SELECT DATABASE()) AND table_name = '" + table + "' AND column_name = '" + column + "'";
            try (ResultSet rs = check.executeQuery(sql)) {
                if (rs.next() && rs.getInt("cnt") == 0) {
                    stmt.execute(alterSql);
                }
            }
        } catch (Exception ignored) {}
    }
}
