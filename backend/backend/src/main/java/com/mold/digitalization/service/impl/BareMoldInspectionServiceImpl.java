package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.BareMoldInspection;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.mapper.BareMoldInspectionMapper;
import com.mold.digitalization.service.BareMoldInspectionService;
import com.mold.digitalization.service.MoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BareMoldInspectionServiceImpl extends ServiceImpl<BareMoldInspectionMapper, BareMoldInspection> implements BareMoldInspectionService {

    @Autowired
    private MoldService moldService;

    @Override
    public BareMoldInspection create(String moldCode, Long inspectorId, String inspectorName, LocalDateTime inspectionTime, String remark) {
        Mold mold = moldService.getMoldByCode(moldCode);
        BareMoldInspection r = new BareMoldInspection();
        r.setMoldCode(moldCode);
        r.setMoldId(mold != null ? mold.getId() : null);
        r.setInspectorId(inspectorId);
        r.setInspectorName(inspectorName);
        r.setInspectionTime(inspectionTime != null ? inspectionTime : LocalDateTime.now());
        r.setRemark(remark);
        r.setCreateTime(LocalDateTime.now());
        r.setUpdateTime(LocalDateTime.now());
        save(r);
        return r;
    }
}
