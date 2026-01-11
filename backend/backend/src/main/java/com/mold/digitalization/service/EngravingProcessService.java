package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.EngravingProcess;

import java.time.LocalDateTime;

public interface EngravingProcessService extends IService<EngravingProcess> {
    EngravingProcess start(String moldCode, Long equipmentId, String equipmentName, String stage, LocalDateTime expectedFinishTime);
    boolean complete(Long id);
}