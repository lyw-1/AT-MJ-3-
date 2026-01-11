package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.ChangeRecord;
import com.mold.digitalization.mapper.ChangeRecordMapper;
import com.mold.digitalization.service.ChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeRecordServiceImpl extends ServiceImpl<ChangeRecordMapper, ChangeRecord> implements ChangeRecordService {

    @Autowired
    private ChangeRecordMapper mapper;

    @Override
    public List<ChangeRecord> getByMoldCode(String moldCode) {
        return mapper.selectByMoldCode(moldCode);
    }

    @Override
    public List<ChangeRecord> getByMoldId(Long moldId) {
        return mapper.selectByMoldId(moldId);
    }

    @Override
    public boolean create(ChangeRecord record) {
        return save(record);
    }

    @Override
    public boolean updateRecord(ChangeRecord record) {
        return updateById(record);
    }

    @Override
    public boolean deleteRecord(Long id) {
        return removeById(id);
    }
}