package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("equipment_maintenance_cycle")
public class EquipmentMaintenanceCycle {
    @TableId
    private Long equipmentId;
    private Integer cycleDays;
}

