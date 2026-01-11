package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Data
@TableName("tuning_record")
public class TuningRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("mold_id")
    private Long moldId;

    @TableField("mold_code")
    @NotBlank
    @Size(max = 50)
    private String moldCode;

    @TableField("image_url")
    private String imageUrl;

    @TableField("stage")
    @Size(max = 100)
    private String stage;

    @TableField("record_time")
    private LocalDateTime recordTime;

    @TableField("equipment")
    @Size(max = 100)
    private String equipment;

    @TableField("pressure")
    @PositiveOrZero
    private Double pressure;

    @TableField("flow_rate")
    @PositiveOrZero
    private Double flowRate;

    @TableField("mud_material")
    @Size(max = 100)
    private String mudMaterial;

    @TableField("mud_hardness")
    @PositiveOrZero
    private Double mudHardness;

    @TableField("remark")
    @Size(max = 500)
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setId(Long id) { this.id = id; }
}
