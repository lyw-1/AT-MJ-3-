package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("slotting_layer_record")
public class SlottingLayerRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("process_id")
    private Long processId;

    @TableField("axis")
    private String axis;

    @TableField("layer_index")
    private Integer layerIndex;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    public void setProcessId(Long processId) { this.processId = processId; }
}
