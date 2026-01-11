package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.ProcessStatusHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 娴佺▼鐘舵€佸巻鍙睲apper接口
 * 鎻愪緵娴佺▼鐘舵€佸巻鍙茶〃鐨勬暟鎹簱操作方法
 */
@Mapper
public interface ProcessStatusHistoryMapper extends BaseMapper<ProcessStatusHistory> {
    
    /**
     * 鏍规嵁娴佺▼ID查询鐘舵€佸巻鍙插垪琛?
     * @param processId 娴佺▼ID
     * @return 鐘舵€佸巻鍙插垪琛?
     */
    List<ProcessStatusHistory> selectByProcessId(Long processId);
    
    /**
     * 鏍规嵁鐘舵€佹煡璇㈢姸鎬佸巻鍙插垪琛?
     * @param status 状态
     * @return 鐘舵€佸巻鍙插垪琛?
     */
    List<ProcessStatusHistory> selectByStatus(Integer status);
    
    /**
     * 鏍规嵁操作浜哄憳ID查询鐘舵€佸巻鍙插垪琛?
     * @param operatorId 操作浜哄憳ID
     * @return 鐘舵€佸巻鍙插垪琛?
     */
    List<ProcessStatusHistory> selectByOperatorId(Long operatorId);
    
    /**
     * 获取娴佺▼鐨勬渶鏂扮姸鎬佸巻鍙茶褰?
     * @param processId 娴佺▼ID
     * @return 鏈€鏂扮姸鎬佸巻鍙茶褰?
     */
    ProcessStatusHistory selectLatestByProcessId(Long processId);
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询鐘舵€佸巻鍙插垪琛?
     * @param startTime 开€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 鐘舵€佸巻鍙插垪琛?
     */
    List<ProcessStatusHistory> selectByTimeRange(@Param("startTime") java.time.LocalDateTime startTime, 
                                               @Param("endTime") java.time.LocalDateTime endTime);
    
    /**
     * 缁熻娴佺▼鐨勭姸鎬佸彉鏇存鏁?
     * @param processId 娴佺▼ID
     * @return 鐘舵€佸彉鏇存鏁?
     */
    int countStatusChangesByProcessId(Long processId);
    
    /**
     * 删除鎸囧畾娴佺▼鐨勬墍鏈夌姸鎬佸巻鍙茶褰?
     * @param processId 娴佺▼ID
     * @return 删除褰卞搷鐨勮鏁?
     */
    int deleteByProcessId(Long processId);
}