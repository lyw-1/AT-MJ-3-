package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.ProcessException;
import java.util.List;

/**
 * 娴佺▼开傚父记录服务接口
 * 瀹氫箟娴佺▼开傚父记录鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
public interface ProcessExceptionService extends IService<ProcessException> {
    
    /**
     * 鏍规嵁娴佺▼ID查询开傚父记录鍒楄〃
     * @param processId 娴佺▼ID
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> getExceptionsByProcessId(Long processId);
    
    /**
     * 鏍规嵁开傚父绫诲瀷查询开傚父记录鍒楄〃
     * @param exceptionType 开傚父绫诲瀷
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> getExceptionsByExceptionType(Integer exceptionType);
    
    /**
     * 鏍规嵁涓ラ噸绋嬪害查询开傚父记录鍒楄〃
     * @param severity 涓ラ噸绋嬪害
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> getExceptionsBySeverity(Integer severity);
    
    /**
     * 鏍规嵁澶勭悊鐘舵€佹煡璇㈠紓甯歌褰曞垪琛?     * @param handlingStatus 澶勭悊状态     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> getExceptionsByHandlingStatus(Integer handlingStatus);
    
    /**
     * 鏍规嵁澶勭悊浜哄憳ID查询开傚父记录鍒楄〃
     * @param handlerId 澶勭悊浜哄憳ID
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> getExceptionsByHandlerId(Long handlerId);
    
    /**
     * 查询鏈鐞嗙殑开傚父记录鍒楄〃
     * @return 鏈鐞嗙殑开傚父记录鍒楄〃
     */
    List<ProcessException> getUnhandledExceptions();
    
    /**
     * 查询楂樹弗閲嶇▼搴︾殑开傚父记录鍒楄〃
     * @return 楂樹弗閲嶇▼搴︾殑开傚父记录鍒楄〃
     */
    List<ProcessException> getHighSeverityExceptions();
    
    /**
     * 创建开傚父记录
     * @param processException 开傚父记录淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createProcessException(ProcessException processException);
    
    /**
     * 更新开傚父记录
     * @param processException 开傚父记录淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateProcessException(ProcessException processException);
    
    /**
     * 删除开傚父记录
     * @param exceptionId 开傚父记录ID
     * @return 鏄惁删除成功
     */
    boolean deleteProcessException(Long exceptionId);
    
    /**
     * 获取鎵€鏈夊紓甯歌褰曞垪琛?     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> getAllProcessExceptions();
    
    /**
     * 更新开傚父澶勭悊状态     * @param exceptionId 开傚父记录ID
     * @param handlingStatus 澶勭悊状态     * @param handlerId 澶勭悊浜哄憳ID
     * @return 鏄惁更新成功
     */
    boolean updateExceptionHandlingStatus(Long exceptionId, Integer handlingStatus, Long handlerId);
    
    /**
     * 澶勭悊开傚父
     * @param exceptionId 开傚父记录ID
     * @param handlerId 澶勭悊浜哄憳ID
     * @param handlingResult 澶勭悊结果
     * @return 鏄惁澶勭悊成功
     */
    boolean handleException(Long exceptionId, Long handlerId, String handlingResult);
    
    /**
     * 缁熻鎸囧畾娴佺▼鐨勫紓甯告暟閲?     * @param processId 娴佺▼ID
     * @return 开傚父鏁伴噺
     */
    int countExceptionsByProcessId(Long processId);
    
    /**
     * 缁熻鏈鐞嗙殑开傚父鏁伴噺
     * @return 鏈鐞嗙殑开傚父鏁伴噺
     */
    int countUnhandledExceptions();
    
    /**
     * 删除鎸囧畾娴佺▼鐨勬墍鏈夊紓甯歌褰?     * @param processId 娴佺▼ID
     * @return 鏄惁删除成功
     */
    boolean deleteExceptionsByProcessId(Long processId);
    
    /**
     * 获取开傚父缁熻淇℃伅
     * @return 开傚父缁熻淇℃伅
     */
    com.mold.digitalization.entity.ExceptionStatistics getExceptionStatistics();
    
    /**
     * 开傚父缁熻淇℃伅绫?     */
    class ExceptionStatistics {
        private Long totalExceptions;
        private Long unhandledExceptions;
        private Long handledExceptions;
        private Long highSeverityExceptions;
        private Long normalSeverityExceptions;
        
        // 鏋勯€犲嚱鏁般€乬etter鍜宻etter方法
        public ExceptionStatistics() {}
        
        public ExceptionStatistics(Long totalExceptions, Long unhandledExceptions, Long handledExceptions, 
                                 Long highSeverityExceptions, Long normalSeverityExceptions) {
            this.totalExceptions = totalExceptions;
            this.unhandledExceptions = unhandledExceptions;
            this.handledExceptions = handledExceptions;
            this.highSeverityExceptions = highSeverityExceptions;
            this.normalSeverityExceptions = normalSeverityExceptions;
        }
        
        public Long getTotalExceptions() { return totalExceptions; }
        public void setTotalExceptions(Long totalExceptions) { this.totalExceptions = totalExceptions; }
        
        public Long getUnhandledExceptions() { return unhandledExceptions; }
        public void setUnhandledExceptions(Long unhandledExceptions) { this.unhandledExceptions = unhandledExceptions; }
        
        public Long getHandledExceptions() { return handledExceptions; }
        public void setHandledExceptions(Long handledExceptions) { this.handledExceptions = handledExceptions; }
        
        public Long getHighSeverityExceptions() { return highSeverityExceptions; }
        public void setHighSeverityExceptions(Long highSeverityExceptions) { this.highSeverityExceptions = highSeverityExceptions; }
        
        public Long getNormalSeverityExceptions() { return normalSeverityExceptions; }
        public void setNormalSeverityExceptions(Long normalSeverityExceptions) { this.normalSeverityExceptions = normalSeverityExceptions; }
    }
}
