package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工序预设置实体类
 * 对应数据库表：process_preset
 */
@Data
@TableName("process_preset")
public class ProcessPreset {

    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模具ID，外键关联mold表
     */
    @TableField("mold_id")
    private Long moldId;

    /**
     * 工序代码，关联工序路线的step_code
     */
    @TableField("process_code")
    private String processCode;

    /**
     * 预设置键
     */
    @TableField("preset_key")
    private String presetKey;

    /**
     * 预设置值
     */
    @TableField("preset_value")
    private String presetValue;

    /**
     * 描述信息
     */
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
