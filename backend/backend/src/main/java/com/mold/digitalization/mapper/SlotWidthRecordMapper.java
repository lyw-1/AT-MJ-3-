package com.mold.digitalization.mapper;

import com.mold.digitalization.entity.SlotWidthRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 妲藉记录Mapper接口
 * 鎻愪緵瀵箂lot_width_record琛ㄧ殑数据搴撴搷浣? */
@Mapper
public interface SlotWidthRecordMapper extends BaseMapper<SlotWidthRecord> {

    /**
     * 鏍规嵁妯″叿ID查询妲藉记录
     * @param moldId 妯″叿ID
     * @return 妲藉记录鍒楄〃
     */
    List<SlotWidthRecord> selectByMoldId(Long moldId);

    /**
     * 查询涓嶅悎鏍肩殑妲藉记录
     * @return 涓嶅悎鏍兼Ы瀹借褰曞垪琛?     */
    List<SlotWidthRecord> selectUnqualifiedRecords();

    /**
     * 查询鎸囧畾鏃堕棿娈靛唴鐨勬Ы瀹借褰?     * @param params 鍖呭惈开€濮嬫椂闂村拰缁撴潫鏃堕棿鐨勫弬鏁癿ap
     * @return 妲藉记录鍒楄〃
     */
    List<SlotWidthRecord> selectByDateRange(Map<String, Object> params);

    /**
     * 获取妯″叿鍚勬Ы浣嶇殑鏈€鏂版祴閲忚褰?     * @param moldId 妯″叿ID
     * @return 鍚勬Ы浣嶆渶鏂版祴閲忚褰曞垪琛?     */
    List<SlotWidthRecord> selectLatestByMold(Long moldId);

    /**
     * 缁熻妲藉记录鐨勫悎鏍兼儏鍐?     * @return 鍖呭惈鍚堟牸缁熻淇℃伅鐨刴ap鍒楄〃
     */
    List<Map<String, Object>> countQualificationStats();
}
