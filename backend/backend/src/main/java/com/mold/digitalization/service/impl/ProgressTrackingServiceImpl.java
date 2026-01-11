package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.enums.ProcessStatusEnum;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.mapper.ProcessStatusHistoryMapper;
import com.mold.digitalization.service.ProgressTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 杩涘害璺熻釜服务实现绫?
 * 实现妯″叿鍔犲伐娴佺▼杩涘害璺熻釜鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
@Service
@Transactional
public class ProgressTrackingServiceImpl extends ServiceImpl<MoldProcessMapper, MoldProcess> implements ProgressTrackingService {
    private static final Logger logger = LoggerFactory.getLogger(ProgressTrackingServiceImpl.class);
    
    @Autowired
    private MoldProcessMapper moldProcessMapper;
    
    @Autowired
    private ProcessStatusHistoryMapper processStatusHistoryMapper;
    
    @Override
    public Integer getCurrentProgress(Long processId) {
        try {
            MoldProcess process = moldProcessMapper.selectById(processId);
            if (process != null) {
                return process.getCurrentProgress();
            }
            logger.warn("娴佺▼ID涓簕}鐨勬祦绋嬩笉瀛樺湪", processId);
            return 0;
        } catch (Exception e) {
            logger.error("获取娴佺▼{}鐨勫綋鍓嶈繘搴﹀け璐? {}", processId, e.getMessage(), e);
            return 0;
        }
    }
    
