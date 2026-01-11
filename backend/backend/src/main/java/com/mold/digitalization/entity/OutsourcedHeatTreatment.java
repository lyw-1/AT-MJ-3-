package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("outsourced_heat_treatment")
public class OutsourcedHeatTreatment {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("mold_id")
    private Long moldId;

    @TableField("mold_code")
    private String moldCode;

    @TableField("type")
    private Integer type;

    @TableField("supplier")
    private String supplier;

    @TableField("target_hrc")
    private String targetHrc;

    @TableField("status")
    private Integer status;

    @TableField("expected_finish_time")
    private LocalDateTime expectedFinishTime;

    @TableField("shipped_time")
    private LocalDateTime shippedTime;

    @TableField("returned_time")
    private LocalDateTime returnedTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setTargetHrc(String targetHrc) { this.targetHrc = targetHrc; }
    public void setExpectedFinishTime(LocalDateTime expectedFinishTime) { this.expectedFinishTime = expectedFinishTime; }
    public void setStatus(Integer status) { this.status = status; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public void setShippedTime(LocalDateTime shippedTime) { this.shippedTime = shippedTime; }
    public void setMoldCode(String moldCode) { this.moldCode = moldCode; }
    public void setMoldId(Long moldId) { this.moldId = moldId; }
    public void setType(Integer type) { this.type = type; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public void setReturnedTime(LocalDateTime returnedTime) { this.returnedTime = returnedTime; }
}
