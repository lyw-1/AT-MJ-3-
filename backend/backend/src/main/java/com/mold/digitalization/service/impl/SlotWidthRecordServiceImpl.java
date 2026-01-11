package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.SlotWidthRecordMapper;
import com.mold.digitalization.entity.SlotWidthRecord;
import com.mold.digitalization.service.SlotWidthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 妲藉记录服务实现绫?
 * 实现妲藉娴嬮噺记录鐩稿叧鐨勪笟鍔￠€昏緫
 */
@Service
public class SlotWidthRecordServiceImpl extends ServiceImpl<SlotWidthRecordMapper, SlotWidthRecord> implements SlotWidthRecordService {
    
    @Autowired
    private SlotWidthRecordMapper slotWidthRecordMapper;
    
    @Override
    public boolean createRecord(SlotWidthRecord record) {
        // 设置娴嬮噺鏃堕棿鍜岄粯璁ょ姸鎬?
        if (record.getMeasurementTime() == null) {
            record.setMeasurementTime(LocalDateTime.now());
        }
        record.setStatus(0); // 榛樿鐘舵€侊細寰呭鐞?
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
    // Determine tolerance; default to 0.05 if not provided
    Double tolerance = record.getTolerance() != null ? record.getTolerance() : 0.05;
    Double diff = Math.abs(record.getActualWidth() - record.getDesignWidth());
        record.setIsQualified(diff <= tolerance ? 1 : 0);
        
        return save(record);
    }
    
    @Override
    public List<SlotWidthRecord> getByMoldId(Long moldId) {
        QueryWrapper<SlotWidthRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mold_id", moldId);
        queryWrapper.orderByDesc("measurement_time");
        return slotWidthRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<SlotWidthRecord> getUnqualifiedRecords() {
    QueryWrapper<SlotWidthRecord> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("is_qualified", 0); // 0: unqualified
    queryWrapper.orderByDesc("measurement_time");
        return slotWidthRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean updateStatus(Long id, Integer status) {
        SlotWidthRecord record = new SlotWidthRecord();
        record.setId(id);
        record.setStatus(status);
        record.setUpdateTime(LocalDateTime.now());
        return updateById(record);
    }
}