    @Override
    public boolean updateProgress(Long processId, Integer progress, Long operatorId) {
        try {
            MoldProcess process = moldProcessMapper.selectById(processId);
            if (process == null) {
                logger.error("娴佺▼ID涓簕}鐨勬祦绋嬩笉瀛樺湪", processId);
                return false;
            }
            
            // 验证杩涘害鍊?
            if (progress < 0 || progress > 100) {
                logger.error("杩涘害鍊納}涓嶅悎娉曪紝蹇呴』鍦?-100涔嬮棿", progress);
                return false;
            }
            
            Integer oldProgress = process.getCurrentProgress();
            process.setCurrentProgress(progress);
            process.setUpdateTime(LocalDateTime.now());
            
            int result = moldProcessMapper.updateById(process);
            
            if (result > 0) {
                // 记录杩涘害鍙樻洿鍘嗗彶
                recordProgressChange(processId, oldProgress, progress, operatorId, 
                    String.format("杩涘害浠?d%更新鍒?d%", oldProgress, progress));
                logger.info("娴佺▼{}鐨勮繘搴﹀凡浠巤}更新鍒皗}", processId, oldProgress, progress);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            logger.error("更新娴佺▼{}鐨勮繘搴﹀け璐? {}", processId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public List<ProcessStatusHistory> getProgressHistory(Long processId) {
        try {
            QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("process_id", processId)
                       .orderByDesc("create_time");
            return processStatusHistoryMapper.selectList(queryWrapper);
        } catch (Exception e) {
            logger.error("获取娴佺▼{}鐨勮繘搴﹀巻鍙插け璐? {}", processId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public LocalDateTime getEstimatedCompletionTime(Long processId) {
        try {
            MoldProcess process = moldProcessMapper.selectById(processId);
            if (process == null) {
                logger.warn("娴佺▼ID涓簕}鐨勬祦绋嬩笉瀛樺湪", processId);
                return null;
            }
            
            LocalDateTime startTime = process.getActualStartTime();
            LocalDateTime plannedEndTime = process.getPlannedEndTime();
            
            if (startTime == null || plannedEndTime == null) {
                return null;
            }
            
            // 鏍规嵁褰撳墠杩涘害璋冩暣棰勮瀹屾垚鏃堕棿
            Integer currentProgress = process.getCurrentProgress();
            if (currentProgress == null || currentProgress == 0) {
                return plannedEndTime;
            }
            
            Duration totalDuration = Duration.between(startTime, plannedEndTime);
            long totalMinutes = totalDuration.toMinutes();
            
            if (totalMinutes <= 0) {
                return plannedEndTime;
            }
            
            // 璁＄畻鍓╀綑鏃堕棿
            long remainingMinutes = (long) ((100 - currentProgress) * totalMinutes / 100.0);
            
            return LocalDateTime.now().plusMinutes(remainingMinutes);
        } catch (Exception e) {
            logger.error("璁＄畻娴佺▼{}鐨勯璁″畬鎴愭椂闂村け璐? {}", processId, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public Long calculateActualDuration(Long processId) {
        try {
            MoldProcess process = moldProcessMapper.selectById(processId);
            if (process == null) {
                logger.warn("娴佺▼ID涓簕}鐨勬祦绋嬩笉瀛樺湪", processId);
                return 0L;
            }
            
            LocalDateTime startTime = process.getActualStartTime();
            LocalDateTime endTime = process.getActualEndTime();
            
            if (startTime == null) {
                return 0L;
            }
            
            if (endTime == null) {
                // 濡傛灉娴佺▼鏈粨鏉燂紝璁＄畻浠庡紑濮嬪埌鐜板湪鐨勬椂闂?
                endTime = LocalDateTime.now();
            }
            
            Duration duration = Duration.between(startTime, endTime);
            return duration.toMinutes();
        } catch (Exception e) {
            logger.error("璁＄畻娴佺▼{}鐨勫疄闄呰€楁椂失败: {}", processId, e.getMessage(), e);
            return 0L;
        }
    }
    
    @Override
    public ProgressStatistics getProgressStatistics(Long processId) {
        try {
            MoldProcess process = moldProcessMapper.selectById(processId);
            if (process == null) {
            logger.warn("娴佺▼ID涓簕}鐨勬祦绋嬩笉瀛樺湪", processId);
                return null;
            }
            
            ProgressStatistics statistics = new ProgressStatistics();
            statistics.setProcessId(processId);
            statistics.setCurrentProgress(process.getCurrentProgress());
            statistics.setStartTime(process.getActualStartTime());
            statistics.setEstimatedEndTime(process.getPlannedEndTime());
            statistics.setActualEndTime(process.getActualEndTime());
            statistics.setActualDuration(calculateActualDuration(processId));
            statistics.setStatus(process.getCurrentStatus());
            
            // 璁＄畻棰勮鑰楁椂
            if (process.getActualStartTime() != null && process.getPlannedEndTime() != null) {
                Duration estimatedDuration = Duration.between(process.getActualStartTime(), process.getPlannedEndTime());
                statistics.setEstimatedDuration(estimatedDuration.toMinutes());
            }
            
            // 设置鐘舵€佸悕绉?
            ProcessStatusEnum statusEnum = ProcessStatusEnum.getByCode(process.getCurrentStatus());
            if (statusEnum != null) {
                statistics.setStatusName(statusEnum.getDesc());
            }
            
            return statistics;
        } catch (Exception e) {
            logger.error("获取娴佺▼{}鐨勮繘搴︾粺璁′俊鎭け璐? {}", processId, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public Map<Long, ProgressSummary> getProgressSummary(List<Long> processIds) {
        try {
            if (processIds == null || processIds.isEmpty()) {
                return new HashMap<>();
            }
            
            QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", processIds);
            List<MoldProcess> processes = moldProcessMapper.selectList(queryWrapper);
            
            Map<Long, ProgressSummary> summaryMap = new HashMap<>();
            
            for (MoldProcess process : processes) {
                ProgressSummary summary = new ProgressSummary();
                summary.setProcessId(process.getId());
                summary.setProcessCode(process.getProcessCode());
                summary.setCurrentProgress(process.getCurrentProgress());
                summary.setStatus(process.getCurrentStatus());
                summary.setStartTime(process.getActualStartTime());
                summary.setEstimatedEndTime(process.getPlannedEndTime());
                summary.setTimeout(isProcessTimeout(process.getId()));
                
                // 设置鐘舵€佸悕绉?
                ProcessStatusEnum statusEnum = ProcessStatusEnum.getByCode(process.getCurrentStatus());
                if (statusEnum != null) {
                    summary.setStatusName(statusEnum.getDesc());
                }
                
                summaryMap.put(process.getId(), summary);
            }
            
            return summaryMap;
        } catch (Exception e) {
            logger.error("获取澶氫釜娴佺▼鐨勮繘搴︽眹鎬讳俊鎭け璐? {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }
    
    @Override
    public boolean isProcessTimeout(Long processId) {
        try {
            MoldProcess process = moldProcessMapper.selectById(processId);
            if (process == null) {
                return false;
            }
            
            LocalDateTime estimatedEndTime = process.getPlannedEndTime();
            if (estimatedEndTime == null) {
                return false;
            }
            
            // 濡傛灉娴佺▼宸插畬鎴愶紝妫€鏌ュ疄闄呭畬鎴愭椂闂存槸鍚﹁秴杩囬璁″畬鎴愭椂闂?
            if (process.getActualEndTime() != null) {
                return process.getActualEndTime().isAfter(estimatedEndTime);
            }
            
            // 濡傛灉娴佺▼鏈畬鎴愶紝妫€鏌ュ綋鍓嶆椂闂存槸鍚﹁秴杩囬璁″畬鎴愭椂闂?
            return LocalDateTime.now().isAfter(estimatedEndTime);
        } catch (Exception e) {
            logger.error("妫€鏌ユ祦绋媨}鏄惁瓒呮椂失败: {}", processId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public List<MoldProcess> getTimeoutProcesses() {
        try {
            QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNotNull("estimated_end_time")
                       .le("estimated_end_time", LocalDateTime.now())
                       .ne("current_status", ProcessStatusEnum.COMPLETED.getCode())
                       .ne("current_status", ProcessStatusEnum.CANCELLED.getCode());
            
            return moldProcessMapper.selectList(queryWrapper);
        } catch (Exception e) {
            logger.error("获取瓒呮椂娴佺▼鍒楄〃失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<ProgressTrend> getProgressTrend(Long processId, Integer timeRange) {
        try {
            LocalDateTime startTime = LocalDateTime.now().minusDays(timeRange);
            
            QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("process_id", processId)
                       .ge("create_time", startTime)
                       .orderByAsc("create_time");
            
            List<ProcessStatusHistory> histories = processStatusHistoryMapper.selectList(queryWrapper);
            
            return histories.stream()
                .map(history -> {
                    ProgressTrend trend = new ProgressTrend();
                    trend.setTimestamp(history.getCreateTime());
                    trend.setProgress(history.getProgress());
                    trend.setStatus(ProcessStatusEnum.getByCode(history.getStatus()).getDesc());
                    trend.setOperator(String.valueOf(history.getOperatorId()));
                    return trend;
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Get progress trend failed for process {}: {}", processId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean recordProgressChange(Long processId, Integer fromProgress, Integer toProgress, 
                                       Long operatorId, String description) {
        try {
            ProcessStatusHistory history = new ProcessStatusHistory();
            history.setProcessId(processId);
            history.setStatus(0); // 杩涘害鍙樻洿状态
            history.setProgress(toProgress);
            history.setOperatorId(operatorId);
            history.setDescription(description);
            history.setCreateTime(LocalDateTime.now());
            
            int result = processStatusHistoryMapper.insert(history);
            return result > 0;
        } catch (Exception e) {
            logger.error("记录娴佺▼{}鐨勮繘搴﹀彉鏇村巻鍙插け璐? {}", processId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public ProgressTrackingService.ProgressTrend getProgressTrend(Long processId, Date startTime, Date endTime) {
        try {
            LocalDateTime start = startTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime end = endTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            
            QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("process_id", processId)
                       .between("create_time", start, end)
                       .orderByAsc("create_time");
            
            List<ProcessStatusHistory> histories = processStatusHistoryMapper.selectList(queryWrapper);
            
            if (histories.isEmpty()) {
                return null;
            }
            
            // 返回鏈€鏂扮殑杩涘害瓒嬪娍数据
            ProcessStatusHistory latestHistory = histories.get(histories.size() - 1);
            ProgressTrackingService.ProgressTrend trend = new ProgressTrackingService.ProgressTrend();
            trend.setTimestamp(latestHistory.getCreateTime());
            trend.setProgress(latestHistory.getProgress());
            trend.setStatus(ProcessStatusEnum.getByCode(latestHistory.getStatus()).getDesc());
            trend.setOperator(String.valueOf(latestHistory.getOperatorId()));
            
            return trend;
        } catch (Exception e) {
            logger.error("Get progress trend failed for process {}: {}", processId, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public List<ProgressTrackingService.ProgressSummary> getProgressSummary() {
        try {
            List<MoldProcess> processes = moldProcessMapper.selectList(null);
            
            return processes.stream()
                .map(process -> {
                    ProgressTrackingService.ProgressSummary summary = new ProgressTrackingService.ProgressSummary();
                    summary.setProcessId(process.getId());
                    summary.setProcessCode(process.getProcessCode());
                    summary.setCurrentProgress(process.getCurrentProgress());
                    summary.setStatus(process.getCurrentStatus());
                    summary.setStartTime(process.getActualStartTime());
                    summary.setEstimatedEndTime(process.getPlannedEndTime());
                    summary.setTimeout(isProcessTimeout(process.getId()));
                    
                    ProcessStatusEnum statusEnum = ProcessStatusEnum.getByCode(process.getCurrentStatus());
                    if (statusEnum != null) {
                        summary.setStatusName(statusEnum.getDesc());
                    }
                    
                    return summary;
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("获取鎵€鏈夋祦绋嬬殑杩涘害鎽樿失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<MoldProcess> getDelayedProcesses() {
        try {
            QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNotNull("estimated_end_time")
                       .le("estimated_end_time", LocalDateTime.now())
                       .lt("current_progress", 100)
                       .ne("current_status", ProcessStatusEnum.COMPLETED.getCode())
                       .ne("current_status", ProcessStatusEnum.CANCELLED.getCode());
            
            return moldProcessMapper.selectList(queryWrapper);
        } catch (Exception e) {
            logger.error("获取寤惰繜娴佺▼鍒楄〃失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<MoldProcess> getNormalProgressProcesses() {
        try {
            QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNotNull("estimated_end_time")
                       .gt("estimated_end_time", LocalDateTime.now())
                       .ge("current_progress", 0)
                       .lt("current_progress", 100)
                       .ne("current_status", ProcessStatusEnum.COMPLETED.getCode())
                       .ne("current_status", ProcessStatusEnum.CANCELLED.getCode());
            
            return moldProcessMapper.selectList(queryWrapper);
        } catch (Exception e) {
            logger.error("获取姝ｅ父杩涘害娴佺▼鍒楄〃失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<MoldProcess> getAbnormalProgressProcesses() {
        try {
            QueryWrapper<MoldProcess> queryWrapper = new QueryWrapper<>();
            queryWrapper.and(wrapper -> wrapper
                       .isNull("estimated_end_time")
                       .or()
                       .lt("current_progress", 0)
                       .or()
                       .gt("current_progress", 100)
                       .or()
                       .isNull("current_progress"));
            
            return moldProcessMapper.selectList(queryWrapper);
        } catch (Exception e) {
            logger.error("获取杩涘害开傚父娴佺▼鍒楄〃失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public ProgressTrackingService.ProgressSummary getProgressPrediction(Long processId) {
        try {
            MoldProcess process = moldProcessMapper.selectById(processId);
            if (process == null) {
                return null;
            }
            
            ProgressTrackingService.ProgressSummary prediction = new ProgressTrackingService.ProgressSummary();
            prediction.setProcessId(process.getId());
            prediction.setProcessCode(process.getProcessCode());
            prediction.setCurrentProgress(process.getCurrentProgress());
            prediction.setStatus(process.getCurrentStatus());
            prediction.setStartTime(process.getActualStartTime());
            prediction.setEstimatedEndTime(getEstimatedCompletionTime(processId));
            prediction.setTimeout(isProcessTimeout(processId));
            
            ProcessStatusEnum statusEnum = ProcessStatusEnum.getByCode(process.getCurrentStatus());
            if (statusEnum != null) {
                prediction.setStatusName(statusEnum.getDesc());
            }
            
            return prediction;
        } catch (Exception e) {
            logger.error("获取娴佺▼{}鐨勮繘搴﹂娴嬪け璐? {}", processId, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public ProgressTrackingService.ProgressSummary getProgressAnalysis(Long processId) {
        try {
            MoldProcess process = moldProcessMapper.selectById(processId);
            if (process == null) {
                return null;
            }
            
            ProgressTrackingService.ProgressSummary analysis = new ProgressTrackingService.ProgressSummary();
            analysis.setProcessId(process.getId());
            analysis.setProcessCode(process.getProcessCode());
            analysis.setCurrentProgress(process.getCurrentProgress());
            analysis.setStatus(process.getCurrentStatus());
            analysis.setStartTime(process.getActualStartTime());
            analysis.setEstimatedEndTime(process.getPlannedEndTime());
            analysis.setTimeout(isProcessTimeout(processId));
            
            ProcessStatusEnum statusEnum = ProcessStatusEnum.getByCode(process.getCurrentStatus());
            if (statusEnum != null) {
                analysis.setStatusName(statusEnum.getDesc());
            }
            
            return analysis;
        } catch (Exception e) {
            logger.error("获取娴佺▼{}鐨勮繘搴﹀垎鏋愭姤鍛婂け璐? {}", processId, e.getMessage(), e);
            return null;
        }
    }
}
