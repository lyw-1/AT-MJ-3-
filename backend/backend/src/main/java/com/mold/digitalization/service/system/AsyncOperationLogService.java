package com.mold.digitalization.service.system;

import com.mold.digitalization.entity.system.OperationLog;
import org.springframework.scheduling.annotation.Async;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 开傛操作鏃ュ織服务
 * 鎻愪緵开傛澶勭悊操作鏃ュ織鐨勫姛鑳斤紝鐢ㄤ簬澶ф暟鎹噺鍦烘櫙
 */
public interface AsyncOperationLogService {

    /**
     * 开傛鎵归噺淇濆瓨操作鏃ュ織
     * 閫傜敤浜庨渶瑕佹壒閲忚褰曞ぇ閲忔搷浣滄棩蹇楃殑鍦烘櫙
     * 
     * @param operationLogs 操作鏃ュ織鍒楄〃
     */
    @Async
    void batchSaveOperationLogsAsync(List<OperationLog> operationLogs);

    /**
     * 开傛鍒嗘壒娓呯悊鍘嗗彶鏃ュ織
     * 閫氳繃鍒嗘壒澶勭悊鐨勬柟开忔竻鐞嗗ぇ閲忓巻鍙叉棩蹇楋紝閬垮厤闀挎椂闂村崰鐢ㄦ暟鎹簱连接
     * 
     * @param cutoffTime 鎴鏃堕棿锛屾竻鐞嗘鏃堕棿涔嬪墠鐨勬棩蹇?     * @param batchSize 姣忔壒澶勭悊鐨勬暟鎹噺
     * @param sensitiveLevelThreshold 鏁忔劅绾у埆闃堝€硷紝楂樹簬姝ょ骇鍒殑鏃ュ織浼氳淇濈暀
     * @return 娓呯悊鐨勬€昏褰曟暟
     */
    @Async
    int batchCleanOldLogsAsync(LocalDateTime cutoffTime, int batchSize, String sensitiveLevelThreshold);

    /**
     * 开傛瀵煎嚭操作鏃ュ織
     * 灏嗘搷浣滄棩蹇楁暟鎹紓姝ュ鍑轰负鏂囦欢
     * 
     * @param queryParams 查询鍙傛暟
     * @param exportFilePath 瀵煎嚭鏂囦欢璺緞
     * @return 瀵煎嚭鐨勬枃浠惰矾寰?     */
    @Async
    String asyncExportOperationLogs(Object queryParams, String exportFilePath);
}