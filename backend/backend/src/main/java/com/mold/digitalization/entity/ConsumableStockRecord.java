package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("consumable_stock_record")
public class ConsumableStockRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("item_id")
    private Long itemId;

    @TableField("item_name")
    private String itemName;

    @TableField("item_category")
    private String itemCategory;

    @TableField("item_spec")
    private String itemSpec;

    @TableField("record_type")
    private String recordType; // IN / OUT

    @TableField("qty")
    private Integer qty;

    @TableField("operator")
    private String operator;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setRecordType(String recordType) { this.recordType = recordType; }
    public void setQty(Integer qty) { this.qty = qty; }
    public void setOperator(String operator) { this.operator = operator; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setItemCategory(String itemCategory) { this.itemCategory = itemCategory; }
    public void setItemSpec(String itemSpec) { this.itemSpec = itemSpec; }
}
