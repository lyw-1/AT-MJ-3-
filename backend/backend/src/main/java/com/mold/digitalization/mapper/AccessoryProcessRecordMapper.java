package com.mold.digitalization.mapper;

import com.mold.digitalization.entity.AccessoryProcessRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢鍔犲伐记录Mapper接口
 * 鎻愪緵瀵筧ccessory_process_record琛ㄧ殑数据搴撴搷浣? */
@Mapper
public interface AccessoryProcessRecordMapper extends BaseMapper<AccessoryProcessRecord> {

    /**
     * 鏍规嵁閰嶄欢ID查询鍔犲伐记录
     * @param accessoryId 閰嶄欢ID
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> selectByAccessoryId(Long accessoryId);

    /**
     * 鏍规嵁浠诲姟ID查询鍔犲伐记录
     * @param taskId 浠诲姟ID
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> selectByTaskId(Long taskId);

    /**
     * 鏍规嵁鍔犲伐结果查询记录
     * @param processResult 鍔犲伐结果
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> selectByProcessResult(Integer processResult);

    /**
     * 查询鎸囧畾鏃堕棿娈靛唴鐨勫姞宸ヨ褰?     * @param params 鍖呭惈开€濮嬫椂闂村拰缁撴潫鏃堕棿鐨勫弬鏁癿ap
     * @return 鍔犲伐记录鍒楄〃
     */
    List<AccessoryProcessRecord> selectByDateRange(Map<String, Object> params);

    /**
     * 缁熻鍚勫伐搴忕殑鍔犲伐鎯呭喌
     * @return 鍖呭惈宸ュ簭鍚嶇О鍜岀粺璁＄粨鏋滅殑map鍒楄〃
     */
    List<Map<String, Object>> countByProcessName();
}
