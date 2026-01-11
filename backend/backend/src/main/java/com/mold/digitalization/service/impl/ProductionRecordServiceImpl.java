package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.ProductionRecord;
import com.mold.digitalization.mapper.ProductionRecordMapper;
import com.mold.digitalization.service.ProductionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 进度记录ServiceImpl
 */
@Service
public class ProductionRecordServiceImpl extends ServiceImpl<ProductionRecordMapper, ProductionRecord> implements ProductionRecordService {

    @Autowired
    private ProductionRecordMapper mapper;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ProductionRecord> getByTaskId(Long taskId) {
        return mapper.selectByTaskId(taskId);
    }

    @Override
    public ProductionRecord getByTaskIdAndProcess(Long taskId, String process) {
        return mapper.selectByTaskIdAndProcess(taskId, process);
    }

    @Override
    public ProductionRecord getLatestByTaskId(Long taskId) {
        return mapper.selectLatestByTaskId(taskId);
    }

    @Override
    public List<ProductionRecord> getProductionRecordsByMoldId(Long moldId) {
        return mapper.selectByMoldId(moldId);
    }

    @Override
    public boolean create(ProductionRecord record) {
        try {
            return save(record);
        } catch (Exception e) {
            String msg = String.valueOf(e.getMessage());
            if (msg.contains("doesn't exist") || msg.contains("does not exist") || msg.contains("Unknown table")) {
                // 自动创建表
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `production_record` (" +
                        "  `id` BIGINT NOT NULL AUTO_INCREMENT," +
                        "  `task_id` BIGINT NULL," +
                        "  `process` VARCHAR(100) NULL," +
                        "  `start_time` DATETIME NULL," +
                        "  `end_time` DATETIME NULL," +
                        "  `operator_id` BIGINT NULL," +
                        "  `equipment_id` BIGINT NULL," +
                        "  `process_params` TEXT NULL," +
                        "  `quality_result` INT NULL DEFAULT 0," +
                        "  `remark` VARCHAR(500) NULL," +
                        "  `create_time` DATETIME NULL," +
                        "  `update_time` DATETIME NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  INDEX `idx_pr_task_id` (`task_id`)," +
                        "  INDEX `idx_pr_process` (`process`)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
                return save(record);
            }
            throw e;
        }
    }

    @Override
    public boolean updateRecord(ProductionRecord record) {
        return updateById(record);
    }

    @Override
    public boolean deleteRecord(Long id) {
        return removeById(id);
    }
}
