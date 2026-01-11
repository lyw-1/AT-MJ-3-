package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.EdmFormingProcess;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.mapper.EdmFormingProcessMapper;
import com.mold.digitalization.service.EdmFormingProcessService;
import com.mold.digitalization.service.MoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EdmFormingProcessServiceImpl extends ServiceImpl<EdmFormingProcessMapper, EdmFormingProcess> implements EdmFormingProcessService {

    @Autowired
    private MoldService moldService;

    @Override
    public EdmFormingProcess start(String moldCode, Long equipmentId, String equipmentName, String stage, LocalDateTime expectedFinishTime) {
        Mold mold = moldService.getMoldByCode(moldCode);
        EdmFormingProcess p = new EdmFormingProcess();
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
        EdmFormingProcess p = getById(id);
        if (p == null) return false;
        p.setStatus(2);
        p.setUpdateTime(LocalDateTime.now());
        return updateById(p);
    }
}