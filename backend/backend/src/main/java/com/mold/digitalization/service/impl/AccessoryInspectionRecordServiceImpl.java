package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.AccessoryInspectionRecord;
import com.mold.digitalization.mapper.AccessoryInspectionRecordMapper;
import com.mold.digitalization.service.AccessoryInspectionRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢璐ㄦ记录Service实现锟? * 实现閰嶄欢璐ㄦ鐩稿叧鐨勪笟鍔￠€昏緫
 */
@Service
public class AccessoryInspectionRecordServiceImpl extends ServiceImpl<AccessoryInspectionRecordMapper, AccessoryInspectionRecord> implements AccessoryInspectionRecordService {

    @Autowired
    private AccessoryInspectionRecordMapper accessoryInspectionRecordMapper;

    @Override
    public Long createInspectionRecord(AccessoryInspectionRecord record) {
        // 设置创建鏃堕棿鍜屾洿鏂版椂闂?
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        // 濡傛灉娌℃湁设置璐ㄦ日期锛岄粯璁よ缃负褰撳墠鏃堕棿
        if (record.getInspectionDate() == null) {
            record.setInspectionDate(LocalDateTime.now());
        }
        // 淇濆瓨记录
        save(record);
        return record.getId();
    }

    @Override
    public List<AccessoryInspectionRecord> getByAccessoryId(Long accessoryId) {
        return accessoryInspectionRecordMapper.selectByAccessoryId(accessoryId);
    }

    @Override
    public List<AccessoryInspectionRecord> getByBatchNumber(String batchNumber) {
        return accessoryInspectionRecordMapper.selectByBatchNumber(batchNumber);
    }

    @Override
    public List<AccessoryInspectionRecord> getUnqualifiedRecords() {
        // 查询涓嶅悎鏍肩殑记录锛坕nspectionResult = 2锛?
        return accessoryInspectionRecordMapper.selectByInspectionResult(2);
    }

    @Override
    public List<AccessoryInspectionRecord> getByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return accessoryInspectionRecordMapper.selectByDateRange(params);
    }

    @Override
    public boolean updateInspectionResult(Long id, Integer inspectionResult) {
        AccessoryInspectionRecord record = getById(id);
        if (record != null) {
            record.setInspectionResult(inspectionResult);
            record.setUpdateTime(LocalDateTime.now());
            record.setInspectionDate(LocalDateTime.now()); // 更新璐ㄦ日期涓哄綋鍓嶆椂闂?
            return updateById(record);
        }
        return false;
    }

    @Override
    public Map<String, Object> getInspectionStats(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<AccessoryInspectionRecord> records = accessoryInspectionRecordMapper.selectByDateRange(params);
        
        int totalRecords = records.size();
        int qualifiedCount = 0;
        int unqualifiedCount = 0;
        int pendingCount = 0;
        
        for (AccessoryInspectionRecord record : records) {
            Integer result = record.getInspectionResult();
            if (result != null) {
                switch (result) {
                    case 1: // 鍚堟牸
                        qualifiedCount++;
                        break;
                    case 2: // 涓嶅悎鏍?
                        unqualifiedCount++;
                        break;
                    case 0: // 寰呮
                        pendingCount++;
                        break;
                }
            }
        }
        
        double passRate = totalRecords > 0 ? (double) qualifiedCount / totalRecords * 100 : 0;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", totalRecords);
        stats.put("qualifiedCount", qualifiedCount);
        stats.put("unqualifiedCount", unqualifiedCount);
        stats.put("pendingCount", pendingCount);
        stats.put("passRate", Math.round(passRate * 100) / 100.0); // 淇濈暀涓や綅灏忔暟
        
        return stats;
    }
}
