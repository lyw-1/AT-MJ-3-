package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.MoldStorageRecordMapper;
import com.mold.digitalization.entity.MoldStorageRecord;
import com.mold.digitalization.service.MoldStorageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 妯″叿鍏ュ簱记录服务实现绫? * 实现妯″叿鍏ュ簱鐩稿叧鐨勪笟鍔￠€昏緫
 */
@Service
public class MoldStorageRecordServiceImpl extends ServiceImpl<MoldStorageRecordMapper, MoldStorageRecord> implements MoldStorageRecordService {
    
    @Autowired
    private MoldStorageRecordMapper moldStorageRecordMapper;
    
    @Override
    public List<MoldStorageRecord> getByMoldId(Long moldId) {
        QueryWrapper<MoldStorageRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mold_id", moldId);
        queryWrapper.orderByDesc("storage_date");
        return moldStorageRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<MoldStorageRecord> getByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<MoldStorageRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("storage_date", startTime, endTime);
        queryWrapper.orderByDesc("storage_date");
        return moldStorageRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<MoldStorageRecord> getByBatchNumber(String batchNumber) {
        QueryWrapper<MoldStorageRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_number", batchNumber);
        queryWrapper.orderByDesc("storage_date");
        return moldStorageRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<MoldStorageRecord> getByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // 澶嶇敤鐜版湁鐨勬椂闂磋寖鍥存煡璇㈤€昏緫
        return getByTimeRange(startDate, endDate);
    }
    
    @Override
    public boolean createRecord(MoldStorageRecord record) {
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return save(record);
    }
    
    @Override
    public boolean updateInspectionResult(Long id, Integer inspectionResult) {
        MoldStorageRecord record = new MoldStorageRecord();
        record.setId(id);
        record.setInspectionResult(inspectionResult);
        record.setUpdateTime(LocalDateTime.now());
        return updateById(record);
    }

    @Override
    public List<MoldStorageRecord> getByInspectionResult(Integer inspectionResult) {
        QueryWrapper<MoldStorageRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspection_result", inspectionResult);
        queryWrapper.orderByDesc("storage_date");
        return moldStorageRecordMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateRecord(MoldStorageRecord record) {
        record.setUpdateTime(LocalDateTime.now());
        return updateById(record);
    }

    @Override
    public boolean deleteRecord(Long id) {
        return removeById(id);
    }

    @Override
    public Map<String, Object> getStorageStats() {
    Map<String, Object> stats = new HashMap<>();
    // 缁熻鎬诲叆搴撴暟閲?
    long totalCountLong = count();
    Integer totalCount = Math.toIntExact(totalCountLong);
        stats.put("totalCount", totalCount);
        
        // 缁熻鍚勮川妫€结果鏁伴噺
        QueryWrapper<MoldStorageRecord> qualifiedWrapper = new QueryWrapper<>();
        qualifiedWrapper.eq("inspection_result", 1); // 鍋囪1琛ㄧず鍚堟牸
        long qualifiedCountLong = count(qualifiedWrapper);
        Integer qualifiedCount = Math.toIntExact(qualifiedCountLong);
        stats.put("qualifiedCount", qualifiedCount);
        
    QueryWrapper<MoldStorageRecord> unqualifiedWrapper = new QueryWrapper<>();
    unqualifiedWrapper.eq("inspection_result", 2); // 鍋囪2琛ㄧず涓嶅悎鏍?
    long unqualifiedCountLong = count(unqualifiedWrapper);
    Integer unqualifiedCount = Math.toIntExact(unqualifiedCountLong);
        stats.put("unqualifiedCount", unqualifiedCount);
        
    // 璁＄畻鍚堟牸鐜?
    double passRate = totalCount > 0 ? (double)qualifiedCount / totalCount * 100 : 0;
    stats.put("passRate", passRate);
        
        return stats;
    }

    @Override
    public List<MoldStorageRecord> getAllRecords() {
        QueryWrapper<MoldStorageRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("storage_date");
        return moldStorageRecordMapper.selectList(queryWrapper);
    }
}