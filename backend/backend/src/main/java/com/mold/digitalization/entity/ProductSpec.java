package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product_spec")
public class ProductSpec {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("spec_code")
    private String specCode;
    @TableField("spec_name")
    private String specName;
    @TableField("description")
    private String description;
}
