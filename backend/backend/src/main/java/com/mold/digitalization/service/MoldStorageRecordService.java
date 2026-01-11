package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldStorageRecord;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * 妯″叿鍏ュ簱记录服务接口
 * 鎻愪緵妯″叿鍏ュ簱鐩稿叧鐨勪笟鍔℃搷浣? */
public interface MoldStorageRecordService extends IService<MoldStorageRecord> {
    
    /**
     * 鏍规嵁妯″叿ID查询鍏ュ簱记录
     * @param moldId 妯″叿ID
     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> getByMoldId(Long moldId);
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询鍏ュ簱记录
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> getByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 鏍规嵁鎵规鍙锋煡璇㈠叆搴撹褰?     * @param batchNumber 鎵规鍙?     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> getByBatchNumber(String batchNumber);
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询鍏ュ簱记录锛堝吋瀹笴ontroller鐨勫弬鏁板悕锛?     * @param startDate 开€濮嬫棩鏈?     * @param endDate 缁撴潫日期
     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> getByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 创建鏂扮殑鍏ュ簱记录
     * @param record 鍏ュ簱记录
     * @return 鏄惁创建成功
     */
    boolean createRecord(MoldStorageRecord record);
    
    /**
     * 更新鍏ュ簱记录状态     * @param id 记录ID
     * @param inspectionResult 璐ㄦ结果
     * @return 鏄惁更新成功
     */
    boolean updateInspectionResult(Long id, Integer inspectionResult);
    
    /**
     * 鏍规嵁璐ㄦ结果查询鍏ュ簱记录
     * @param inspectionResult 璐ㄦ结果
     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> getByInspectionResult(Integer inspectionResult);
    
    /**
     * 更新鍏ュ簱记录
     * @param record 鍏ュ簱记录
     * @return 鏄惁更新成功
     */
    boolean updateRecord(MoldStorageRecord record);
    
    /**
     * 删除鍏ュ簱记录
     * @param id 记录ID
     * @return 鏄惁删除成功
     */
    boolean deleteRecord(Long id);
    
    /**
     * 获取鍏ュ簱记录缁熻淇℃伅
     * @return 缁熻淇℃伅
     */
    Map<String, Object> getStorageStats();
    
    /**
     * 获取鎵€鏈夊叆搴撹褰?     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> getAllRecords();
}
