package com.mold.digitalization.mapper;

import com.mold.digitalization.entity.ToolingMaintenance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 妯″叿缁翠慨淇濆吇记录Mapper接口
 * 鎻愪緵瀵箃ooling_maintenance琛ㄧ殑数据搴撴搷浣? */
@Mapper
public interface ToolingMaintenanceMapper extends BaseMapper<ToolingMaintenance> {

    /**
     * 鏍规嵁妯″叿ID查询缁翠慨淇濆吇记录
     * @param moldId 妯″叿ID
     * @return 缁翠慨淇濆吇记录鍒楄〃
     */
    List<ToolingMaintenance> selectByMoldId(Long moldId);

    /**
     * 鏍规嵁鐘舵€佹煡璇㈢淮淇繚鍏昏褰?     * @param status 缁存姢状态     * @return 缁翠慨淇濆吇记录鍒楄〃
     */
    List<ToolingMaintenance> selectByStatus(Integer status);

    /**
     * 查询鎸囧畾鏃堕棿娈靛唴鐨勭淮淇繚鍏昏褰?     * @param params 鍖呭惈开€濮嬫椂闂村拰缁撴潫鏃堕棿鐨勫弬鏁癿ap
     * @return 缁翠慨淇濆吇记录鍒楄〃
     */
    List<ToolingMaintenance> selectByDateRange(Map<String, Object> params);

    /**
     * 缁熻鍚勭淮鎶ょ被鍨嬬殑鏁伴噺
     * @return 鍖呭惈缁存姢绫诲瀷鍜屾暟閲忕殑缁熻结果
     */
    List<Map<String, Object>> countByMaintenanceType();
}