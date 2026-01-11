package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.EquipmentAccessoryReplacementRecord;
import com.mold.digitalization.mapper.EquipmentAccessoryReplacementRecordMapper;
import com.mold.digitalization.service.EquipmentAccessoryReplacementRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipmentAccessoryReplacementRecordServiceImpl extends ServiceImpl<EquipmentAccessoryReplacementRecordMapper, EquipmentAccessoryReplacementRecord> implements EquipmentAccessoryReplacementRecordService {

    @Override
    public List<EquipmentAccessoryReplacementRecord> listByEquipmentId(Long equipmentId) {
        QueryWrapper<EquipmentAccessoryReplacementRecord> qw = new QueryWrapper<>();
        qw.eq("equipment_id", equipmentId).orderByDesc("replace_time");
        return list(qw);
    }

    @Override
    public EquipmentAccessoryReplacementRecord addRecord(Long equipmentId, EquipmentAccessoryReplacementRecord record) {
        record.setEquipmentId(equipmentId);
        if (record.getReplaceTime() == null) record.setReplaceTime(LocalDateTime.now());
        save(record);
        return record;
    }

    @Override
    public Page<EquipmentAccessoryReplacementRecord> pageList(Long equipmentId, Integer pageNum, Integer pageSize, String startTime, String endTime, String accessoryName, String accessoryCode, Long operatorUserId) {
        Page<EquipmentAccessoryReplacementRecord> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        QueryWrapper<EquipmentAccessoryReplacementRecord> qw = new QueryWrapper<>();
        if (equipmentId != null) qw.eq("equipment_id", equipmentId);
        if (operatorUserId != null) qw.eq("operator_user_id", operatorUserId);
        if (accessoryName != null && !accessoryName.isEmpty()) qw.like("accessory_name", accessoryName);
        if (accessoryCode != null && !accessoryCode.isEmpty()) qw.like("accessory_code", accessoryCode);
        if (startTime != null && !startTime.isEmpty()) qw.ge("replace_time", startTime);
        if (endTime != null && !endTime.isEmpty()) qw.le("replace_time", endTime);
        qw.orderByDesc("replace_time");
        return page(page, qw);
    }
}

