package com.mold.digitalization.service;

import com.mold.digitalization.entity.ToolingMaintenance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 妯″叿缁翠慨淇濆吇记录Service接口
 * 鎻愪緵妯″叿缁翠慨淇濆吇鐩稿叧鐨勪笟鍔￠€昏緫
 */
public interface ToolingMaintenanceService extends IService<ToolingMaintenance> {

    /**
     * 创建缁翠慨淇濆吇记录
     * @param maintenance 缁翠慨淇濆吇记录实体
     * @return 创建鐨勮褰旾D
     */
    Long createMaintenance(ToolingMaintenance maintenance);

    /**
     * 鏍规嵁妯″叿ID查询缁翠慨淇濆吇记录
     * @param moldId 妯″叿ID
     * @return 缁翠慨淇濆吇记录鍒楄〃
     */
    List<ToolingMaintenance> getByMoldId(Long moldId);

    /**
     * 鏍规嵁鐘舵€佹煡璇㈢淮淇繚鍏昏褰?     * @param status 缁存姢状态     * @return 缁翠慨淇濆吇记录鍒楄〃
     */
    List<ToolingMaintenance> getByStatus(Integer status);

    /**
     * 查询鎸囧畾鏃堕棿娈靛唴鐨勭淮淇繚鍏昏褰?     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 缁翠慨淇濆吇记录鍒楄〃
     */
    List<ToolingMaintenance> getByDateRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新缁存姢记录状态     * @param id 记录ID
     * @param status 鏂扮姸鎬?     * @return 鏄惁更新成功
     */
    boolean updateMaintenanceStatus(Long id, Integer status);

    /**
     * 缁熻鍚勭淮鎶ょ被鍨嬬殑鏁伴噺
     * @return 缁存姢绫诲瀷缁熻结果
     */
    List<Map<String, Object>> getMaintenanceTypeStats();

    /**
     * 获取缁存姢璐圭敤缁熻
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 璐圭敤缁熻结果
     */
    Map<String, Object> getMaintenanceCostStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新缁存姢结果
     * @param id 记录ID
     * @param result 缁存姢结果
     * @param nextMaintenanceTime 涓嬫缁存姢鏃堕棿
     * @return 鏄惁更新成功
     */
    boolean updateMaintenanceResult(Long id, String result, String nextMaintenanceTime);
}
