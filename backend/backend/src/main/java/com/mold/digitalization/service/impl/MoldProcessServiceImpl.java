package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.enums.ProcessStatusEnum;
import com.mold.digitalization.service.MoldProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 妯″叿鍔犲伐娴佺▼服务实现绫?
 * 实现妯″叿鍔犲伐娴佺▼鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
@Service
public class MoldProcessServiceImpl extends ServiceImpl<MoldProcessMapper, MoldProcess> implements MoldProcessService {
    
    @Autowired
    private MoldProcessMapper moldProcessMapper;
    
    @Override
    public MoldProcess getMoldProcessByCode(String processCode) {
        return moldProcessMapper.selectByProcessCode(processCode);
    }
    
    @Override
    public List<MoldProcess> getMoldProcessesByMoldId(Long moldId) {
        return moldProcessMapper.selectByMoldId(moldId);
    }
    
    @Override
    public List<MoldProcess> getMoldProcessesByStatus(Integer status) {
        QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("current_status", status);
        return list(queryWrapper);
    }
    
    @Override
    public List<MoldProcess> getMoldProcessesByPriority(Integer priority) {
        QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("priority", priority);
        return list(queryWrapper);
    }
    
    @Override
    public List<MoldProcess> getMoldProcessesByEquipmentId(Long equipmentId) {
        return moldProcessMapper.selectByEquipmentId(equipmentId);
    }
    
    @Override
    public List<MoldProcess> getMoldProcessesByTimeRange(Date startTime, Date endTime) {
        QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("create_time", startTime, endTime);
        return list(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean createMoldProcess(MoldProcess process) {
        try {
            if (process.getEquipmentId() == null) {
                process.setEquipmentId(0L);
            }
            int rows = moldProcessMapper.insertDev(process);
            return rows > 0;
        } catch (Exception e) {
            return save(process);
        }
    }
    
    @Override
    @Transactional
    public boolean updateMoldProcess(MoldProcess process) {
        return updateById(process);
    }
    
    @Override
    @Transactional
    public boolean deleteMoldProcess(Long processId) {
        return removeById(processId);
    }
    
    @Override
    public List<MoldProcess> getAllMoldProcesses() {
        return list();
    }
    
    @Override
    @Transactional
    public boolean updateMoldProcessStatus(Long processId, Integer status) {
        MoldProcess process = new MoldProcess();
        process.setId(processId);
        process.setCurrentStatus(status);
        return updateById(process);
    }
    
    @Override
    @Transactional
    public boolean updateMoldProcessProgress(Long processId, Integer progress) {
        MoldProcess process = new MoldProcess();
        process.setId(processId);
        process.setCurrentProgress(progress);
        return updateById(process);
    }
    
    @Override
    @Transactional
    public boolean startMoldProcess(Long processId) {
        MoldProcess process = getById(processId);
        if (process == null) {
            return false;
        }
        
        process.setCurrentStatus(ProcessStatusEnum.PREPARING.getCode());
        process.setActualStartTime(java.time.LocalDateTime.now());
        return updateById(process);
    }
    
    @Override
    @Transactional
    public boolean completeMoldProcess(Long processId) {
        MoldProcess process = getById(processId);
        if (process == null) {
            return false;
        }
        
        process.setCurrentStatus(ProcessStatusEnum.COMPLETED.getCode());
        process.setCurrentProgress(100);
        process.setActualEndTime(java.time.LocalDateTime.now());
        return updateById(process);
    }
    
    @Override
    @Transactional
    public boolean pauseMoldProcess(Long processId) {
        MoldProcess process = getById(processId);
        if (process == null) {
            return false;
        }
        
        process.setCurrentStatus(ProcessStatusEnum.PAUSED.getCode());
        return updateById(process);
    }
    
    @Override
    @Transactional
    public boolean resumeMoldProcess(Long processId) {
        MoldProcess process = getById(processId);
        if (process == null) {
            return false;
        }
        
        process.setCurrentStatus(ProcessStatusEnum.PROCESSING.getCode());
        return updateById(process);
    }
    
    @Override
    public List<MoldProcess> getMoldProcessesByProductionTaskId(Long productionTaskId) {
        QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("production_task_id", productionTaskId);
        return list(queryWrapper);
    }
    
    @Override
    public List<MoldProcess> getMoldProcessesByResponsibleUserId(Long responsibleUserId) {
        QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("responsible_user_id", responsibleUserId);
        return list(queryWrapper);
    }
    
    @Override
    public List<MoldProcess> getMoldProcessesByOperatorId(Long operatorId) {
        QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_id", operatorId);
        return list(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean cancelMoldProcess(Long processId) {
        MoldProcess process = getById(processId);
        if (process == null) {
            return false;
        }
        
        process.setCurrentStatus(ProcessStatusEnum.CANCELLED.getCode());
        return updateById(process);
    }
    
    @Override
    public MoldProcessService.MoldProcessStatistics getMoldProcessStatistics() {
        MoldProcessService.MoldProcessStatistics statistics = new MoldProcessService.MoldProcessStatistics();
        
        // 创建查询鍖呰鍣?
        QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
        
        // 缁熻鎬绘暟
        statistics.setTotalProcesses((long) count());
        
        // 缁熻宸插畬鎴愭祦绋嬫暟閲?
        queryWrapper.clear();
        queryWrapper.eq("current_status", ProcessStatusEnum.COMPLETED.getCode());
        statistics.setCompletedProcesses((long) count(queryWrapper));
        
        // 缁熻杩涜涓祦绋嬫暟閲忥紙鍖呮嫭准备涓€佸姞宸ヤ腑銆佹殏鍋滀腑锛?       queryWrapper.clear();
        queryWrapper.in("current_status", ProcessStatusEnum.PREPARING.getCode(), ProcessStatusEnum.PROCESSING.getCode(), ProcessStatusEnum.PAUSED.getCode());
        statistics.setInProgressProcesses((long) count(queryWrapper));
        
        // 缁熻寰呭紑濮嬫祦绋嬫暟閲?       queryWrapper.clear();
        queryWrapper.eq("current_status", ProcessStatusEnum.PENDING.getCode());
        statistics.setPendingProcesses((long) count(queryWrapper));
        
        // 缁熻宸插彇娑堟祦绋嬫暟閲?       queryWrapper.clear();
        queryWrapper.eq("current_status", ProcessStatusEnum.CANCELLED.getCode());
        statistics.setCancelledProcesses((long) count(queryWrapper));
        
        return statistics;
    }
}
