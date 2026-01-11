package com.mold.digitalization.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 操作鏃ュ織服务接口
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 鍒嗛〉查询操作鏃ュ織锛堝吋瀹规棫接口锛?     * @param pageNum 椤电爜
     * @param pageSize 姣忛〉澶у皬
     * @param params 查询鍙傛暟
     * @return 鍒嗛〉结果
     */
    Page<OperationLog> queryPage(Integer pageNum, Integer pageSize, Map<String, Object> params);
    
    /**
     * 澶氭潯浠跺垎椤垫煡璇㈡搷浣滄棩蹇?     * @param queryDTO 查询鏉′欢
     * @return 鍒嗛〉结果
     */
    Page<OperationLog> queryByCondition(OperationLogQueryDTO queryDTO);

    /**
     * 淇濆瓨操作鏃ュ織
     * @param log 操作鏃ュ織瀵硅薄
     * @return 淇濆瓨结果
     */
    boolean saveOperationLog(OperationLog log);

    /**
     * 鎵归噺删除操作鏃ュ織
     * @param ids 鏃ュ織ID鍒楄〃
     * @return 删除结果
     */
    boolean deleteBatch(Long[] ids);

    /**
     * 娓呯┖操作鏃ュ織
     * @return 娓呯┖结果
     */
    boolean cleanOperationLogs();
    
    /**
     * 娓呯悊鎸囧畾鏃堕棿涔嬪墠鐨勬搷浣滄棩蹇?     * @param beforeTime 娓呯悊鏃堕棿鐐?     * @return 删除鐨勮褰曟暟
     */
    int cleanOldLogs(LocalDateTime beforeTime);
    
    /**
     * 鎸夋搷浣滅被鍨嬬粺璁?     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 缁熻结果
     */
    List<OperationLogStatisticDTO> statisticByOperationType(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 鎸夌敤鎴风粺璁?     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 缁熻结果
     */
    List<OperationLogStatisticDTO> statisticByUser(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 鎸夋ā鍧楃粺璁?     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 缁熻结果
     */
    List<OperationLogStatisticDTO> statisticByModule(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 鍒嗛〉查询鏁忔劅操作
     * @param queryDTO 查询鏉′欢
     * @return 鍒嗛〉结果
     */
    Page<OperationLog> querySensitiveOperations(OperationLogQueryDTO queryDTO);
    
    /**
     * 鎸夋晱鎰熺骇鍒煡璇㈡搷浣滄棩蹇?     * @param sensitiveLevel 鏁忔劅绾у埆
     * @return 操作鏃ュ織鍒楄〃
     */
    List<OperationLog> queryBySensitiveLevel(String sensitiveLevel);
    
    /**
     * 瀵煎嚭操作鏃ュ織
     * @param queryDTO 查询鏉′欢
     * @return 瀵煎嚭鏂囦欢璺緞
     */
    String exportOperationLogs(OperationLogQueryDTO queryDTO);
    
    /**
     * 鎸夋椂闂磋寖鍥存煡璇㈡搷浣滄棩蹇?     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 操作鏃ュ織鍒楄〃
     */
    List<OperationLog> queryByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 鎸夋晱鎰熺骇鍒竻鐞嗘搷浣滄棩蹇?     * @param normalBeforeTime 鏅€氭棩蹇楁竻鐞嗘椂闂撮槇鍊?     * @param criticalBeforeTime 鍏抽敭鏃ュ織娓呯悊鏃堕棿闃堝€?     * @return 删除鐨勮褰曟暟
     */
    int cleanOldLogsByLevel(LocalDateTime normalBeforeTime, LocalDateTime criticalBeforeTime);
}
