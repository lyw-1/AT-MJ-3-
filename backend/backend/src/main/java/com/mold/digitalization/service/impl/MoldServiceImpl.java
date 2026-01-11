package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.MoldMapper;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.service.MoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

/**
 * 妯″叿服务实现绫?
 * 实现妯″叿鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
@Service
public class MoldServiceImpl extends ServiceImpl<MoldMapper, Mold> implements MoldService {
    
    @Autowired
    private MoldMapper moldMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Mold getMoldByCode(String moldCode) {
        return moldMapper.selectByMoldCode(moldCode);
    }
    
    @Override
    public List<Mold> getMoldsByTypeId(Long typeId) {
        return moldMapper.selectByTypeId(typeId);
    }
    
    @Override
    public List<Mold> getMoldsByStatusId(Long statusId) {
        return moldMapper.selectByStatusId(statusId);
    }
    
    @Override
    public boolean createMold(Mold mold) {
        try {
            return save(mold);
        } catch (Exception e) {
            String msg = String.valueOf(e.getMessage());
            if (msg.contains("doesn't exist") || msg.contains("does not exist") || msg.contains("Unknown table")) {
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `mold` (\n" +
                        "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                        "  `mold_code` VARCHAR(50) NOT NULL,\n" +
                        "  `mold_name` VARCHAR(100) NOT NULL,\n" +
                        "  `mold_type_id` BIGINT NOT NULL,\n" +
                        "  `mold_status_id` BIGINT NOT NULL,\n" +
                        "  `responsible_user_id` BIGINT NOT NULL,\n" +
                        "  `design_life` INT NULL,\n" +
                        "  `current_usage_count` INT NULL,\n" +
                        "  `storage_time` DATETIME NULL,\n" +
                        "  `estimated_scrap_time` DATETIME NULL,\n" +
                        "  `remark` VARCHAR(500) NULL,\n" +
                        "  `material` VARCHAR(50) NULL,\n" +
                        "  `length` DOUBLE NULL,\n" +
                        "  `width` DOUBLE NULL,\n" +
                        "  `height` DOUBLE NULL,\n" +
                        "  `weight` DOUBLE NULL,\n" +
                        "  `location` VARCHAR(100) NULL,\n" +
                        "  `version` VARCHAR(50) NULL,\n" +
                        "  `design_department` VARCHAR(100) NULL,\n" +
                        "  `manufacturer` VARCHAR(100) NULL,\n" +
                        "  `purchase_date` DATETIME NULL,\n" +
                        "  `create_time` DATETIME NULL,\n" +
                        "  `update_time` DATETIME NULL,\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  UNIQUE KEY `uk_mold_code` (`mold_code`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
                return save(mold);
            }
            throw e;
        }
    }
    
    @Override
    public boolean updateMold(Mold mold) {
        return updateById(mold);
    }
    
    @Override
    public boolean deleteMold(Long moldId) {
        // Check if mold exists
        Mold existingMold = getById(moldId);
        if (existingMold == null) {
            return false;
        }
        
        // 浣跨敤Mapper鐨刣eleteById方法删除
        return moldMapper.deleteById(moldId) > 0;
    }
    
    @Override
    public List<Mold> getAllMolds() {
        return list();
    }
    
    @Override
    public boolean updateMoldStatus(Long moldId, Long statusId) {
        // Check if mold exists
        Mold existingMold = getById(moldId);
        if (existingMold == null) {
            return false;
        }
        // Update status
        existingMold.setMoldStatusId(statusId);
        return updateById(existingMold);
    }
}
