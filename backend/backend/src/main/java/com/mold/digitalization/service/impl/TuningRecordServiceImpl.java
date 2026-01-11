package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.TuningRecord;
import com.mold.digitalization.mapper.TuningRecordMapper;
import com.mold.digitalization.service.TuningRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class TuningRecordServiceImpl extends ServiceImpl<TuningRecordMapper, TuningRecord> implements TuningRecordService {

    @Autowired
    private TuningRecordMapper mapper;

    @Override
    public List<TuningRecord> getByMoldCode(String moldCode) {
        return mapper.selectByMoldCode(moldCode);
    }

    @Override
    public List<TuningRecord> getByMoldId(Long moldId) {
        return mapper.selectByMoldId(moldId);
    }

    @Override
    public boolean create(TuningRecord record) {
        LocalDateTime now = LocalDateTime.now();
        if (record.getCreateTime() == null) {
            record.setCreateTime(now);
        }
        record.setUpdateTime(now);
        return save(record);
    }

    @Override
    public boolean updateRecord(TuningRecord record) {
        record.setUpdateTime(LocalDateTime.now());
        return updateById(record);
    }

    @Override
    public boolean deleteRecord(Long id) {
        return removeById(id);
    }
}
