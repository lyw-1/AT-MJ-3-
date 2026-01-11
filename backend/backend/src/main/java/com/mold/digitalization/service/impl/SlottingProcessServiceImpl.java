package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.entity.SlottingLayerRecord;
import com.mold.digitalization.entity.SlottingProcess;
import com.mold.digitalization.mapper.SlottingLayerRecordMapper;
import com.mold.digitalization.mapper.SlottingProcessMapper;
import com.mold.digitalization.service.MoldService;
import com.mold.digitalization.service.SlottingProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SlottingProcessServiceImpl extends ServiceImpl<SlottingProcessMapper, SlottingProcess> implements SlottingProcessService {

    @Autowired
    private SlottingLayerRecordMapper layerMapper;
    @Autowired
    private MoldService moldService;

    @Override
    public SlottingProcess start(String moldCode, Long equipmentId, String equipmentName, LocalDateTime expectedFinishTime) {
        Mold mold = moldService.getMoldByCode(moldCode);
        SlottingProcess p = new SlottingProcess();
        p.setMoldCode(moldCode);
        p.setMoldId(mold != null ? mold.getId() : null);
        p.setEquipmentId(equipmentId);
        p.setEquipmentName(equipmentName);
        p.setStatus(1);
        p.setExpectedFinishTime(expectedFinishTime);
        p.setCreateTime(LocalDateTime.now());
        p.setUpdateTime(LocalDateTime.now());
        save(p);
        return p;
    }

    @Override
    public boolean addLayers(Long processId, List<SlottingLayerRecord> layers) {
        for (SlottingLayerRecord r : layers) {
            r.setProcessId(processId);
            layerMapper.insert(r);
        }
        SlottingProcess p = getById(processId);
        if (p != null) {
            p.setUpdateTime(LocalDateTime.now());
            updateById(p);
        }
        return true;
    }

    @Override
    public boolean completeAxis(Long processId, String axis) {
        SlottingProcess p = getById(processId);
        if (p == null) return false;
        p.setUpdateTime(LocalDateTime.now());
        return updateById(p);
    }

    @Override
    public boolean complete(Long processId) {
        SlottingProcess p = getById(processId);
        if (p == null) return false;
        p.setStatus(2);
        p.setUpdateTime(LocalDateTime.now());
        return updateById(p);
    }

    @Override
    public SlottingProcess getDetail(Long processId) {
        return getById(processId);
    }
}
