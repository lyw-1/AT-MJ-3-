package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.Log;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 鏃ュ織服务接口
 * 瀹氫箟鏃ュ織鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
public interface LogService extends IService<Log> {
    
    /**
     * 鏍规嵁用户ID查询鏃ュ織鍒楄〃
     * @param userId 用户ID
     * @return 鏃ュ織鍒楄〃
     */
    List<Log> getLogsByUserId(Long userId);
    
    /**
     * 鏍规嵁操作绫诲瀷查询鏃ュ織鍒楄〃
     * @param operationType 操作绫诲瀷
     * @return 鏃ュ織鍒楄〃
     */
    List<Log> getLogsByOperationType(String operationType);
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询鏃ュ織鍒楄〃
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 鏃ュ織鍒楄〃
     */
    List<Log> getLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 创建鏂版棩蹇楄褰?     * @param log 鏃ュ織淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createLog(Log log);
    
    /**
     * 获取鎵€鏈夋棩蹇楀垪琛?     * @return 鏃ュ織鍒楄〃
     */
    List<Log> getAllLogs();
    
    /**
     * 娓呯悊鎸囧畾鏃堕棿涔嬪墠鐨勬棩蹇?     * @param beforeDate 娓呯悊鎴日期
     * @return 娓呯悊鐨勬棩蹇楁暟閲?     */
    int cleanLogsBefore(LocalDateTime beforeDate);
}
