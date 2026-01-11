package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 杩涘害璺熻釜服务接口
 * 瀹氫箟妯″叿鍔犲伐娴佺▼杩涘害璺熻釜鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
public interface ProgressTrackingService extends IService<MoldProcess> {
    
    /**
     * 获取娴佺▼鐨勫綋鍓嶈繘搴?     * @param processId 娴佺▼ID
     * @return 褰撳墠杩涘害鐧惧垎姣?     */
    Integer getCurrentProgress(Long processId);
    
    /**
     * 更新娴佺▼杩涘害
     * @param processId 娴佺▼ID
     * @param progress 杩涘害鐧惧垎姣?     * @param operatorId 操作浜哄憳ID
     * @return 鏄惁更新成功
     */
    boolean updateProgress(Long processId, Integer progress, Long operatorId);
    
    /**
     * 获取娴佺▼鐨勮繘搴﹀巻鍙茶褰?     * @param processId 娴佺▼ID
     * @return 杩涘害鍘嗗彶记录鍒楄〃
     */
    List<ProcessStatusHistory> getProgressHistory(Long processId);
    
    /**
     * 获取娴佺▼鐨勯璁″畬鎴愭椂闂?     * @param processId 娴佺▼ID
     * @return 棰勮瀹屾垚鏃堕棿
     */
    LocalDateTime getEstimatedCompletionTime(Long processId);
    
    /**
     * 璁＄畻娴佺▼鐨勫疄闄呰€楁椂
     * @param processId 娴佺▼ID
     * @return 实际鑰楁椂锛堝垎閽燂級
     */
    Long calculateActualDuration(Long processId);
    
    /**
     * 获取娴佺▼鐨勮繘搴︾粺璁′俊鎭?     * @param processId 娴佺▼ID
     * @return 杩涘害缁熻淇℃伅
     */
    ProgressStatistics getProgressStatistics(Long processId);
    
    /**
     * 获取澶氫釜娴佺▼鐨勮繘搴︽眹鎬讳俊鎭?     * @param processIds 娴佺▼ID鍒楄〃
     * @return 杩涘害姹囨€讳俊鎭?     */
    Map<Long, ProgressSummary> getProgressSummary(List<Long> processIds);
    
    /**
     * 妫€鏌ユ祦绋嬫槸鍚﹁秴鏃?     * @param processId 娴佺▼ID
     * @return 鏄惁瓒呮椂
     */
    boolean isProcessTimeout(Long processId);
    
    /**
     * 获取瓒呮椂娴佺▼鍒楄〃
     * @return 瓒呮椂娴佺▼鍒楄〃
     */
    List<MoldProcess> getTimeoutProcesses();
    
    /**
     * 获取娴佺▼鐨勮繘搴﹁秼鍔挎暟鎹?     * @param processId 娴佺▼ID
     * @param timeRange 鏃堕棿鑼冨洿锛堝ぉ锛?     * @return 杩涘害瓒嬪娍数据
     */
    List<ProgressTrend> getProgressTrend(Long processId, Integer timeRange);
    
    /**
     * 获取娴佺▼鐨勮繘搴﹁秼鍔挎暟鎹紙鎸夋椂闂磋寖鍥达級
     * @param processId 娴佺▼ID
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 杩涘害瓒嬪娍数据
     */
    ProgressTrend getProgressTrend(Long processId, Date startTime, Date endTime);
    
    /**
     * 获取鎵€鏈夋祦绋嬬殑杩涘害鎽樿
     * @return 杩涘害鎽樿鍒楄〃
     */
    List<ProgressSummary> getProgressSummary();
    
    /**
     * 获取寤惰繜娴佺▼鍒楄〃
     * @return 寤惰繜娴佺▼鍒楄〃
     */
    List<MoldProcess> getDelayedProcesses();
    
    /**
     * 获取姝ｅ父杩涘害娴佺▼鍒楄〃
     * @return 姝ｅ父杩涘害娴佺▼鍒楄〃
     */
    List<MoldProcess> getNormalProgressProcesses();
    
    /**
     * 获取杩涘害开傚父娴佺▼鍒楄〃
     * @return 杩涘害开傚父娴佺▼鍒楄〃
     */
    List<MoldProcess> getAbnormalProgressProcesses();
    
    /**
     * 获取娴佺▼杩涘害棰勬祴
     * @param processId 娴佺▼ID
     * @return 杩涘害棰勬祴淇℃伅
     */
    ProgressSummary getProgressPrediction(Long processId);
    
    /**
     * 获取娴佺▼杩涘害鍒嗘瀽鎶ュ憡
     * @param processId 娴佺▼ID
     * @return 杩涘害鍒嗘瀽鎶ュ憡
     */
    ProgressSummary getProgressAnalysis(Long processId);
    
