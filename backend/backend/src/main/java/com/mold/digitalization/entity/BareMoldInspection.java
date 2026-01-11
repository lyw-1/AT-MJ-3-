package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("bare_mold_inspection")
public class BareMoldInspection {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("mold_id")
    private Long moldId;

    @TableField("mold_code")
    private String moldCode;

    @TableField("inspector_id")
    private Long inspectorId;

    @TableField("inspector_name")
    private String inspectorName;

    @TableField("inspection_time")
    private LocalDateTime inspectionTime;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setMoldCode(String moldCode) { this.moldCode = moldCode; }
    public void setMoldId(Long moldId) { this.moldId = moldId; }
    public void setInspectorId(Long inspectorId) { this.inspectorId = inspectorId; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }
    public void setInspectionTime(LocalDateTime inspectionTime) { this.inspectionTime = inspectionTime; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
