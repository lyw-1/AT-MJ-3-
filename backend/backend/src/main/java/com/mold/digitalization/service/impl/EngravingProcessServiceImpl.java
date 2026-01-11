package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.EngravingProcess;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.mapper.EngravingProcessMapper;
import com.mold.digitalization.service.EngravingProcessService;
import com.mold.digitalization.service.MoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EngravingProcessServiceImpl extends ServiceImpl<EngravingProcessMapper, EngravingProcess> implements EngravingProcessService {

    @Autowired
    private MoldService moldService;

    @Override
    public EngravingProcess start(String moldCode, Long equipmentId, String equipmentName, String stage, LocalDateTime expectedFinishTime) {
        Mold mold = moldService.getMoldByCode(moldCode);
        EngravingProcess p = new EngravingProcess();
        p.setMoldCode(moldCode);
        p.setMoldId(mold != null ? mold.getId() : null);
        p.setEquipmentId(equipmentId);
        p.setEquipmentName(equipmentName);
        p.setStage(stage);
        p.setStatus(1);
        p.setExpectedFinishTime(expectedFinishTime);
        p.setCreateTime(LocalDateTime.now());
        p.setUpdateTime(LocalDateTime.now());
        save(p);
        return p;
    }

    @Override
    public boolean complete(Long id) {
        EngravingProcess p = getById(id);
        if (p == null) return false;
        p.setStatus(2);
        p.setUpdateTime(LocalDateTime.now());
        return updateById(p);
    }
}