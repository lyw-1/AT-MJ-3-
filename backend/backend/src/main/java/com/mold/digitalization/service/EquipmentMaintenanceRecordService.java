package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.EquipmentMaintenanceRecord;
import java.util.List;
import java.util.Map;

public interface EquipmentMaintenanceRecordService extends IService<EquipmentMaintenanceRecord> {
    List<EquipmentMaintenanceRecord> listByEquipmentId(Long equipmentId);
    EquipmentMaintenanceRecord addRecord(Long equipmentId, EquipmentMaintenanceRecord record);
    Integer getCycleDays(Long equipmentId);
    void setCycleDays(Long equipmentId, Integer days);
    Map<Long, Integer> cyclesSnapshot();
    Page<EquipmentMaintenanceRecord> pageList(Long equipmentId, Integer pageNum, Integer pageSize, String startTime, String endTime, Long operatorUserId, String keyword);
}
