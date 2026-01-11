package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.ProcessException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 娴佺▼开傚父记录Mapper接口
 * 鎻愪緵娴佺▼开傚父记录琛ㄧ殑数据搴撴搷浣滄柟娉? */
@Mapper
public interface ProcessExceptionMapper extends BaseMapper<ProcessException> {
    
    /**
     * 鏍规嵁娴佺▼ID查询开傚父记录鍒楄〃
     * @param processId 娴佺▼ID
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> selectByProcessId(Long processId);
    
    /**
     * 鏍规嵁开傚父绫诲瀷查询开傚父记录鍒楄〃
     * @param exceptionType 开傚父绫诲瀷
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> selectByExceptionType(Integer exceptionType);
    
    /**
     * 鏍规嵁涓ラ噸绋嬪害查询开傚父记录鍒楄〃
     * @param severity 涓ラ噸绋嬪害
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> selectBySeverity(Integer severity);
    
    /**
     * 鏍规嵁澶勭悊鐘舵€佹煡璇㈠紓甯歌褰曞垪琛?     * @param handlingStatus 澶勭悊状态     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> selectByHandlingStatus(Integer handlingStatus);
    
    /**
     * 鏍规嵁澶勭悊浜哄憳ID查询开傚父记录鍒楄〃
     * @param handlerId 澶勭悊浜哄憳ID
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> selectByHandlerId(Long handlerId);
    
    /**
     * 查询鏈鐞嗙殑开傚父记录鍒楄〃
     * @return 鏈鐞嗙殑开傚父记录鍒楄〃
     */
    List<ProcessException> selectUnhandledExceptions();
    
    /**
     * 查询楂樹弗閲嶇▼搴︾殑开傚父记录鍒楄〃
     * @return 楂樹弗閲嶇▼搴︾殑开傚父记录鍒楄〃
     */
    List<ProcessException> selectHighSeverityExceptions();
    
    /**
     * 更新开傚父澶勭悊状态     * @param exceptionId 开傚父记录ID
     * @param handlingStatus 澶勭悊状态     * @param handlerId 澶勭悊浜哄憳ID
     * @return 更新褰卞搷鐨勮鏁?     */
    int updateHandlingStatus(@Param("exceptionId") Long exceptionId, 
                           @Param("handlingStatus") Integer handlingStatus, 
                           @Param("handlerId") Long handlerId);
    
    /**
     * 更新开傚父澶勭悊淇℃伅
     * @param exceptionId 开傚父记录ID
     * @param handlingStatus 澶勭悊状态     * @param handlerId 澶勭悊浜哄憳ID
     * @param handlingResult 澶勭悊结果
     * @param handlingTime 澶勭悊鏃堕棿
     * @return 更新褰卞搷鐨勮鏁?     */
    int updateHandlingInfo(@Param("exceptionId") Long exceptionId, 
                         @Param("handlingStatus") Integer handlingStatus, 
                         @Param("handlerId") Long handlerId, 
                         @Param("handlingResult") String handlingResult, 
                         @Param("handlingTime") java.time.LocalDateTime handlingTime);
    
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
     * @return 删除褰卞搷鐨勮鏁?     */
    int deleteByProcessId(Long processId);
}
