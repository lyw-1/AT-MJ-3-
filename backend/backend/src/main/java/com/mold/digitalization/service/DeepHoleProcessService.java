package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.DeepHoleLayerRecord;
import com.mold.digitalization.entity.DeepHolePreDrillBatch;
import com.mold.digitalization.entity.DeepHoleProcess;

import java.time.LocalDateTime;
import java.util.List;

public interface DeepHoleProcessService extends IService<DeepHoleProcess> {
    DeepHoleProcess startProcess(String moldCode, Long equipmentId, String equipmentName, Integer craftType, Integer plannedLayerCount, LocalDateTime expectedFinishTime);
    boolean addLayerRecords(Long processId, List<DeepHoleLayerRecord> layers);
    boolean addPreDrillBatch(Long processId, DeepHolePreDrillBatch batch);
    DeepHoleProcess getDetail(Long processId);
    boolean completeProcess(Long processId);
}
