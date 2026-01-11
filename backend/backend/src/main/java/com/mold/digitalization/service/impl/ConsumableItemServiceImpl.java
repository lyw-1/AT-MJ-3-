package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.ConsumableItem;
import com.mold.digitalization.mapper.ConsumableItemMapper;
import com.mold.digitalization.service.ConsumableItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsumableItemServiceImpl extends ServiceImpl<ConsumableItemMapper, ConsumableItem> implements ConsumableItemService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean create(ConsumableItem item) {
        try {
            // 设置创建时间和更新时间
            if (item.getCreateTime() == null) {
                item.setCreateTime(LocalDateTime.now());
            }
            item.setUpdateTime(LocalDateTime.now());

            // 计算总价值
            item.calculateTotalValue();

            // 更新库存状态
            item.updateStockStatus();

            return save(item);
        } catch (Exception e) {
            String msg = String.valueOf(e.getMessage());
            if (msg.contains("doesn't exist") || msg.contains("does not exist") || msg.contains("Unknown table")) {
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `consumable_item` (\n" +
                        "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                        "  `material_code` VARCHAR(100) NULL,\n" +
                        "  `item_name` VARCHAR(200) NOT NULL,\n" +
                        "  `item_category` VARCHAR(100) NULL,\n" +
                        "  `specification` VARCHAR(200) NULL,\n" +
                        "  `unit` VARCHAR(50) NULL,\n" +
                        "  `current_stock` INT NULL,\n" +
                        "  `min_stock` INT NULL,\n" +
                        "  `max_stock` INT NULL,\n" +
                        "  `stock_status` VARCHAR(50) NULL,\n" +
                        "  `unit_price` DOUBLE NULL,\n" +
                        "  `total_value` DOUBLE NULL,\n" +
                        "  `supplier` VARCHAR(200) NULL,\n" +
                        "  `location` VARCHAR(200) NULL,\n" +
                        "  `remarks` VARCHAR(500) NULL,\n" +
                        "  `create_time` DATETIME NULL,\n" +
                        "  `update_time` DATETIME NULL,\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  INDEX `idx_item_name` (`item_name`),\n" +
                        "  INDEX `idx_material_code` (`material_code`),\n" +
                        "  INDEX `idx_stock_status` (`stock_status`),\n" +
                        "  INDEX `idx_supplier` (`supplier`),\n" +
                        "  INDEX `idx_location` (`location`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
                return save(item);
            } else {
                // 尝试添加缺失的列
                addMissingColumns();
                return save(item);
            }
        }
    }

    @Override
    public boolean updateById(ConsumableItem item) {
        // 更新时间
        item.setUpdateTime(LocalDateTime.now());
        // 计算总价值
        item.calculateTotalValue();
        // 更新库存状态
        item.updateStockStatus();
        return super.updateById(item);
    }

    @Override
    public boolean isMaterialCodeUnique(String materialCode, Long id) {
        // 使用QueryWrapper查询是否存在相同的物料编码
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ConsumableItem> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("material_code", materialCode);
        
        // 如果id不为空，说明是编辑场景，需要排除当前记录
        if (id != null) {
            queryWrapper.ne("id", id);
        }
        
        // 查询是否存在相同的物料编码
        ConsumableItem existingItem = getOne(queryWrapper, false);
        // 如果不存在，返回true表示唯一；如果存在，返回false表示重复
        return existingItem == null;
    }
    
    /**
     * 添加缺失的列
     */
    private void addMissingColumns() {
        try {
            // 检查并添加specification列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `specification` VARCHAR(200) NULL;");
            // 检查并添加min_stock列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `min_stock` INT NULL;");
            // 检查并添加max_stock列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `max_stock` INT NULL;");
            // 检查并添加stock_status列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `stock_status` VARCHAR(50) NULL;");
            // 检查并添加unit_price列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `unit_price` DOUBLE NULL;");
            // 检查并添加total_value列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `total_value` DOUBLE NULL;");
            // 检查并添加supplier列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `supplier` VARCHAR(200) NULL;");
            // 检查并添加location列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `location` VARCHAR(200) NULL;");
            // 检查并添加remarks列
            jdbcTemplate.execute("ALTER TABLE `consumable_item` ADD COLUMN IF NOT EXISTS `remarks` VARCHAR(500) NULL;");
        } catch (Exception e) {
            // 忽略添加列失败的异常，继续执行保存操作
            System.out.println("添加列失败: " + e.getMessage());
        }
    }
}
