package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统参数实体类
 * 对应数据库表：system_parameter
 */
@Data
@TableName("system_parameter")
public class SystemParameter {

    /**
     * 参数ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 参数键名，唯一
     */
    private String paramKey;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 参数描述
     */
    private String description;

    /**
     * 参数类型：0-字符串，1-数字，2-布尔值，3-JSON
     */
    private Integer paramType;

    /**
     * 是否可编辑：0-不可编辑，1-可编辑
     */
    private Integer editable;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 手动添加setter方法以确保编译通过
     * @param id 参数ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}