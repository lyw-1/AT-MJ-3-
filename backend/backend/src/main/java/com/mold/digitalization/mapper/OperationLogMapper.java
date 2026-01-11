package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作鏃ュ織Mapper接口
 * 鎻愪緵操作鏃ュ織鐨勫鍒犳敼鏌ュ拰缁熻鍒嗘瀽功能
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    
    /**
     * 澶氭潯浠跺垎椤垫煡璇㈡搷浣滄棩蹇?
     * @param page 鍒嗛〉瀵硅薄
     * @param queryDTO 查询鏉′欢
     * @return 鍒嗛〉结果
     */
    Page<OperationLog> queryPage(@Param("page") Page<OperationLog> page, 
                                @Param("query") OperationLogQueryDTO queryDTO);
    
    /**
     * 鎸夋椂闂磋寖鍥存煡璇㈡搷浣滄棩蹇?
     * @param startTime 开€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 操作鏃ュ織鍒楄〃
     */
    List<OperationLog> queryByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                       @Param("endTime") LocalDateTime endTime);
    
    /**
     * 鎸夋搷浣滅被鍨嬬粺璁?
     * @param startTime 开€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 缁熻结果
     */
    List<OperationLogStatisticDTO> statisticByOperationType(@Param("startTime") LocalDateTime startTime, 
                                                          @Param("endTime") LocalDateTime endTime);
    
    /**
     * 鎸夌敤鎴风粺璁?
     * @param startTime 开€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 缁熻结果
     */
    List<OperationLogStatisticDTO> statisticByUser(@Param("startTime") LocalDateTime startTime, 
                                                 @Param("endTime") LocalDateTime endTime);
    
    /**
     * 鎸夋ā鍧楃粺璁?
     * @param startTime 开€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 缁熻结果
     */
    List<OperationLogStatisticDTO> statisticByModule(@Param("startTime") LocalDateTime startTime, 
                                                   @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查询鏁忔劅操作
     * @param page 鍒嗛〉瀵硅薄
     * @param queryDTO 查询鏉′欢
     * @return 鍒嗛〉结果
     */
    Page<OperationLog> querySensitiveOperations(@Param("page") Page<OperationLog> page, 
                                              @Param("query") OperationLogQueryDTO queryDTO);
    
    /**
     * 娓呯悊鎸囧畾鏃堕棿涔嬪墠鐨勬搷浣滄棩蹇?
     * @param beforeTime 娓呯悊鏃堕棿鐐?
     * @return 删除鐨勮褰曟暟
     */
    int cleanOldLogs(@Param("beforeTime") LocalDateTime beforeTime);
}
