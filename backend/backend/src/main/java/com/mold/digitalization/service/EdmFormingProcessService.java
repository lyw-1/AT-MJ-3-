package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.EdmFormingProcess;

import java.time.LocalDateTime;

public interface EdmFormingProcessService extends IService<EdmFormingProcess> {
    EdmFormingProcess start(String moldCode, Long equipmentId, String equipmentName, String stage, LocalDateTime expectedFinishTime);
    boolean complete(Long id);
}