package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.InspectionResult;
import java.util.List;

/**
 * 璐ㄩ噺妫€楠岀粨鏋滄湇鍔℃帴鍙? * 瀹氫箟璐ㄩ噺妫€楠岀粨鏋滅浉鍏崇殑涓氬姟服务方法
 */
public interface InspectionResultService extends IService<InspectionResult> {
    
    /**
     * 鏍规嵁娴佺▼ID查询妫€楠岀粨鏋滃垪琛?     * @param processId 娴佺▼ID
     * @return 妫€楠岀粨鏋滃垪琛?     */
    List<InspectionResult> getInspectionResultsByProcessId(Long processId);
    
    /**
     * 鏍规嵁妫€楠岄」鐩煡璇㈡楠岀粨鏋滃垪琛?     * @param inspectionItem 妫€楠岄」鐩?     * @return 妫€楠岀粨鏋滃垪琛?     */
    List<InspectionResult> getInspectionResultsByInspectionItem(String inspectionItem);
    
    /**
     * 鏍规嵁妫€楠岀粨鏋滄煡璇㈡楠岀粨鏋滃垪琛?     * @param inspectionResult 妫€楠岀粨鏋?     * @return 妫€楠岀粨鏋滃垪琛?     */
    List<InspectionResult> getInspectionResultsByResult(String inspectionResult);
    
    /**
     * 鏍规嵁妫€楠屼汉鍛業D查询妫€楠岀粨鏋滃垪琛?     * @param inspectorId 妫€楠屼汉鍛業D
     * @return 妫€楠岀粨鏋滃垪琛?     */
    List<InspectionResult> getInspectionResultsByInspectorId(Long inspectorId);
    
    /**
     * 查询鍚堟牸鐨勬楠岀粨鏋滃垪琛?     * @return 鍚堟牸鐨勬楠岀粨鏋滃垪琛?     */
    List<InspectionResult> getPassedInspectionResults();
    
    /**
     * 查询涓嶅悎鏍肩殑妫€楠岀粨鏋滃垪琛?     * @return 涓嶅悎鏍肩殑妫€楠岀粨鏋滃垪琛?     */
    List<InspectionResult> getFailedInspectionResults();
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询妫€楠岀粨鏋滃垪琛?     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 妫€楠岀粨鏋滃垪琛?     */
    List<InspectionResult> getInspectionResultsByTimeRange(java.time.LocalDateTime startTime, 
                                                          java.time.LocalDateTime endTime);
    
    /**
     * 创建妫€楠岀粨鏋滆褰?     * @param inspectionResult 妫€楠岀粨鏋滀俊鎭?     * @return 鏄惁创建成功
     */
    boolean createInspectionResult(InspectionResult inspectionResult);
    
    /**
     * 更新妫€楠岀粨鏋滆褰?     * @param inspectionResult 妫€楠岀粨鏋滀俊鎭?     * @return 鏄惁更新成功
     */
    boolean updateInspectionResult(InspectionResult inspectionResult);
    
    /**
     * 删除妫€楠岀粨鏋滆褰?     * @param resultId 妫€楠岀粨鏋淚D
     * @return 鏄惁删除成功
     */
    boolean deleteInspectionResult(Long resultId);
    
    /**
     * 获取鎵€鏈夋楠岀粨鏋滆褰曞垪琛?     * @return 妫€楠岀粨鏋滆褰曞垪琛?     */
    List<InspectionResult> getAllInspectionResults();
    
    /**
     * 缁熻鎸囧畾娴佺▼鐨勬楠岀粨鏋滄暟閲?     * @param processId 娴佺▼ID
     * @return 妫€楠岀粨鏋滄暟閲?     */
    int countInspectionResultsByProcessId(Long processId);
    
    /**
     * 缁熻鎸囧畾娴佺▼鐨勫悎鏍兼楠岀粨鏋滄暟閲?     * @param processId 娴佺▼ID
     * @return 鍚堟牸妫€楠岀粨鏋滄暟閲?     */
    int countPassedResultsByProcessId(Long processId);
    
    /**
     * 缁熻鎸囧畾娴佺▼鐨勪笉鍚堟牸妫€楠岀粨鏋滄暟閲?     * @param processId 娴佺▼ID
     * @return 涓嶅悎鏍兼楠岀粨鏋滄暟閲?     */
    int countFailedResultsByProcessId(Long processId);
    
    /**
     * 璁＄畻鎸囧畾娴佺▼鐨勬楠屽悎鏍肩巼
     * @param processId 娴佺▼ID
     * @return 妫€楠屽悎鏍肩巼锛堢櫨鍒嗘瘮锛?     */
    Double calculatePassRateByProcessId(Long processId);
    
    /**
     * 删除鎸囧畾娴佺▼鐨勬墍鏈夋楠岀粨鏋滆褰?     * @param processId 娴佺▼ID
     * @return 鏄惁删除成功
     */
    boolean deleteInspectionResultsByProcessId(Long processId);
    
    /**
     * 获取妫€楠岀粺璁′俊鎭?     * @return 妫€楠岀粺璁′俊鎭?     */
    InspectionStatistics getInspectionStatistics();
    
    /**
     * 妫€楠岀粺璁′俊鎭被
     */
    class InspectionStatistics {
        private Long totalInspections;
        private Long passedInspections;
        private Long failedInspections;
        private Double overallPassRate;
        private Long qualifiedInspections;
        private Long unqualifiedInspections;
        private Double qualificationRate;
        
        // 鏋勯€犲嚱鏁般€乬etter鍜宻etter方法
        public InspectionStatistics() {}
        
        public InspectionStatistics(Long totalInspections, Long passedInspections, Long failedInspections, 
                                  Double overallPassRate) {
            this.totalInspections = totalInspections;
            this.passedInspections = passedInspections;
            this.failedInspections = failedInspections;
            this.overallPassRate = overallPassRate;
        }
        
        public Long getTotalInspections() { return totalInspections; }
        public void setTotalInspections(Long totalInspections) { this.totalInspections = totalInspections; }
        
        public Long getPassedInspections() { return passedInspections; }
        public void setPassedInspections(Long passedInspections) { this.passedInspections = passedInspections; }
        
        public Long getFailedInspections() { return failedInspections; }
        public void setFailedInspections(Long failedInspections) { this.failedInspections = failedInspections; }
        
        public Double getOverallPassRate() { return overallPassRate; }
        public void setOverallPassRate(Double overallPassRate) { this.overallPassRate = overallPassRate; }
        
        public Long getQualifiedInspections() { return qualifiedInspections; }
        public void setQualifiedInspections(Long qualifiedInspections) { this.qualifiedInspections = qualifiedInspections; }
        
        public Long getUnqualifiedInspections() { return unqualifiedInspections; }
        public void setUnqualifiedInspections(Long unqualifiedInspections) { this.unqualifiedInspections = unqualifiedInspections; }
        
        public Double getQualificationRate() { return qualificationRate; }
        public void setQualificationRate(Double qualificationRate) { this.qualificationRate = qualificationRate; }
    }
}
