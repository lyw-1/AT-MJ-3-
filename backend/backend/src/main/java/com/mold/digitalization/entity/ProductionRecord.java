package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 进度记录实体类
 * 对应数据库表：production_record
 */
@Data
@TableName("production_record")
public class ProductionRecord {

    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID，外键关联production_task表
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 工序，关联工序表
     */
    @TableField("process")
    private String process;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 操作人员ID，外键关联user表
     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 设备ID，外键关联equipment表
     */
    @TableField("equipment_id")
    private Long equipmentId;

    /**
     * 加工参数，JSON格式
     */
    @TableField("process_params")
    private String processParams;

    /**
     * 质量检查结果：0-未检查，1-合格，2-不合格
     */
    @TableField("quality_result")
    private Integer qualityResult;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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
