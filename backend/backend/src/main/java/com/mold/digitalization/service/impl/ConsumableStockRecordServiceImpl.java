package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.ConsumableStockRecord;
import com.mold.digitalization.mapper.ConsumableStockRecordMapper;
import com.mold.digitalization.service.ConsumableStockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsumableStockRecordServiceImpl extends ServiceImpl<ConsumableStockRecordMapper, ConsumableStockRecord> implements ConsumableStockRecordService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean createRecord(ConsumableStockRecord record) {
        try {
            if (record.getCreateTime() == null) record.setCreateTime(LocalDateTime.now());
            return save(record);
        } catch (Exception e) {
            String msg = String.valueOf(e.getMessage());
            if (msg.contains("doesn't exist") || msg.contains("does not exist") || msg.contains("Unknown table")) {
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `consumable_stock_record` (\n" +
                        "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                        "  `item_id` BIGINT NULL,\n" +
                        "  `item_name` VARCHAR(200) NULL,\n" +
                        "  `item_category` VARCHAR(100) NULL,\n" +
                        "  `item_spec` VARCHAR(200) NULL,\n" +
                        "  `record_type` VARCHAR(10) NOT NULL,\n" +
                        "  `qty` INT NOT NULL,\n" +
                        "  `operator` VARCHAR(100) NULL,\n" +
                        "  `remark` VARCHAR(500) NULL,\n" +
                        "  `create_time` DATETIME NULL,\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  INDEX `idx_record_item` (`item_id`),\n" +
                        "  INDEX `idx_record_time` (`create_time`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
                return save(record);
            }
            throw e;
        }
    }
}
