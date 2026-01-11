package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.MoldInitialParameter;
import com.mold.digitalization.mapper.MoldInitialParameterMapper;
import com.mold.digitalization.service.MoldInitialParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoldInitialParameterServiceImpl extends ServiceImpl<MoldInitialParameterMapper, MoldInitialParameter> implements MoldInitialParameterService {

    @Autowired
    private MoldInitialParameterMapper mapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MoldInitialParameter> getByMoldCode(String moldCode) {
        return mapper.selectByMoldCode(moldCode);
    }

    @Override
    public MoldInitialParameter getLatestByMoldCode(String moldCode) {
        return mapper.selectLatestByMoldCode(moldCode);
    }

    @Override
    public MoldInitialParameter getByApplicationNo(String applicationNo) {
        return mapper.selectByApplicationNo(applicationNo);
    }

    @Override
    public boolean create(MoldInitialParameter param) {
        try {
            return save(param);
        } catch (Exception e) {
            String msg = String.valueOf(e.getMessage());
            if (msg.contains("doesn't exist") || msg.contains("does not exist") || msg.contains("Unknown table")) {
                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `mold_initial_parameter` (\n" +
                        "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                        "  `application_no` VARCHAR(100) NULL,\n" +
                        "  `category` VARCHAR(100) NULL,\n" +
                        "  `mold_code` VARCHAR(100) NULL,\n" +
                        "  `product_spec` VARCHAR(255) NULL,\n" +
                        "  `material` VARCHAR(100) NULL,\n" +
                        "  `hrc` VARCHAR(50) NULL,\n" +
                        "  `structure` VARCHAR(255) NULL,\n" +
                        "  `total_shrinkage` DOUBLE NULL,\n" +
                        "  `core_size` VARCHAR(100) NULL,\n" +
                        "  `outline` VARCHAR(255) NULL,\n" +
                        "  `thickness` DOUBLE NULL,\n" +
                        "  `location_hole_pitch` DOUBLE NULL,\n" +
                        "  `inlet_diameter` DOUBLE NULL,\n" +
                        "  `hole_count` INT NULL,\n" +
                        "  `hole_depth` DOUBLE NULL,\n" +
                        "  `porosity_type` VARCHAR(100) NULL,\n" +
                        "  `slot_width` DOUBLE NULL,\n" +
                        "  `slot_depth` DOUBLE NULL,\n" +
                        "  `cut_in_amount` DOUBLE NULL,\n" +
                        "  `center_distance` DOUBLE NULL,\n" +
                        "  `feed_ratio` DOUBLE NULL,\n" +
                        "  `core_step_height` DOUBLE NULL,\n" +
                        "  `owner_name` VARCHAR(100) NULL,\n" +
                        "  `remark` VARCHAR(500) NULL,\n" +
                        "  `create_time` DATETIME NULL,\n" +
                        "  `update_time` DATETIME NULL,\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  INDEX `idx_mip_mold_code` (`mold_code`),\n" +
                        "  INDEX `idx_mip_application_no` (`application_no`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
                return save(param);
            }
            throw e;
        }
    }

    @Override
    public boolean updateParam(MoldInitialParameter param) {
        return updateById(param);
    }

    @Override
    public boolean deleteParam(Long id) {
        return removeById(id);
    }
}
