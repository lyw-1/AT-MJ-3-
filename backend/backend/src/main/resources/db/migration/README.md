# Flyway 迁移目录

将增量数据库变更以 `V<version>__<description>.sql` 命名放置在此目录，例如：

- `V1__init_schema.sql`
- `V2__add_operation_log_indexes.sql`
- `V3__alter_user_lock_columns.sql`

使用建议：
- 开发环境可启用：`spring.flyway.enabled=true`、`spring.flyway.baseline-on-migrate=true`
- 首次接入已有数据库时开启 baseline，以现有结构作为基线，后续变更通过增量脚本管理
- 生产环境变更通过审批与灰度发布，确保备份与回滚脚本可用

