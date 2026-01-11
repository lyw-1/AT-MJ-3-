package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.entity.OutsourcedHeatTreatment;
import com.mold.digitalization.mapper.OutsourcedHeatTreatmentMapper;
import com.mold.digitalization.service.MoldService;
import com.mold.digitalization.service.OutsourcedHeatTreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OutsourcedHeatTreatmentServiceImpl extends ServiceImpl<OutsourcedHeatTreatmentMapper, OutsourcedHeatTreatment> implements OutsourcedHeatTreatmentService {

    @Autowired
    private MoldService moldService;

    @Override
    public OutsourcedHeatTreatment create(String moldCode, Integer type, String supplier, String targetHrc, LocalDateTime expectedFinishTime) {
        Mold mold = moldService.getMoldByCode(moldCode);
        OutsourcedHeatTreatment r = new OutsourcedHeatTreatment();
        r.setMoldCode(moldCode);
        r.setMoldId(mold != null ? mold.getId() : null);
        r.setType(type);
        r.setSupplier(supplier);
        r.setTargetHrc(targetHrc);
        r.setExpectedFinishTime(expectedFinishTime);
        r.setStatus(0);
        r.setCreateTime(LocalDateTime.now());
        r.setUpdateTime(LocalDateTime.now());
        save(r);
        return r;
    }

    @Override
    public boolean ship(Long id) {
        OutsourcedHeatTreatment r = getById(id);
        if (r == null) return false;
        r.setStatus(1);
        r.setShippedTime(LocalDateTime.now());
        r.setUpdateTime(LocalDateTime.now());
        return updateById(r);
    }

    @Override
    public boolean back(Long id) {
        OutsourcedHeatTreatment r = getById(id);
        if (r == null) return false;
        r.setStatus(2);
        r.setReturnedTime(LocalDateTime.now());
        r.setUpdateTime(LocalDateTime.now());
        return updateById(r);
    }
}
