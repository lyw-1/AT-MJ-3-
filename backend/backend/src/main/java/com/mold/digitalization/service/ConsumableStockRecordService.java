package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.ConsumableStockRecord;

public interface ConsumableStockRecordService extends IService<ConsumableStockRecord> {
    boolean createRecord(ConsumableStockRecord record);
}
