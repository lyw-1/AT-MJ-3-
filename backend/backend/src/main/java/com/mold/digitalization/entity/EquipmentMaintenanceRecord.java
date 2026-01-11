package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("equipment_maintenance_record")
public class EquipmentMaintenanceRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long equipmentId;
    private LocalDateTime maintenanceTime;
    private String content;
    private Long operatorUserId;
    private String remark;
}
