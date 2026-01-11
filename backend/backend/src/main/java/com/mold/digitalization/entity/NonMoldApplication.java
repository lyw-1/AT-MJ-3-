package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("non_mold_application")
public class NonMoldApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("title")
    private String title;
    @TableField("description")
    private String description;
    @TableField("applicant_user_id")
    private Long applicantUserId;
    @TableField("status")
    private Integer status;
}
