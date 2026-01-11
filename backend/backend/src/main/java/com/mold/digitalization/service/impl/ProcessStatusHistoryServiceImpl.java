package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.ProcessStatusHistoryMapper;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.service.ProcessStatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 娴佺▼鐘舵€佸巻鍙叉湇鍔″疄鐜扮被
 * 实现娴佺▼鐘舵€佸巻鍙茬浉鍏崇殑涓氬姟服务方法
 */
@Service
public class ProcessStatusHistoryServiceImpl extends ServiceImpl<ProcessStatusHistoryMapper, ProcessStatusHistory> implements ProcessStatusHistoryService {
    
    @Autowired
    private ProcessStatusHistoryMapper processStatusHistoryMapper;
    
    @Override
    public List<ProcessStatusHistory> getStatusHistoriesByProcessId(Long processId) {
        QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId)
                   .orderByDesc("transition_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<ProcessStatusHistory> getStatusHistoriesByStatus(Integer status) {
        QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status)
.orderByDesc("transition_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<ProcessStatusHistory> getStatusHistoriesByOperatorId(Long operatorId) {
        QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operator_id", operatorId)
                   .orderByDesc("transition_time");
        return list(queryWrapper);
    }
    
    @Override
    public ProcessStatusHistory getLatestStatusHistoryByProcessId(Long processId) {
        return processStatusHistoryMapper.selectLatestByProcessId(processId);
    }
    
    @Override
    public List<ProcessStatusHistory> getStatusHistoriesByTimeRange(java.time.LocalDateTime startTime, 
                                                                  java.time.LocalDateTime endTime) {
        QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("transition_time", startTime, endTime)
                   .orderByDesc("transition_time");
        return list(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean createStatusHistory(ProcessStatusHistory statusHistory) {
        return save(statusHistory);
    }
    
    @Override
    @Transactional
    public boolean updateStatusHistory(ProcessStatusHistory statusHistory) {
        return updateById(statusHistory);
    }
    
    @Override
    @Transactional
    public boolean deleteStatusHistory(Long historyId) {
        return removeById(historyId);
    }
    
    @Override
    public List<ProcessStatusHistory> getAllStatusHistories() {
        QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("transition_time");
        return list(queryWrapper);
    }
    
    @Override
    public int countStatusChangesByProcessId(Long processId) {
        QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        return Math.toIntExact(count(queryWrapper));
    }
    
    @Override
    @Transactional
    public boolean deleteStatusHistoriesByProcessId(Long processId) {
        QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        return remove(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean recordStatusChange(Long processId, Integer status, Long operatorId, String remark) {
        ProcessStatusHistory statusHistory = new ProcessStatusHistory();
        statusHistory.setProcessId(processId);
        statusHistory.setStatus(status);
        statusHistory.setOperatorId(operatorId);
        statusHistory.setOperationRemark(remark);
        statusHistory.setOperationTime(LocalDateTime.now());
        // 补充设置 transition_time，避免基于该列的时间范围查询/排序出现 Null 值
        statusHistory.setTransitionTime(LocalDateTime.now());
        
        return save(statusHistory);
    }
}
