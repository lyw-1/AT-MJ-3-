package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import jakarta.validation.constraints.*;

/**
 * 成品实体类
 */
@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 成品类别
     */
    @TableField("product_category")
    @NotBlank(message = "成品类别不能为空")
    @Size(max = 255, message = "成品类别长度不能超过255个字符")
    private String productCategory;
    
    /**
     * 成品规格
     */
    @TableField("product_spec")
    @NotBlank(message = "成品规格不能为空")
    @Size(max = 255, message = "成品规格长度不能超过255个字符")
    private String productSpec;
    
    /**
     * 容重要求最小值
     */
    @TableField("density_requirement_min")
    @Min(value = 0, message = "容重要求最小值不能小于0")
    private Double densityRequirementMin;
    
    /**
     * 容重要求最大值
     */
    @TableField("density_requirement_max")
    @Min(value = 0, message = "容重要求最大值不能小于0")
    private Double densityRequirementMax;
    
    /**
     * 壁厚要求
     */
    @TableField("wall_thickness_requirement")
    @Min(value = 0, message = "壁厚要求不能小于0")
    private Double wallThicknessRequirement;
    
    /**
     * 槽宽要求最小值
     */
    @TableField("slot_width_requirement_min")
    @Min(value = 0, message = "槽宽要求最小值不能小于0")
    private Double slotWidthRequirementMin;
    
    /**
     * 槽宽要求最大值
     */
    @TableField("slot_width_requirement_max")
    @Min(value = 0, message = "槽宽要求最大值不能小于0")
    private Double slotWidthRequirementMax;
    
    /**
     * 其他特殊要求
     */
    @TableField("other_requirements")
    @Size(max = 500, message = "其他特殊要求长度不能超过500个字符")
    private String otherRequirements;
    
    /**
     * 客户名称
     */
    @TableField("customer")
    @Size(max = 255, message = "客户名称长度不能超过255个字符")
    private String customer;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    @Size(max = 50, message = "创建时间长度不能超过50个字符")
    private String createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    @Size(max = 50, message = "更新时间长度不能超过50个字符")
    private String updateTime;
}
