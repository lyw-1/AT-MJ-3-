package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.ChangeRecord;

import java.util.List;

public interface ChangeRecordService extends IService<ChangeRecord> {
    List<ChangeRecord> getByMoldCode(String moldCode);
    List<ChangeRecord> getByMoldId(Long moldId);
    boolean create(ChangeRecord record);
    boolean updateRecord(ChangeRecord record);
    boolean deleteRecord(Long id);
}