package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldProcess;
import java.util.Date;
import java.util.List;

/**
 * 妯″叿鍔犲伐娴佺▼服务接口
 * 瀹氫箟妯″叿鍔犲伐娴佺▼鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
public interface MoldProcessService extends IService<MoldProcess> {
    
    /**
     * 鏍规嵁娴佺▼浠ｇ爜查询娴佺▼淇℃伅
     * @param processCode 娴佺▼浠ｇ爜
     * @return 娴佺▼淇℃伅
     */
    MoldProcess getMoldProcessByCode(String processCode);
    
    /**
     * 鏍规嵁妯″叿ID查询娴佺▼鍒楄〃
     * @param moldId 妯″叿ID
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getMoldProcessesByMoldId(Long moldId);
    
    /**
     * 鏍规嵁鐢熶骇浠诲姟ID查询娴佺▼鍒楄〃
     * @param productionTaskId 鐢熶骇浠诲姟ID
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getMoldProcessesByProductionTaskId(Long productionTaskId);
    
    /**
     * 鏍规嵁鐘舵€佹煡璇㈡祦绋嬪垪琛?
     * @param status 娴佺▼状态
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getMoldProcessesByStatus(Integer status);
    
    /**
     * 鏍规嵁璐熻矗浜篒D查询娴佺▼鍒楄〃
     * @param responsibleUserId 璐熻矗浜篒D
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getMoldProcessesByResponsibleUserId(Long responsibleUserId);
    
    /**
     * 鏍规嵁操作浜哄憳ID查询娴佺▼鍒楄〃
     * @param operatorId 操作浜哄憳ID
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getMoldProcessesByOperatorId(Long operatorId);
    
    /**
     * 鏍规嵁璁惧ID查询娴佺▼鍒楄〃
     * @param equipmentId 璁惧ID
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getMoldProcessesByEquipmentId(Long equipmentId);
    
    /**
     * 鏍规嵁浼樺厛绾ф煡璇㈡祦绋嬪垪琛?
     * @param priority 浼樺厛绾?
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getMoldProcessesByPriority(Integer priority);
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询娴佺▼鍒楄〃
     * @param startTime 开€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getMoldProcessesByTimeRange(Date startTime, Date endTime);
    
    /**
     * 创建鏂版祦绋?
     * @param moldProcess 娴佺▼淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createMoldProcess(MoldProcess moldProcess);
    
    /**
     * 更新娴佺▼淇℃伅
     * @param moldProcess 娴佺▼淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateMoldProcess(MoldProcess moldProcess);
    
    /**
     * 删除娴佺▼
     * @param processId 娴佺▼ID
     * @return 鏄惁删除成功
     */
    boolean deleteMoldProcess(Long processId);
    
    /**
     * 获取鎵€鏈夋祦绋嬪垪琛?
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> getAllMoldProcesses();
    
    /**
     * 更新娴佺▼状态
     * @param processId 娴佺▼ID
     * @param status 鏂扮姸鎬?
     * @return 鏄惁更新成功
     */
    boolean updateMoldProcessStatus(Long processId, Integer status);
    
    /**
     * 更新娴佺▼杩涘害
     * @param processId 娴佺▼ID
     * @param progress 鏂拌繘搴?
     * @return 鏄惁更新成功
     */
    boolean updateMoldProcessProgress(Long processId, Integer progress);
    
    /**
     * 开€濮嬫祦绋?
     * @param processId 娴佺▼ID
     * @return 鏄惁开€濮嬫垚鍔?
     */
    boolean startMoldProcess(Long processId);
    
    /**
     * 瀹屾垚娴佺▼
     * @param processId 娴佺▼ID
     * @return 鏄惁瀹屾垚成功
     */
    boolean completeMoldProcess(Long processId);
    
    /**
     * 鏆傚仠娴佺▼
     * @param processId 娴佺▼ID
     * @return 鏄惁鏆傚仠成功
     */
    boolean pauseMoldProcess(Long processId);
    
    /**
     * 鎭㈠娴佺▼
     * @param processId 娴佺▼ID
     * @return 鏄惁鎭㈠成功
     */
    boolean resumeMoldProcess(Long processId);
    
    /**
     * 鍙栨秷娴佺▼
     * @param processId 娴佺▼ID
     * @return 鏄惁鍙栨秷成功
     */
    boolean cancelMoldProcess(Long processId);
    
    /**
     * 获取娴佺▼缁熻淇℃伅
     * @return 娴佺▼缁熻淇℃伅
     */
    MoldProcessStatistics getMoldProcessStatistics();
    
    /**
     * 娴佺▼缁熻淇℃伅绫?
     */
    class MoldProcessStatistics {
        private Long totalProcesses;
        private Long completedProcesses;
        private Long inProgressProcesses;
        private Long pendingProcesses;
        private Long cancelledProcesses;
        
        // 鏋勯€犲嚱鏁般€乬etter鍜宻etter方法
        public MoldProcessStatistics() {}
        
        public MoldProcessStatistics(Long totalProcesses, Long completedProcesses, Long inProgressProcesses, 
                                   Long pendingProcesses, Long cancelledProcesses) {
            this.totalProcesses = totalProcesses;
            this.completedProcesses = completedProcesses;
            this.inProgressProcesses = inProgressProcesses;
            this.pendingProcesses = pendingProcesses;
            this.cancelledProcesses = cancelledProcesses;
        }
        
        public Long getTotalProcesses() { return totalProcesses; }
        public void setTotalProcesses(Long totalProcesses) { this.totalProcesses = totalProcesses; }
        
        public Long getCompletedProcesses() { return completedProcesses; }
        public void setCompletedProcesses(Long completedProcesses) { this.completedProcesses = completedProcesses; }
        
        public Long getInProgressProcesses() { return inProgressProcesses; }
        public void setInProgressProcesses(Long inProgressProcesses) { this.inProgressProcesses = inProgressProcesses; }
        
        public Long getPendingProcesses() { return pendingProcesses; }
        public void setPendingProcesses(Long pendingProcesses) { this.pendingProcesses = pendingProcesses; }
        
        public Long getCancelledProcesses() { return cancelledProcesses; }
        public void setCancelledProcesses(Long cancelledProcesses) { this.cancelledProcesses = cancelledProcesses; }
    }
}
