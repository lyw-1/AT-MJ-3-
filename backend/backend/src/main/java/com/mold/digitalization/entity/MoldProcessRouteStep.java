package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mold_process_route_step")
public class MoldProcessRouteStep {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("mold_id")
    private Long moldId;
    @TableField("step_code")
    private String stepCode;
    @TableField("step_name")
    private String stepName;
    @TableField("order_no")
    private Integer orderNo;
    @TableField("equipment_id")
    private Long equipmentId;
    @TableField("operator_id")
    private Long operatorId;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
