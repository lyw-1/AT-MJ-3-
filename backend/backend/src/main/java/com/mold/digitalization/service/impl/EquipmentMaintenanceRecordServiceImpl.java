package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.EquipmentMaintenanceCycle;
import com.mold.digitalization.entity.EquipmentMaintenanceRecord;
import com.mold.digitalization.mapper.EquipmentMaintenanceCycleMapper;
import com.mold.digitalization.mapper.EquipmentMaintenanceRecordMapper;
import com.mold.digitalization.service.EquipmentMaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentMaintenanceRecordServiceImpl extends ServiceImpl<EquipmentMaintenanceRecordMapper, EquipmentMaintenanceRecord> implements EquipmentMaintenanceRecordService {

    @Autowired
    private EquipmentMaintenanceCycleMapper cycleMapper;

    @Override
    public List<EquipmentMaintenanceRecord> listByEquipmentId(Long equipmentId) {
        QueryWrapper<EquipmentMaintenanceRecord> qw = new QueryWrapper<>();
        qw.eq("equipment_id", equipmentId).orderByDesc("maintenance_time");
        return list(qw);
    }

    @Override
    public EquipmentMaintenanceRecord addRecord(Long equipmentId, EquipmentMaintenanceRecord record) {
        record.setEquipmentId(equipmentId);
        if (record.getMaintenanceTime() == null) {
            record.setMaintenanceTime(LocalDateTime.now());
        }
        save(record);
        return record;
    }

    @Override
    public Integer getCycleDays(Long equipmentId) {
        EquipmentMaintenanceCycle c = cycleMapper.selectById(equipmentId);
        return c == null ? null : c.getCycleDays();
    }

    @Override
    public void setCycleDays(Long equipmentId, Integer days) {
        if (days == null || days <= 0) {
            cycleMapper.deleteById(equipmentId);
        } else {
            EquipmentMaintenanceCycle c = cycleMapper.selectById(equipmentId);
            if (c == null) {
                c = new EquipmentMaintenanceCycle();
                c.setEquipmentId(equipmentId);
            }
            c.setCycleDays(days);
            if (cycleMapper.selectById(equipmentId) == null) {
                cycleMapper.insert(c);
            } else {
                cycleMapper.updateById(c);
            }
        }
    }

    @Override
    public Map<Long, Integer> cyclesSnapshot() {
        Map<Long, Integer> map = new HashMap<>();
        List<EquipmentMaintenanceCycle> list = cycleMapper.selectList(new QueryWrapper<EquipmentMaintenanceCycle>());
        for (EquipmentMaintenanceCycle c : list) {
            map.put(c.getEquipmentId(), c.getCycleDays());
        }
        return map;
    }

    @Override
    public Page<EquipmentMaintenanceRecord> pageList(Long equipmentId, Integer pageNum, Integer pageSize, String startTime, String endTime, Long operatorUserId, String keyword) {
        Page<EquipmentMaintenanceRecord> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        QueryWrapper<EquipmentMaintenanceRecord> qw = new QueryWrapper<>();
        if (equipmentId != null) {
            qw.eq("equipment_id", equipmentId);
        }
        if (operatorUserId != null) {
            qw.eq("operator_user_id", operatorUserId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            qw.like("content", keyword);
        }
        if (startTime != null && !startTime.isEmpty()) {
            qw.ge("maintenance_time", startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            qw.le("maintenance_time", endTime);
        }
        qw.orderByDesc("maintenance_time");
        return page(page, qw);
    }
}

