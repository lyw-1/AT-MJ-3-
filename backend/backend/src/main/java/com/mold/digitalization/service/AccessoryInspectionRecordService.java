package com.mold.digitalization.service;

import com.mold.digitalization.entity.AccessoryInspectionRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢璐ㄦ记录Service接口
 * 鎻愪緵閰嶄欢璐ㄦ鐩稿叧鐨勪笟鍔￠€昏緫
 */
public interface AccessoryInspectionRecordService extends IService<AccessoryInspectionRecord> {

    /**
     * 创建璐ㄦ记录
     * @param record 璐ㄦ记录实体
     * @return 创建鐨勮褰旾D
     */
    Long createInspectionRecord(AccessoryInspectionRecord record);

    /**
     * 鏍规嵁閰嶄欢ID查询璐ㄦ记录
     * @param accessoryId 閰嶄欢ID
     * @return 璐ㄦ记录鍒楄〃
     */
    List<AccessoryInspectionRecord> getByAccessoryId(Long accessoryId);

    /**
     * 鏍规嵁鎵规鍙锋煡璇㈣川妫€记录
     * @param batchNumber 鎵规鍙?     * @return 璐ㄦ记录鍒楄〃
     */
    List<AccessoryInspectionRecord> getByBatchNumber(String batchNumber);

    /**
     * 查询涓嶅悎鏍肩殑璐ㄦ记录
     * @return 涓嶅悎鏍艰川妫€记录鍒楄〃
     */
    List<AccessoryInspectionRecord> getUnqualifiedRecords();

    /**
     * 查询鎸囧畾鏃堕棿娈靛唴鐨勮川妫€记录
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 璐ㄦ记录鍒楄〃
     */
    List<AccessoryInspectionRecord> getByDateRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新璐ㄦ结果
     * @param id 记录ID
     * @param inspectionResult 鏂扮殑璐ㄦ结果
     * @return 鏄惁更新成功
     */
    boolean updateInspectionResult(Long id, Integer inspectionResult);

    /**
     * 获取璐ㄦ缁熻淇℃伅
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 璐ㄦ缁熻结果
     */
    Map<String, Object> getInspectionStats(LocalDateTime startTime, LocalDateTime endTime);
}
