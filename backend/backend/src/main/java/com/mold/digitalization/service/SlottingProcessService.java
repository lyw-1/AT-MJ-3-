package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.SlottingLayerRecord;
import com.mold.digitalization.entity.SlottingProcess;

import java.time.LocalDateTime;
import java.util.List;

public interface SlottingProcessService extends IService<SlottingProcess> {
    SlottingProcess start(String moldCode, Long equipmentId, String equipmentName, LocalDateTime expectedFinishTime);
    boolean addLayers(Long processId, List<SlottingLayerRecord> layers);
    boolean completeAxis(Long processId, String axis);
    boolean complete(Long processId);
    SlottingProcess getDetail(Long processId);
}
