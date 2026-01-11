package com.mold.digitalization.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
@Profile("dev")
public class DepartmentSchemaInitializer implements org.springframework.boot.CommandLineRunner {
    private final DataSource dataSource;
    public DepartmentSchemaInitializer(DataSource dataSource) { this.dataSource = dataSource; }

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            if (!tableExists(conn, "department")) {
                stmt.execute("CREATE TABLE department (\n" +
                        "  id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "  name VARCHAR(100) NOT NULL,\n" +
                        "  parent_id BIGINT DEFAULT NULL,\n" +
                        "  sort_order INT DEFAULT 0,\n" +
                        "  status TINYINT DEFAULT 1,\n" +
                        "  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                        "  INDEX idx_name (name),\n" +
                        "  INDEX idx_status (status),\n" +
                        "  INDEX idx_parent_id (parent_id)\n" +
                        ")");
                seed(stmt);
            } else {
                ensureColumn(stmt, conn, "department", "name", "ALTER TABLE department ADD COLUMN name VARCHAR(100) NOT NULL");
                ensureColumn(stmt, conn, "department", "parent_id", "ALTER TABLE department ADD COLUMN parent_id BIGINT DEFAULT NULL");
                ensureColumn(stmt, conn, "department", "sort_order", "ALTER TABLE department ADD COLUMN sort_order INT DEFAULT 0");
                ensureColumn(stmt, conn, "department", "status", "ALTER TABLE department ADD COLUMN status TINYINT DEFAULT 1");
                ensureColumn(stmt, conn, "department", "create_time", "ALTER TABLE department ADD COLUMN create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                ensureColumn(stmt, conn, "department", "update_time", "ALTER TABLE department ADD COLUMN update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
                // 添加索引
                try {
                    stmt.execute("CREATE INDEX idx_name ON department(name)");
                } catch (Exception ignored) { }
                try {
                    stmt.execute("CREATE INDEX idx_status ON department(status)");
                } catch (Exception ignored) { }
                try {
                    stmt.execute("CREATE INDEX idx_parent_id ON department(parent_id)");
                } catch (Exception ignored) { }
            }

            // 用户-部门关联表
            if (!tableExists(conn, "user_department")) {
                stmt.execute("CREATE TABLE user_department (\n" +
                        "  user_id BIGINT NOT NULL,\n" +
                        "  department_id BIGINT NOT NULL,\n" +
                        "  UNIQUE KEY uk_user (user_id),\n" +
                        "  INDEX idx_department (department_id)\n" +
                        ")");
            } else {
                // 添加department_id索引
                try {
                    stmt.execute("CREATE INDEX idx_department ON user_department(department_id)");
                } catch (Exception ignored) { }
            }
        } catch (Exception ignored) { }
    }

    private boolean tableExists(Connection conn, String table) {
        try (Statement s = conn.createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT COUNT(*) AS cnt FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()) AND table_name='" + table + "' AND table_name='" + table + "'")) {
                return rs.next() && rs.getInt("cnt") > 0;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureColumn(Statement stmt, Connection conn, String table, String column, String ddl) {
        try (Statement check = conn.createStatement()) {
            String sql = "SELECT COUNT(*) AS cnt FROM information_schema.columns WHERE table_schema = (SELECT DATABASE()) AND table_name='" + table + "' AND column_name='" + column + "'";
            try (ResultSet rs = check.executeQuery(sql)) { if (rs.next() && rs.getInt("cnt") == 0) stmt.execute(ddl); }
        } catch (Exception ignored) {}
    }

    private void seed(Statement stmt) {
        try {
            stmt.execute("INSERT INTO department(name, status) VALUES ('研发部',1),('生产部',1),('质检部',1),('设备部',1)");
        } catch (Exception ignored) {}
    }
}