    /**
     * 记录杩涘害鍙樻洿鍘嗗彶
     * @param processId 娴佺▼ID
     * @param fromProgress 鍘熻繘搴?     * @param toProgress 鐩爣杩涘害
     * @param operatorId 操作浜哄憳ID
     * @param description 鎻忚堪淇℃伅
     * @return 鏄惁记录成功
     */
    boolean recordProgressChange(Long processId, Integer fromProgress, Integer toProgress, 
                                Long operatorId, String description);
    
    /**
     * 杩涘害缁熻淇℃伅绫?     */
    class ProgressStatistics {
        private Long processId;
        private Integer currentProgress;
        private LocalDateTime startTime;
        private LocalDateTime estimatedEndTime;
        private LocalDateTime actualEndTime;
        private Long actualDuration; // 鍒嗛挓
        private Long estimatedDuration; // 鍒嗛挓
        private Integer status;
        private String statusName;
        
        public ProgressStatistics() {}
        
        public ProgressStatistics(Long processId, Integer currentProgress, LocalDateTime startTime, 
                                LocalDateTime estimatedEndTime, LocalDateTime actualEndTime, 
                                Long actualDuration, Long estimatedDuration, Integer status, String statusName) {
            this.processId = processId;
            this.currentProgress = currentProgress;
            this.startTime = startTime;
            this.estimatedEndTime = estimatedEndTime;
            this.actualEndTime = actualEndTime;
            this.actualDuration = actualDuration;
            this.estimatedDuration = estimatedDuration;
            this.status = status;
            this.statusName = statusName;
        }
        
        // Getter鍜孲etter方法
        public Long getProcessId() { return processId; }
        public void setProcessId(Long processId) { this.processId = processId; }
        
        public Integer getCurrentProgress() { return currentProgress; }
        public void setCurrentProgress(Integer currentProgress) { this.currentProgress = currentProgress; }
        
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        
        public LocalDateTime getEstimatedEndTime() { return estimatedEndTime; }
        public void setEstimatedEndTime(LocalDateTime estimatedEndTime) { this.estimatedEndTime = estimatedEndTime; }
        
        public LocalDateTime getActualEndTime() { return actualEndTime; }
        public void setActualEndTime(LocalDateTime actualEndTime) { this.actualEndTime = actualEndTime; }
        
        public Long getActualDuration() { return actualDuration; }
        public void setActualDuration(Long actualDuration) { this.actualDuration = actualDuration; }
        
        public Long getEstimatedDuration() { return estimatedDuration; }
        public void setEstimatedDuration(Long estimatedDuration) { this.estimatedDuration = estimatedDuration; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public String getStatusName() { return statusName; }
        public void setStatusName(String statusName) { this.statusName = statusName; }
    }
    
    /**
     * 杩涘害姹囨€讳俊鎭被
     */
    class ProgressSummary {
        private Long processId;
        private String processCode;
        private Integer currentProgress;
        private Integer status;
        private String statusName;
        private LocalDateTime startTime;
        private LocalDateTime estimatedEndTime;
        private boolean isTimeout;
        
        public ProgressSummary() {}
        
        public ProgressSummary(Long processId, String processCode, Integer currentProgress, 
                             Integer status, String statusName, LocalDateTime startTime, 
                             LocalDateTime estimatedEndTime, boolean isTimeout) {
            this.processId = processId;
            this.processCode = processCode;
            this.currentProgress = currentProgress;
            this.status = status;
            this.statusName = statusName;
            this.startTime = startTime;
            this.estimatedEndTime = estimatedEndTime;
            this.isTimeout = isTimeout;
        }
        
        // Getter鍜孲etter方法
        public Long getProcessId() { return processId; }
        public void setProcessId(Long processId) { this.processId = processId; }
        
        public String getProcessCode() { return processCode; }
        public void setProcessCode(String processCode) { this.processCode = processCode; }
        
        public Integer getCurrentProgress() { return currentProgress; }
        public void setCurrentProgress(Integer currentProgress) { this.currentProgress = currentProgress; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        
        public String getStatusName() { return statusName; }
        public void setStatusName(String statusName) { this.statusName = statusName; }
        
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        
        public LocalDateTime getEstimatedEndTime() { return estimatedEndTime; }
        public void setEstimatedEndTime(LocalDateTime estimatedEndTime) { this.estimatedEndTime = estimatedEndTime; }
        
        public boolean isTimeout() { return isTimeout; }
        public void setTimeout(boolean timeout) { isTimeout = timeout; }
    }
    
    /**
     * 杩涘害瓒嬪娍数据绫?     */
    class ProgressTrend {
        private LocalDateTime timestamp;
        private Integer progress;
        private String status;
        private String operator;
        
        public ProgressTrend() {}
        
        public ProgressTrend(LocalDateTime timestamp, Integer progress, String status, String operator) {
            this.timestamp = timestamp;
            this.progress = progress;
            this.status = status;
            this.operator = operator;
        }
        
        // Getter鍜孲etter方法
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        
        public Integer getProgress() { return progress; }
        public void setProgress(Integer progress) { this.progress = progress; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
    }
}
