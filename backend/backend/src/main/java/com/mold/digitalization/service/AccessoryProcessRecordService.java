package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.AccessoryProcessRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢鍔犲伐记录服务接口
 * 鎻愪緵閰嶄欢鍔犲伐鐩稿叧鐨勪笟鍔℃搷浣?
 */
public interface AccessoryProcessRecordService extends IService<AccessoryProcessRecord> {
    
    /**
     * 创建閰嶄欢鍔犲伐记录
     * @param record 鍔犲伐记录
     * @return 鏄惁创建成功
     */
    boolean createRecord(AccessoryProcessRecord record);
    
    /**
     * 鏍规嵁閰嶄欢ID查询鍔犲伐记录
     * @param accessoryId 閰嶄欢ID
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> getByAccessoryId(Long accessoryId);
    
    /**
     * 鏍规嵁妯″叿ID查询鐩稿叧閰嶄欢鍔犲伐记录
     * @param moldId 妯″叿ID
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> getByMoldId(Long moldId);
    
    /**
     * 鏍规嵁浠诲姟ID查询鍔犲伐记录
     * @param taskId 浠诲姟ID
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> getByTaskId(Long taskId);
    
    /**
     * 鏍规嵁鍔犲伐结果查询记录
     * @param processResult 鍔犲伐结果
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> getByProcessResult(Integer processResult);
    
    /**
     * 查询鎸囧畾鏃堕棿娈靛唴鐨勫姞宸ヨ锟?     * @param startTime 开€濮嬫椂锟?     * @param endTime 缁撴潫鏃堕棿
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> getByDateRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 更新鍔犲伐记录鐘讹拷?     * @param id 记录ID
     * @param status 鏂扮姸锟?     * @return 鏄惁更新成功
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 获取鍔犲伐记录缁熻淇℃伅
     * @param startTime 开€濮嬫椂锟?     * @param endTime 缁撴潫鏃堕棿
     * @return 缁熻结果
     */
    Map<String, Object> getProcessStats(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 缁熻鍚勫伐搴忕殑鍔犲伐鎯呭喌
     * @return 宸ュ簭缁熻结果
     */
    List<Map<String, Object>> getProcessNameStats();
}
