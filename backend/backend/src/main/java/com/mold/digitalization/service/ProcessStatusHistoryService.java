package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.ProcessStatusHistory;
import java.util.List;

/**
 * 娴佺▼鐘舵€佸巻鍙叉湇鍔℃帴鍙? * 瀹氫箟娴佺▼鐘舵€佸巻鍙茬浉鍏崇殑涓氬姟服务方法
 */
public interface ProcessStatusHistoryService extends IService<ProcessStatusHistory> {
    
    /**
     * 鏍规嵁娴佺▼ID查询鐘舵€佸巻鍙插垪琛?     * @param processId 娴佺▼ID
     * @return 鐘舵€佸巻鍙插垪琛?     */
    List<ProcessStatusHistory> getStatusHistoriesByProcessId(Long processId);
    
    /**
     * 鏍规嵁鐘舵€佹煡璇㈢姸鎬佸巻鍙插垪琛?     * @param status 状态     * @return 鐘舵€佸巻鍙插垪琛?     */
    List<ProcessStatusHistory> getStatusHistoriesByStatus(Integer status);
    
    /**
     * 鏍规嵁操作浜哄憳ID查询鐘舵€佸巻鍙插垪琛?     * @param operatorId 操作浜哄憳ID
     * @return 鐘舵€佸巻鍙插垪琛?     */
    List<ProcessStatusHistory> getStatusHistoriesByOperatorId(Long operatorId);
    
    /**
     * 获取娴佺▼鐨勬渶鏂扮姸鎬佸巻鍙茶褰?     * @param processId 娴佺▼ID
     * @return 鏈€鏂扮姸鎬佸巻鍙茶褰?     */
    ProcessStatusHistory getLatestStatusHistoryByProcessId(Long processId);
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询鐘舵€佸巻鍙插垪琛?     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 鐘舵€佸巻鍙插垪琛?     */
    List<ProcessStatusHistory> getStatusHistoriesByTimeRange(java.time.LocalDateTime startTime, 
                                                           java.time.LocalDateTime endTime);
    
    /**
     * 创建鐘舵€佸巻鍙茶褰?     * @param statusHistory 鐘舵€佸巻鍙蹭俊鎭?     * @return 鏄惁创建成功
     */
    boolean createStatusHistory(ProcessStatusHistory statusHistory);
    
    /**
     * 更新鐘舵€佸巻鍙茶褰?     * @param statusHistory 鐘舵€佸巻鍙蹭俊鎭?     * @return 鏄惁更新成功
     */
    boolean updateStatusHistory(ProcessStatusHistory statusHistory);
    
    /**
     * 删除鐘舵€佸巻鍙茶褰?     * @param historyId 鍘嗗彶记录ID
     * @return 鏄惁删除成功
     */
    boolean deleteStatusHistory(Long historyId);
    
    /**
     * 获取鎵€鏈夌姸鎬佸巻鍙茶褰曞垪琛?     * @return 鐘舵€佸巻鍙茶褰曞垪琛?     */
    List<ProcessStatusHistory> getAllStatusHistories();
    
    /**
     * 缁熻娴佺▼鐨勭姸鎬佸彉鏇存鏁?     * @param processId 娴佺▼ID
     * @return 鐘舵€佸彉鏇存鏁?     */
    int countStatusChangesByProcessId(Long processId);
    
    /**
     * 删除鎸囧畾娴佺▼鐨勬墍鏈夌姸鎬佸巻鍙茶褰?     * @param processId 娴佺▼ID
     * @return 鏄惁删除成功
     */
    boolean deleteStatusHistoriesByProcessId(Long processId);
    
    /**
     * 记录娴佺▼鐘舵€佸彉鏇?     * @param processId 娴佺▼ID
     * @param status 鏂扮姸鎬?     * @param operatorId 操作浜哄憳ID
     * @param remark 澶囨敞
     * @return 鏄惁记录成功
     */
    boolean recordStatusChange(Long processId, Integer status, Long operatorId, String remark);
}
