package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldStorageRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 妯″叿鍏ュ簱记录Mapper接口
 * 缁ф壙BaseMapper骞舵彁渚涜嚜瀹氫箟查询方法
 */
public interface MoldStorageRecordMapper extends BaseMapper<MoldStorageRecord> {

    /**
     * 鏍规嵁妯″叿ID查询鍏ュ簱记录
     * @param moldId 妯″叿ID
     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> selectByMoldId(@Param("moldId") Long moldId);

    /**
     * 鏍规嵁鎵规鍙锋煡璇㈠叆搴撹褰?     * @param batchNumber 鎵规鍙?     * @return 鍏ュ簱记录
     */
    MoldStorageRecord selectByBatchNumber(@Param("batchNumber") String batchNumber);

    /**
     * 鏍规嵁鍏ュ簱日期鑼冨洿查询记录
     * @param startDate 开€濮嬫棩鏈?     * @param endDate 缁撴潫日期
     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> selectByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * 鏍规嵁璐ㄦ结果查询记录
     * @param inspectionResult 璐ㄦ结果锛?-寰呰川妫€锛?-鍚堟牸锛?-涓嶅悎鏍?     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> selectByInspectionResult(@Param("inspectionResult") Integer inspectionResult);

    /**
     * 更新璐ㄦ结果
     * @param id 记录ID
     * @param inspectionResult 璐ㄦ结果
     * @return 更新结果
     */
    int updateInspectionResult(@Param("id") Long id, @Param("inspectionResult") Integer inspectionResult);

    /**
     * 鏍规嵁操作鍛業D查询鍏ュ簱记录
     * @param operatorId 操作鍛業D
     * @return 鍏ュ簱记录鍒楄〃
     */
    List<MoldStorageRecord> selectByOperatorId(@Param("operatorId") Long operatorId);
}
