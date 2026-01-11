package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.WireCutProcess;

import java.time.LocalDateTime;

public interface WireCutProcessService extends IService<WireCutProcess> {
    WireCutProcess start(String moldCode, Long equipmentId, String equipmentName, LocalDateTime expectedFinishTime);
    boolean complete(Long id);
}
