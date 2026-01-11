package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.InspectionResultMapper;
import com.mold.digitalization.entity.InspectionResult;
// Use the InspectionStatistics defined on the service interface to match the service contract
import com.mold.digitalization.service.InspectionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 璐ㄩ噺妫€楠岀粨鏋滄湇鍔″疄鐜扮被
 * 实现璐ㄩ噺妫€楠岀粨鏋滅浉鍏崇殑涓氬姟服务方法
 */
@Service
public class InspectionResultServiceImpl extends ServiceImpl<InspectionResultMapper, InspectionResult> implements InspectionResultService {
    
    @Autowired
    private InspectionResultMapper inspectionResultMapper;
    
    @Override
    public List<InspectionResult> getInspectionResultsByProcessId(Long processId) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId)
                   .orderByDesc("inspection_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<InspectionResult> getInspectionResultsByInspectionItem(String inspectionItem) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspection_item", inspectionItem)
                   .orderByDesc("inspection_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<InspectionResult> getInspectionResultsByResult(String inspectionResult) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("result", inspectionResult)
                   .orderByDesc("inspection_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<InspectionResult> getInspectionResultsByInspectorId(Long inspectorId) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspector_id", inspectorId)
                   .orderByDesc("inspection_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<InspectionResult> getInspectionResultsByTimeRange(java.time.LocalDateTime startTime, 
                                                                  java.time.LocalDateTime endTime) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("inspection_time", startTime, endTime)
                   .orderByDesc("inspection_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<InspectionResult> getPassedInspectionResults() {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("result", 1) // 1琛ㄧず鍚堟牸
                   .orderByDesc("inspection_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<InspectionResult> getFailedInspectionResults() {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("result", 0) // 0琛ㄧず涓嶅悎鏍?
                   .orderByDesc("inspection_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<InspectionResult> getAllInspectionResults() {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("inspection_time");
        return list(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean createInspectionResult(InspectionResult result) {
        return save(result);
    }
    
    @Override
    @Transactional
    public boolean updateInspectionResult(InspectionResult result) {
        return updateById(result);
    }
    
    @Override
    @Transactional
    public boolean deleteInspectionResult(Long resultId) {
        return removeById(resultId);
    }
    
    @Override
    public int countInspectionResultsByProcessId(Long processId) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        return Math.toIntExact(count(queryWrapper));
    }
    
    @Override
    public int countPassedResultsByProcessId(Long processId) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId)
                   .eq("result", 1);
        return Math.toIntExact(count(queryWrapper));
    }
    
    @Override
    public int countFailedResultsByProcessId(Long processId) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId)
                   .eq("result", 0);
        return Math.toIntExact(count(queryWrapper));
    }
    
    @Override
    public Double calculatePassRateByProcessId(Long processId) {
        int total = countInspectionResultsByProcessId(processId);
        if (total == 0) {
            return 0.0;
        }
        
        int qualified = countPassedResultsByProcessId(processId);
        return (double) qualified / total * 100;
    }
    
    @Override
    public boolean deleteInspectionResultsByProcessId(Long processId) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        return remove(queryWrapper);
    }
    
    @Override
    public InspectionStatistics getInspectionStatistics() {
        // Build statistics using the nested InspectionStatistics class defined by InspectionResultService
        InspectionResultService.InspectionStatistics statistics = new InspectionResultService.InspectionStatistics();

        long total = count();
        statistics.setTotalInspections(total);

        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("result", 1);
        long passed = count(queryWrapper);
        statistics.setPassedInspections(passed);

        queryWrapper.clear();
        queryWrapper.eq("result", 0);
        long failed = count(queryWrapper);
        statistics.setFailedInspections(failed);

        double overallPassRate = total == 0 ? 0.0 : (double) passed / total * 100.0;
        statistics.setOverallPassRate(overallPassRate);

        return statistics;
    }
}
