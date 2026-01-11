package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.EquipmentAccessoryReplacementRecord;
import java.util.List;

public interface EquipmentAccessoryReplacementRecordService extends IService<EquipmentAccessoryReplacementRecord> {
    List<EquipmentAccessoryReplacementRecord> listByEquipmentId(Long equipmentId);
    EquipmentAccessoryReplacementRecord addRecord(Long equipmentId, EquipmentAccessoryReplacementRecord record);
    Page<EquipmentAccessoryReplacementRecord> pageList(Long equipmentId, Integer pageNum, Integer pageSize, String startTime, String endTime, String accessoryName, String accessoryCode, Long operatorUserId);
}
