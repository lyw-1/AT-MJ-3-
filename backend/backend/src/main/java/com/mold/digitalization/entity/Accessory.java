package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * 閰嶄欢实体锟? * 瀵瑰簲数据搴撹〃锛歛ccessory
 * 记录妯″叿閰嶄欢鐨勫熀鏈俊鎭拰搴撳瓨鐘讹拷? */
@Data
@TableName("accessory")
public class Accessory {

    /**
     * 閰嶄欢ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 閰嶄欢缂栧彿锛屽敮涓€
     */
    @TableField("accessory_code")
    @NotBlank(message = "Accessory code must not be blank")
    @Size(max = 50, message = "Accessory code must not exceed 50 characters")
    private String accessoryCode;

    /**
     * 閰嶄欢鍚嶇О
     */
    @TableField("accessory_name")
    @NotBlank(message = "Accessory name must not be blank")
    @Size(max = 100, message = "Accessory name must not exceed 100 characters")
    private String accessoryName;

    /**
     * 閰嶄欢绫诲瀷ID锛屽閿叧鑱攁ccessory_type锟?     */
    @TableField("accessory_type_id")
    @NotNull(message = "閰嶄欢绫诲瀷ID涓嶈兘为空")
    private Long accessoryTypeId;

    /**
     * 閰嶄欢鏉愯川
     */
    @TableField("material")
    @Size(max = 50, message = "Material must not exceed 50 characters")
    private String material;

    /**
     * 閰嶄欢瑙勬牸
     */
    @TableField("specification")
    @Size(max = 100, message = "Specification must not exceed 100 characters")
    private String specification;

    /**
     * 璁￠噺鍗曚綅
     */
    @TableField("unit")
    @NotBlank(message = "Unit must not be blank")
    @Size(max = 20, message = "Unit must not exceed 20 characters")
    private String unit;

    /**
     * 褰撳墠搴撳瓨鏁伴噺
     */
    @TableField("stock_quantity")
    @Min(value = 0, message = "Stock quantity must be non-negative")
    private Integer stockQuantity;

    /**
     * 鏈€浣庡簱瀛橀槇锟?     */
    @TableField("minimum_stock")
    @Min(value = 0, message = "鏈€浣庡簱瀛橀槇鍊间笉鑳戒负璐熸暟")
    private Integer minimumStock;

    /**
     * 瀛樻斁浣嶇疆
     */
    @TableField("location")
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    /**
     * 閰嶄欢鐘舵€侊細0-姝ｅ父锟?-鍋滅敤锟?-缂鸿揣
     */
    @TableField("status")
    @Min(value = 0, message = "Status must be >= 0")
    @Max(value = 2, message = "Status must be <= 2")
    private Integer status;

    /**
     * 澶囨敞淇℃伅
     */
    @TableField("remark")
    @Size(max = 500, message = "Remark must not exceed 500 characters")
    private String remark;

    /**
     * 创建鏃堕棿
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新鏃堕棿
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setId(Long id) { this.id = id; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getStockQuantity() { return stockQuantity; }
    public Integer getMinimumStock() { return minimumStock; }
    public Integer getStatus() { return status; }
}
