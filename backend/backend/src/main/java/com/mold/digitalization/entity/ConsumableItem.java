package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("consumable_item")
public class ConsumableItem {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("material_code")
    private String materialCode;

    @TableField("item_name")
    private String itemName;

    @TableField("item_category")
    private String itemCategory;

    @TableField("specification")
    private String specification;

    @TableField("unit")
    private String unit;

    @TableField("current_stock")
    private Integer currentStock;

    @TableField("min_stock")
    private Integer minStock;

    @TableField("max_stock")
    private Integer maxStock;

    @TableField("stock_status")
    private String stockStatus;

    @TableField("unit_price")
    private Double unitPrice;

    @TableField("total_value")
    private Double totalValue;

    @TableField("supplier")
    private String supplier;

    @TableField("location")
    private String location;

    @TableField("remarks")
    private String remarks;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    // 计算totalValue的方法
    public void calculateTotalValue() {
        if (this.currentStock != null && this.unitPrice != null) {
            this.totalValue = this.currentStock * this.unitPrice;
        }
    }

    // 更新库存状态的方法
    public void updateStockStatus() {
        if (this.currentStock == null || this.minStock == null || this.maxStock == null) {
            this.stockStatus = "未知";
            return;
        }

        if (this.currentStock <= this.minStock * 0.5) {
            this.stockStatus = "缺货";
        } else if (this.currentStock <= this.minStock) {
            this.stockStatus = "不足";
        } else if (this.currentStock >= this.maxStock) {
            this.stockStatus = "超量";
        } else {
            this.stockStatus = "充足";
        }
    }
}
