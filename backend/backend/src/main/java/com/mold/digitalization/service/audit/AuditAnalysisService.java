package com.mold.digitalization.service.audit;

import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 审计分析服务接口
 * 提供高级的操作日志分析功能
 */
public interface AuditAnalysisService {

    /**
     * 分析用户行为模式
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 行为模式分析结果
     */
    Map<String, Object> analyzeUserBehavior(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 检测异常操作
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 异常操作列表
     */
    List<OperationLog> detectAbnormalOperations(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分析操作趋势
     * @param dimension 分析维度（day/week/month）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 趋势数据
     */
    List<Map<String, Object>> analyzeOperationTrend(String dimension, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计敏感操作详情
     * @param queryDTO 查询条件
     * @return 敏感操作统计结果
     */
    OperationLogStatisticDTO statisticSensitiveOperations(OperationLogQueryDTO queryDTO);

    /**
     * 获取高频操作TOP N
     * @param topN 数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 高频操作列表
     */
    List<Map<String, Object>> getTopNOperations(int topN, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分析操作成功率
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组字段（operationType/user/module）
     * @return 成功率统计
     */
    List<Map<String, Object>> analyzeSuccessRate(LocalDateTime startTime, LocalDateTime endTime, String groupBy);

    /**
     * 生成审计报告数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 审计报告数据
     */
    Map<String, Object> generateAuditReport(LocalDateTime startTime, LocalDateTime endTime);
}
