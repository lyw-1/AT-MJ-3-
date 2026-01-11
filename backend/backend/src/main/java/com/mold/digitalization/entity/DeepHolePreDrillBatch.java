package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("deep_hole_pre_drill_batch")
public class DeepHolePreDrillBatch {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("process_id")
    private Long processId;

    @TableField("hole_count")
    private Integer holeCount;

    @TableField("depth")
    private Double depth;

    @TableField("single_hole_time_sec")
    private Integer singleHoleTimeSec;

    @TableField("batch_time_sec")
    private Integer batchTimeSec;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    public void setProcessId(Long processId) { this.processId = processId; }
    public Integer getSingleHoleTimeSec() { return singleHoleTimeSec; }
    public Integer getHoleCount() { return holeCount; }
    public void setBatchTimeSec(Integer batchTimeSec) { this.batchTimeSec = batchTimeSec; }
}
