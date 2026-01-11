package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.entity.WireCutProcess;
import com.mold.digitalization.mapper.WireCutProcessMapper;
import com.mold.digitalization.service.MoldService;
import com.mold.digitalization.service.WireCutProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WireCutProcessServiceImpl extends ServiceImpl<WireCutProcessMapper, WireCutProcess> implements WireCutProcessService {

    @Autowired
    private MoldService moldService;

    @Override
    public WireCutProcess start(String moldCode, Long equipmentId, String equipmentName, LocalDateTime expectedFinishTime) {
        Mold mold = moldService.getMoldByCode(moldCode);
        WireCutProcess p = new WireCutProcess();
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
    public boolean complete(Long id) {
        WireCutProcess p = getById(id);
        if (p == null) return false;
        p.setStatus(2);
        p.setUpdateTime(LocalDateTime.now());
        return updateById(p);
    }
}
