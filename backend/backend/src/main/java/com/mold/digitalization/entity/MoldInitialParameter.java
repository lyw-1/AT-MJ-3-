package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mold_initial_parameter")
public class MoldInitialParameter {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("application_no")
    private String applicationNo;

    @TableField("category")
    private String category;

    @TableField("mold_code")
    private String moldCode;

    @TableField("product_spec")
    private String productSpec;

    @TableField("material")
    private String material;

    @TableField("hrc")
    private String hrc;

    @TableField("structure")
    private String structure;

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

    @TableField("hole_count")
    private Integer holeCount;

    @TableField("hole_depth")
    private Double holeDepth;

    @TableField("porosity_type")
    private String porosityType;

    @TableField("slot_width")
    private Double slotWidth;

    @TableField("slot_depth")
    private Double slotDepth;

    @TableField("cut_in_amount")
    private Double cutInAmount;

    @TableField("center_distance")
    private Double centerDistance;

    @TableField("feed_ratio")
    private Double feedRatio;

    @TableField("core_step_height")
    private Double coreStepHeight;

    @TableField("owner_name")
    private String ownerName;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setId(Long id) { this.id = id; }
    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }
    public String getCategory() { return category; }
    public String getMoldCode() { return moldCode; }
    public String getProductSpec() { return productSpec; }
    public String getMaterial() { return material; }
    public void setCategory(String category) { this.category = category; }
    public void setMoldCode(String moldCode) { this.moldCode = moldCode; }
    public void setProductSpec(String productSpec) { this.productSpec = productSpec; }
    public void setMaterial(String material) { this.material = material; }
    public String getHrc() { return hrc; }
    public String getStructure() { return structure; }
    public Double getTotalShrinkage() { return totalShrinkage; }
    public String getCoreSize() { return coreSize; }
    public String getOutline() { return outline; }
    public Double getThickness() { return thickness; }
    public Double getLocationHolePitch() { return locationHolePitch; }
    public void setHrc(String hrc) { this.hrc = hrc; }
    public void setStructure(String structure) { this.structure = structure; }
    public void setTotalShrinkage(Double totalShrinkage) { this.totalShrinkage = totalShrinkage; }
    public void setCoreSize(String coreSize) { this.coreSize = coreSize; }
    public void setOutline(String outline) { this.outline = outline; }
    public void setThickness(Double thickness) { this.thickness = thickness; }
    public void setLocationHolePitch(Double locationHolePitch) { this.locationHolePitch = locationHolePitch; }
    public Double getInletDiameter() { return inletDiameter; }
    public Integer getHoleCount() { return holeCount; }
    public Double getHoleDepth() { return holeDepth; }
    public String getPorosityType() { return porosityType; }
    public void setInletDiameter(Double inletDiameter) { this.inletDiameter = inletDiameter; }
    public void setHoleCount(Integer holeCount) { this.holeCount = holeCount; }
    public void setHoleDepth(Double holeDepth) { this.holeDepth = holeDepth; }
    public void setPorosityType(String porosityType) { this.porosityType = porosityType; }
    public Double getSlotWidth() { return slotWidth; }
    public Double getSlotDepth() { return slotDepth; }
    public Double getCutInAmount() { return cutInAmount; }
    public Double getCenterDistance() { return centerDistance; }
    public Double getFeedRatio() { return feedRatio; }
    public Double getCoreStepHeight() { return coreStepHeight; }
    public String getOwnerName() { return ownerName; }
    public void setSlotWidth(Double slotWidth) { this.slotWidth = slotWidth; }
    public void setSlotDepth(Double slotDepth) { this.slotDepth = slotDepth; }
    public void setCutInAmount(Double cutInAmount) { this.cutInAmount = cutInAmount; }
    public void setCenterDistance(Double centerDistance) { this.centerDistance = centerDistance; }
    public void setFeedRatio(Double feedRatio) { this.feedRatio = feedRatio; }
    public void setCoreStepHeight(Double coreStepHeight) { this.coreStepHeight = coreStepHeight; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getId() { return id; }
}
