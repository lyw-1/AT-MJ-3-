package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("deep_hole_layer_record")
public class DeepHoleLayerRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("process_id")
    private Long processId;

    @TableField("layer_index")
    private Integer layerIndex;

    @TableField("depth")
    private Double depth;

    @TableField("multi_speed_hf")
    private String multiSpeedHf;

    @TableField("single_hole_time_sec")
    private Integer singleHoleTimeSec;

    @TableField("through_hole_count")
    private Integer throughHoleCount;

    @TableField("layer_time_sec")
    private Integer layerTimeSec;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("remark")
    private String remark;

    public void setProcessId(Long processId) { this.processId = processId; }
    public void setLayerTimeSec(Integer layerTimeSec) { this.layerTimeSec = layerTimeSec; }
    public Integer getSingleHoleTimeSec() { return singleHoleTimeSec; }
    public Integer getThroughHoleCount() { return throughHoleCount; }
}
