package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mold_accessory_info")
public class MoldAccessoryInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("seq_code")
    private String seqCode;

    @TableField("accessory_name")
    private String accessoryName;

    @TableField("mold_code")
    private String moldCode;

    @TableField("product_spec")
    private String productSpec;

    @TableField("full_code")
    private String fullCode;

    @TableField("material")
    private String material;

    @TableField("lend_return_status")
    private String lendReturnStatus;

    @TableField("handler")
    private String handler;

    @TableField("production_meters")
    private Double productionMeters;

    @TableField("last_used_date")
    private LocalDateTime lastUsedDate;

    @TableField("location")
    private String location;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setId(Long id) { this.id = id; }
}
