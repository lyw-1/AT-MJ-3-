package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.DeepHoleLayerRecord;
import com.mold.digitalization.entity.DeepHolePreDrillBatch;
import com.mold.digitalization.entity.DeepHoleProcess;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.mapper.DeepHoleLayerRecordMapper;
import com.mold.digitalization.mapper.DeepHolePreDrillBatchMapper;
import com.mold.digitalization.mapper.DeepHoleProcessMapper;
import com.mold.digitalization.service.DeepHoleProcessService;
import com.mold.digitalization.service.MoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeepHoleProcessServiceImpl extends ServiceImpl<DeepHoleProcessMapper, DeepHoleProcess> implements DeepHoleProcessService {

    @Autowired
    private DeepHoleLayerRecordMapper layerMapper;
    @Autowired
    private DeepHolePreDrillBatchMapper batchMapper;
    @Autowired
    private MoldService moldService;

    @Override
    public DeepHoleProcess startProcess(String moldCode, Long equipmentId, String equipmentName, Integer craftType, Integer plannedLayerCount, LocalDateTime expectedFinishTime) {
        Mold mold = moldService.getMoldByCode(moldCode);
        DeepHoleProcess p = new DeepHoleProcess();
        p.setMoldCode(moldCode);
        p.setMoldId(mold != null ? mold.getId() : null);
        p.setEquipmentId(equipmentId);
        p.setEquipmentName(equipmentName);
        p.setCraftType(craftType);
        p.setStatus(1);
        p.setPlannedLayerCount(plannedLayerCount);
        p.setExpectedFinishTime(expectedFinishTime);
        p.setCreateTime(LocalDateTime.now());
        p.setUpdateTime(LocalDateTime.now());
        save(p);
        return p;
    }

    @Override
    public boolean addLayerRecords(Long processId, List<DeepHoleLayerRecord> layers) {
        int totalSec = 0;
        for (DeepHoleLayerRecord r : layers) {
            r.setProcessId(processId);
            Integer s = r.getSingleHoleTimeSec() != null ? r.getSingleHoleTimeSec() : 0;
            Integer c = r.getThroughHoleCount() != null ? r.getThroughHoleCount() : 0;
            int layerSec = s * c;
            r.setLayerTimeSec(layerSec);
            layerMapper.insert(r);
            totalSec += layerSec;
        }
        DeepHoleProcess p = getById(processId);
        if (p != null) {
            if (p.getExpectedFinishTime() == null) {
                p.setExpectedFinishTime(LocalDateTime.now().plusSeconds(totalSec));
            } else {
                p.setExpectedFinishTime(p.getExpectedFinishTime().plusSeconds(totalSec));
            }
            p.setUpdateTime(LocalDateTime.now());
            updateById(p);
        }
        return true;
    }

    @Override
    public boolean addPreDrillBatch(Long processId, DeepHolePreDrillBatch batch) {
        batch.setProcessId(processId);
        Integer s = batch.getSingleHoleTimeSec() != null ? batch.getSingleHoleTimeSec() : 0;
        Integer c = batch.getHoleCount() != null ? batch.getHoleCount() : 0;
        int sec = s * c;
        batch.setBatchTimeSec(sec);
        batchMapper.insert(batch);
        DeepHoleProcess p = getById(processId);
        if (p != null) {
            if (p.getExpectedFinishTime() == null) {
                p.setExpectedFinishTime(LocalDateTime.now().plusSeconds(sec));
            } else {
                p.setExpectedFinishTime(p.getExpectedFinishTime().plusSeconds(sec));
            }
            p.setUpdateTime(LocalDateTime.now());
            updateById(p);
        }
        return true;
    }

    @Override
    public DeepHoleProcess getDetail(Long processId) {
        DeepHoleProcess p = getById(processId);
        return p;
    }

    @Override
    public boolean completeProcess(Long processId) {
        DeepHoleProcess p = getById(processId);
        if (p == null) return false;
        p.setStatus(2);
        p.setUpdateTime(LocalDateTime.now());
        return updateById(p);
    }
}
