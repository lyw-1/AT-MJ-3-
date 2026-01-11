package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.BareMoldInspection;

import java.time.LocalDateTime;

public interface BareMoldInspectionService extends IService<BareMoldInspection> {
    BareMoldInspection create(String moldCode, Long inspectorId, String inspectorName, LocalDateTime inspectionTime, String remark);
}
