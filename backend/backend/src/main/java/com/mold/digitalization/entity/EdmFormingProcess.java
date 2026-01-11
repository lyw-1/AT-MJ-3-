package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("edm_forming_process")
public class EdmFormingProcess {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("mold_id")
    private Long moldId;

    @TableField("mold_code")
    private String moldCode;

    @TableField("equipment_id")
    private Long equipmentId;

    @TableField("equipment_name")
    private String equipmentName;

    @TableField("stage")
    private String stage;

    @TableField("status")
    private Integer status;

    @TableField("expected_finish_time")
    private LocalDateTime expectedFinishTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setMoldCode(String moldCode) { this.moldCode = moldCode; }
    public void setMoldId(Long moldId) { this.moldId = moldId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    public void setStage(String stage) { this.stage = stage; }
    public void setStatus(Integer status) { this.status = status; }
    public void setExpectedFinishTime(LocalDateTime expectedFinishTime) { this.expectedFinishTime = expectedFinishTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
