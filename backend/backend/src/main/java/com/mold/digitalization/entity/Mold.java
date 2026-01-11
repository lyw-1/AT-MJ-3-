package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * 妯″叿实体绫?
 * 瀵瑰簲数据搴撹〃锛歮old
 */
@Data
@TableName("mold")
public class Mold {

    /**
     * 妯″叿ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 妯″叿缂栧彿锛屽敮涓€
     */
    @NotBlank(message = "Mold code must not be blank")
    @Size(max = 50, message = "Mold code must not exceed 50 characters")
    private String moldCode;

    /**
     * 妯″叿鍚嶇О
     */
    @NotBlank(message = "Mold name must not be blank")
    @Size(max = 100, message = "Mold name must not exceed 100 characters")
    private String moldName;

    /**
     * 妯″叿绫诲瀷ID锛屽閿叧鑱攎old_type琛?
     */
    @NotNull(message = "Mold type ID must not be null")
    private Long moldTypeId;

    /**
     * 妯″叿鐘舵€両D锛屽閿叧鑱攎old_status琛?
     */
    @NotNull(message = "Mold status ID must not be null")
    private Long moldStatusId;

    /**
     * 璁捐瀵垮懡锛堜娇鐢ㄦ鏁帮級
     */
    @Min(value = 0, message = "Design life must be non-negative")
    private Integer designLife;

    /**
     * 褰撳墠浣跨敤娆℃暟
     */
    @Min(value = 0, message = "Current usage count must be non-negative")
    private Integer currentUsageCount;

    /**
     * 璐熻矗浜篒D锛屽閿叧鑱攗ser琛?
     */
    @NotNull(message = "Responsible user ID must not be null")
    private Long responsibleUserId;

    /**
     * 鍏ュ簱鏃堕棿
     */
    private LocalDateTime storageTime;

    /**
     * 棰勮鎶ュ簾鏃堕棿
     */
    private LocalDateTime estimatedScrapTime;

    /**
     * 澶囨敞淇℃伅
     */
    @Size(max = 500, message = "Remark must not exceed 500 characters")
    private String remark;

    /**
     * 创建鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 更新鏃堕棿
     */
    private LocalDateTime updateTime;
    
    // 鎵╁睍灞炴€?
    
    /**
     * 妯″叿鏉愯川
     */
    @TableField("material")
    @Size(max = 50, message = "Material must not exceed 50 characters")
    private String material;
    
    /**
     * 妯″叿灏哄锛堥暱锛屽崟浣嶏細mm锛?
     */
    @TableField("length")
    @Min(value = 0, message = "Length must be non-negative")
    private Double length;
    
    /**
     * 妯″叿灏哄锛堝锛屽崟浣嶏細mm锛?
     */
    @TableField("width")
    @Min(value = 0, message = "Width must be non-negative")
    private Double width;
    
    /**
     * 妯″叿灏哄锛堥珮锛屽崟浣嶏細mm锛?
     */
    @TableField("height")
    @Min(value = 0, message = "Height must be non-negative")
    private Double height;
    
    /**
     * 妯″叿閲嶉噺锛堝崟浣嶏細kg锛?
     */
    @TableField("weight")
    @Min(value = 0, message = "Weight must be non-negative")
    private Double weight;
    
    /**
     * 瀛樻斁浣嶇疆
     */
    @TableField("location")
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;
    
    /**
     * 鐗堟湰鍙?
     */
    @TableField("version")
    @Size(max = 50, message = "Version must not exceed 50 characters")
    private String version;
    
    /**
     * 璁捐閮ㄩ棬
     */
    @TableField("design_department")
    @Size(max = 100, message = "Design department must not exceed 100 characters")
    private String designDepartment;
    
    /**
     * 鐢熶骇鍘傚晢
     */
    @TableField("manufacturer")
    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;
    
    /**
     * 閲囪喘日期
     */
    @TableField("purchase_date")
    private LocalDateTime purchaseDate;
    
    @TableField("is_key")
    private Boolean isKey;
    
    // 鎵嬪姩添加setter方法浠ョ‘淇濈紪璇戦€氳繃
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() { return id; }
    
    public void setMoldStatusId(Long moldStatusId) {
        this.moldStatusId = moldStatusId;
    }
}
