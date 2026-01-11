package com.mold.digitalization.mapper;

import com.mold.digitalization.entity.AccessoryInspectionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢璐ㄦ记录Mapper接口
 * 鎻愪緵瀵筧ccessory_inspection_record琛ㄧ殑数据搴撴搷浣? */
@Mapper
public interface AccessoryInspectionRecordMapper extends BaseMapper<AccessoryInspectionRecord> {

    /**
     * 鏍规嵁閰嶄欢ID查询璐ㄦ记录
     * @param accessoryId 閰嶄欢ID
     * @return 璐ㄦ记录鍒楄〃
     */
    List<AccessoryInspectionRecord> selectByAccessoryId(Long accessoryId);

    /**
     * 鏍规嵁鎵规鍙锋煡璇㈣川妫€记录
     * @param batchNumber 鎵规鍙?     * @return 璐ㄦ记录鍒楄〃
     */
    List<AccessoryInspectionRecord> selectByBatchNumber(String batchNumber);

    /**
     * 鏍规嵁璐ㄦ结果查询记录
     * @param inspectionResult 璐ㄦ结果
     * @return 璐ㄦ记录鍒楄〃
     */
    List<AccessoryInspectionRecord> selectByInspectionResult(Integer inspectionResult);

    /**
     * 查询鎸囧畾鏃堕棿娈靛唴鐨勮川妫€记录
     * @param params 鍖呭惈开€濮嬫椂闂村拰缁撴潫鏃堕棿鐨勫弬鏁癿ap
     * @return 璐ㄦ记录鍒楄〃
     */
    List<AccessoryInspectionRecord> selectByDateRange(Map<String, Object> params);

    /**
     * 缁熻鍚勬楠岄」鐩殑鍚堟牸鎯呭喌
     * @return 鍖呭惈妫€楠岄」鐩拰缁熻结果鐨刴ap鍒楄〃
     */
    List<Map<String, Object>> countByInspectionItem();
}
