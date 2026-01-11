package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.AccessoryProcessRecordMapper;
import com.mold.digitalization.entity.AccessoryProcessRecord;
import com.mold.digitalization.service.AccessoryProcessRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢鍔犲伐记录服务实现绫?
 * 实现閰嶄欢鍔犲伐鐩稿叧鐨勪笟鍔￠€昏緫
 */
@Service
public class AccessoryProcessRecordServiceImpl extends ServiceImpl<AccessoryProcessRecordMapper, AccessoryProcessRecord> implements AccessoryProcessRecordService {
    
    @Autowired
    private AccessoryProcessRecordMapper accessoryProcessRecordMapper;
    
    @Override
    public boolean createRecord(AccessoryProcessRecord entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return save(entity);
    }
    
    @Override
    public List<AccessoryProcessRecord> getByAccessoryId(Long accessoryId) {
        QueryWrapper<AccessoryProcessRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessory_id", accessoryId);
        queryWrapper.orderByDesc("end_time");
        return accessoryProcessRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AccessoryProcessRecord> getByMoldId(Long moldId) {
        QueryWrapper<AccessoryProcessRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mold_id", moldId);
        queryWrapper.orderByDesc("end_time");
        return accessoryProcessRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<AccessoryProcessRecord> getByTaskId(Long taskId) {
        return accessoryProcessRecordMapper.selectByTaskId(taskId);
    }
    
    @Override
    public List<AccessoryProcessRecord> getByProcessResult(Integer processResult) {
        return accessoryProcessRecordMapper.selectByProcessResult(processResult);
    }
    
    @Override
    public List<AccessoryProcessRecord> getByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return accessoryProcessRecordMapper.selectByDateRange(params);
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        AccessoryProcessRecord entity = new AccessoryProcessRecord();
        entity.setId(id);
        entity.setUpdateTime(LocalDateTime.now());
        // set process result if provided
        if (status != null) {
            entity.setProcessResult(status);
            // if result is 'qualified' and endTime not set, set endTime
            AccessoryProcessRecord existing = getById(id);
            if (status == 1 && existing != null && existing.getEndTime() == null) {
                entity.setEndTime(LocalDateTime.now());
            }
        }
        return updateById(entity);
    }
    
    @Override
    public Map<String, Object> getProcessStats(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<AccessoryProcessRecord> records = accessoryProcessRecordMapper.selectByDateRange(params);
        
        int totalRecords = records.size();
        int qualifiedCount = 0;
        int unqualifiedCount = 0;
        int unfinishedCount = 0;
        
        for (AccessoryProcessRecord rec : records) {
            Integer result = rec.getProcessResult();
            if (result != null) {
                switch (result) {
                    case 1:
                        // qualified
                        qualifiedCount++;
                        break;
                    case 2:
                        // unqualified
                        unqualifiedCount++;
                        break;
                    case 0:
                        // unfinished
                        unfinishedCount++;
                        break;
                }
            }
        }
        
        double passRate = totalRecords > 0 ? (double) qualifiedCount / totalRecords * 100 : 0;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", totalRecords);
        stats.put("qualifiedCount", qualifiedCount);
        stats.put("unqualifiedCount", unqualifiedCount);
        stats.put("unfinishedCount", unfinishedCount);
        stats.put("passRate", Math.round(passRate * 100) / 100.0); // 淇濈暀涓や綅灏忔暟
        
        return stats;
    }
    
    @Override
    public List<Map<String, Object>> getProcessNameStats() {
        return accessoryProcessRecordMapper.countByProcessName();
    }
}
