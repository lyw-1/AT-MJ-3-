package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mold_archive")
public class MoldArchive {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("mold_code")
    private String moldCode;

    @TableField("product_spec")
    private String productSpec;

    @TableField("category")
    private String category;

    @TableField("is_key")
    private Integer isKey;

    @TableField("structure")
    private String structure;

    @TableField("coating")
    private String coating;

    @TableField("material")
    private String material;

    @TableField("hrc")
    private String hrc;

    @TableField("total_shrinkage")
    private Double totalShrinkage;

    @TableField("core_size")
    private String coreSize;

    @TableField("outline")
    private String outline;

    @TableField("thickness")
    private Double thickness;

    @TableField("location_hole_pitch")
    private Double locationHolePitch;

    @TableField("inlet_diameter")
    private Double inletDiameter;

    @TableField("slot_depth")
    private Double slotDepth;

    @TableField("cut_in_amount")
    private Double cutInAmount;

    @TableField("feed_ratio")
    private Double feedRatio;

    @TableField("self_inspection_score")
    private Double selfInspectionScore;

    @TableField("processing_owner")
    private String processingOwner;

    @TableField("acceptance_score")
    private Double acceptanceScore;

    @TableField("storage_area")
    private String storageArea;

    @TableField("bin_location")
    private String binLocation;

    @TableField("status_tracking")
    private String statusTracking;

    @TableField("handler")
    private String handler;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setStatusTracking(String statusTracking) { this.statusTracking = statusTracking; }
    public void setHandler(String handler) { this.handler = handler; }
    public void setId(Long id) { this.id = id; }
}
