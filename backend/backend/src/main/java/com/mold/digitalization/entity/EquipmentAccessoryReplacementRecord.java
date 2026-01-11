package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("equipment_accessory_replacement_record")
public class EquipmentAccessoryReplacementRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long equipmentId;
    private Long accessoryId;
    private String accessoryName;
    private String accessoryCode;
    private Integer quantity;
    private LocalDateTime replaceTime;
    private String reason;
    private Long operatorUserId;
    private String remark;
}
