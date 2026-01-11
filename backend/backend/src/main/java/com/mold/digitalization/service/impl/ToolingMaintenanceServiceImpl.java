package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.ToolingMaintenance;
import com.mold.digitalization.mapper.ToolingMaintenanceMapper;
import com.mold.digitalization.service.ToolingMaintenanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 妯″叿缁翠慨淇濆吇记录Service实现绫? * 实现妯″叿缁翠慨淇濆吇鐩稿叧鐨勪笟鍔￠€昏緫
 */
@Service
public class ToolingMaintenanceServiceImpl extends ServiceImpl<ToolingMaintenanceMapper, ToolingMaintenance> implements ToolingMaintenanceService {

    @Autowired
    private ToolingMaintenanceMapper toolingMaintenanceMapper;

    @Override
    public Long createMaintenance(ToolingMaintenance maintenance) {
        // 设置创建鏃堕棿鍜屾洿鏂版椂闂?        maintenance.setCreateTime(LocalDateTime.now());
        maintenance.setUpdateTime(LocalDateTime.now());
        // 濡傛灉娌℃湁设置鐘舵€侊紝榛樿设置涓哄緟澶勭悊
        if (maintenance.getStatus() == null) {
            maintenance.setStatus(0);
        }
        // 淇濆瓨记录
        save(maintenance);
        return maintenance.getId();
    }

    @Override
    public List<ToolingMaintenance> getByMoldId(Long moldId) {
        return toolingMaintenanceMapper.selectByMoldId(moldId);
    }

    @Override
    public List<ToolingMaintenance> getByStatus(Integer status) {
        return toolingMaintenanceMapper.selectByStatus(status);
    }

    @Override
    public List<ToolingMaintenance> getByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return toolingMaintenanceMapper.selectByDateRange(params);
    }

    @Override
    public boolean updateMaintenanceStatus(Long id, Integer status) {
        ToolingMaintenance maintenance = getById(id);
        if (maintenance != null) {
            maintenance.setStatus(status);
            maintenance.setUpdateTime(LocalDateTime.now());
            // 濡傛灉鐘舵€佹洿鏂颁负宸插畬鎴愶紝设置缁撴潫鏃堕棿
            if (status == 2 && maintenance.getEndTime() == null) {
                maintenance.setEndTime(LocalDateTime.now());
            }
            return updateById(maintenance);
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getMaintenanceTypeStats() {
        return toolingMaintenanceMapper.countByMaintenanceType();
    }

    @Override
    public Map<String, Object> getMaintenanceCostStats(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<ToolingMaintenance> records = toolingMaintenanceMapper.selectByDateRange(params);
        
        Double totalCost = 0.0;
        Integer recordCount = records.size();
        
        for (ToolingMaintenance record : records) {
            if (record.getCost() != null) {
                totalCost += record.getCost();
            }
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCost", totalCost);
        stats.put("recordCount", recordCount);
        stats.put("avgCost", recordCount > 0 ? totalCost / recordCount : 0);
        
        return stats;
    }

    @Override
    public boolean updateMaintenanceResult(Long id, String result, String nextMaintenanceTime) {
        ToolingMaintenance maintenance = getById(id);
        if (maintenance != null) {
            maintenance.setMaintenanceResult(result);
            maintenance.setNextMaintenanceTime(nextMaintenanceTime);
            maintenance.setUpdateTime(LocalDateTime.now());
            return updateById(maintenance);
        }
        return false;
    }
}
