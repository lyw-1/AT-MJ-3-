package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.TuningRecord;

import java.util.List;

public interface TuningRecordService extends IService<TuningRecord> {
    List<TuningRecord> getByMoldCode(String moldCode);
    List<TuningRecord> getByMoldId(Long moldId);
    boolean create(TuningRecord record);
    boolean updateRecord(TuningRecord record);
    boolean deleteRecord(Long id);
}
