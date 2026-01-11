package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.OutsourcedHeatTreatment;

import java.time.LocalDateTime;

public interface OutsourcedHeatTreatmentService extends IService<OutsourcedHeatTreatment> {
    OutsourcedHeatTreatment create(String moldCode, Integer type, String supplier, String targetHrc, LocalDateTime expectedFinishTime);
    boolean ship(Long id);
    boolean back(Long id);
}
