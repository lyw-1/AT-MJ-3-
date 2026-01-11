package com.mold.digitalization.service.audit.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mold.digitalization.mapper.OperationLogMapper;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.audit.AuditAnalysisService;
import com.mold.digitalization.service.system.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 审计分析服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuditAnalysisServiceImpl implements AuditAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(AuditAnalysisServiceImpl.class);

    private final OperationLogService operationLogService;
    private final OperationLogMapper operationLogMapper;

    @Override
    public Map<String, Object> analyzeUserBehavior(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数验证
            if (userId == null || startTime == null || endTime == null) {
                throw new IllegalArgumentException("参数不能为空: userId=" + userId + ", startTime=" + startTime + ", endTime=" + endTime);
            }
            
            // 构建查询条件
            QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userId", userId)
                    .between("operationTime", startTime, endTime);
            
            List<OperationLog> logs = operationLogService.list(queryWrapper);
            
            // 分析操作类型分布
            Map<String, Long> typeDistribution = logs.stream()
                    .collect(Collectors.groupingBy(OperationLog::getOperationType, Collectors.counting()));
            
        // Analyze operation time distribution (by hour)
        Map<Integer, Long> timeDistribution = logs.stream()
            .collect(Collectors.groupingBy(log -> log.getOperationTime().getHour(), Collectors.counting()));
            
            // Calculate success statistics
            long total = logs.size();
            long successCount = logs.stream().filter(log -> "200".equals(log.getResultCode())).count();
            double successRate = total > 0 ? (double) successCount / total * 100 : 0;
            
            // 找出最频繁操作
            Optional<Map.Entry<String, Long>> mostFrequentOperation = typeDistribution.entrySet().stream()
                    .max(Map.Entry.comparingByValue());
            
            // 找出敏感操作
            List<OperationLog> sensitiveOperations = logs.stream()
                    .filter(OperationLog::getIsSensitive)
                    .collect(Collectors.toList());
            
            // 组装结果
            result.put("totalOperations", (long) total);
            result.put("successRate", String.format("%.2f%%", successRate));
            result.put("operationTypeDistribution", typeDistribution);
            result.put("timeDistribution", timeDistribution);
            result.put("mostFrequentOperation", mostFrequentOperation.map(Map.Entry::getKey).orElse("N/A"));
            result.put("sensitiveOperationCount", (long) sensitiveOperations.size());
            result.put("sensitiveOperations", sensitiveOperations);
            
        } catch (Exception e) {
            logger.error("分析用户行为失败: {}", e.getMessage(), e);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    public List<OperationLog> detectAbnormalOperations(LocalDateTime startTime, LocalDateTime endTime) {
        final List<OperationLog> abnormalOperations = new ArrayList<>();
        
        try {
            // Query logs within the time range
            QueryWrapper<OperationLog> queryWrapper = Wrappers.query();
            queryWrapper.between("operationTime", startTime, endTime);
            List<OperationLog> allLogs = operationLogService.list(queryWrapper);
            
        // 1. Failed sensitive operations
        List<OperationLog> failedSensitive = allLogs.stream()
            .filter(log -> log.getIsSensitive() && !"200".equals(log.getResultCode()))
            .collect(Collectors.toList());
            abnormalOperations.addAll(failedSensitive);
            
            // 2. 同一用户短时间内多次失败操作
            Map<String, List<OperationLog>> userLogs = allLogs.stream()
                    .filter(log -> !"200".equals(log.getResultCode()))
                    .collect(Collectors.groupingBy(OperationLog::getUsername));
            
            userLogs.forEach((username, userLogsList) -> {
                // 创建日志的副本进行排序，避免修改原始列表
                List<OperationLog> sortedLogs = new ArrayList<>(userLogsList);
                sortedLogs.sort(Comparator.comparing(OperationLog::getOperationTime));
                
                // 检测短时间内多次失败（5分钟内3次以上失败）
                for (int i = 0; i < sortedLogs.size() - 2; i++) {
                    LocalDateTime firstTime = sortedLogs.get(i).getOperationTime();
                    LocalDateTime thirdTime = sortedLogs.get(i + 2).getOperationTime();
                    
                    if (thirdTime.minusMinutes(5).isBefore(firstTime)) {
                        abnormalOperations.addAll(sortedLogs.subList(i, i + 3));
                    }
                }
            });
            
            // 3. 非正常时间的敏感操作（例如凌晨2-5点）
            List<OperationLog> unusualTimeSensitive = allLogs.stream()
                    .filter(log -> log.getIsSensitive())
                    .filter(log -> {
                        int hour = log.getOperationTime().getHour();
                        return hour >= 2 && hour <= 5;
                    })
                    .collect(Collectors.toList());
            abnormalOperations.addAll(unusualTimeSensitive);
            
            // 去重
            List<OperationLog> distinctAbnormalOperations = abnormalOperations.stream()
                    .distinct()
                    .sorted(Comparator.comparing(OperationLog::getOperationTime).reversed())
                    .collect(Collectors.toList());
            
            return distinctAbnormalOperations;
        } catch (Exception e) {
            logger.error("检测异常操作失败: {}", e.getMessage(), e);
            return abnormalOperations;
        }
    }

    @Override
    public List<Map<String, Object>> analyzeOperationTrend(String dimension, LocalDateTime startTime, LocalDateTime endTime) {
        final List<Map<String, Object>> trendData = new ArrayList<>();
        
        try {
            // Query logs within the time range
            QueryWrapper<OperationLog> queryWrapper = Wrappers.query();
            queryWrapper.between("operationTime", startTime, endTime)
                       .orderByAsc("operationTime");
            List<OperationLog> logs = operationLogService.list(queryWrapper);
            
            // Grouping variable for logs by time label
            Map<String, List<OperationLog>> groupedLogs;
            DateTimeFormatter formatter;
            
            switch (dimension) {
                case "day":
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    groupedLogs = logs.stream()
                            .collect(Collectors.groupingBy(log -> log.getOperationTime().format(formatter)));
                    break;
                case "week":
                    formatter = DateTimeFormatter.ofPattern("yyyy-'W'ww");
                    groupedLogs = logs.stream()
                            .collect(Collectors.groupingBy(log -> log.getOperationTime().format(formatter)));
                    break;
                case "month":
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                    groupedLogs = logs.stream()
                            .collect(Collectors.groupingBy(log -> log.getOperationTime().format(formatter)));
                    break;
                default:
                    throw new IllegalArgumentException("不支持的维度: " + dimension);
            }
            
            // 生成趋势数据
            groupedLogs.forEach((timeLabel, groupedTimeLogs) -> {
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("timeLabel", timeLabel);
                dataPoint.put("totalCount", groupedTimeLogs.size());
                dataPoint.put("successCount", groupedTimeLogs.stream().filter(log -> "200".equals(log.getResultCode())).count());
                dataPoint.put("failCount", groupedTimeLogs.stream().filter(log -> !"200".equals(log.getResultCode())).count());
                dataPoint.put("sensitiveCount", groupedTimeLogs.stream().filter(OperationLog::getIsSensitive).count());
                trendData.add(dataPoint);
            });
            
            // 按时间标签排序趋势数据
            trendData.sort(Comparator.comparing(obj -> (String) obj.get("timeLabel")));
            
        } catch (Exception e) {
            logger.error("鍒嗘瀽操作瓒嬪娍失败: {}", e.getMessage(), e);
        }
        
        return trendData;
    }

    @Override
    public OperationLogStatisticDTO statisticSensitiveOperations(OperationLogQueryDTO queryDTO) {
        OperationLogStatisticDTO statisticDTO = new OperationLogStatisticDTO();
        
        try {
            // 设置查询鏉′欢
            queryDTO.setIsSensitive(true);
            
            // 查询鏁忔劅操作
            List<OperationLog> sensitiveLogs = operationLogMapper.queryByTimeRange(
                    queryDTO.getStartTime(), 
                    queryDTO.getEndTime()
            );
            
            // 杩囨护鍑烘晱鎰熸搷浣?
            sensitiveLogs = sensitiveLogs.stream()
                    .filter(OperationLog::getIsSensitive)
                    .collect(Collectors.toList());
            
            // 缁熻鎬绘暟
            long totalCount = sensitiveLogs.size();
            statisticDTO.setTotalCount(totalCount);
            
            // 缁熻成功鍜屽け璐ユ暟閲?
            long successCount = sensitiveLogs.stream().filter(log -> "200".equals(log.getResultCode())).count();
            long failCount = totalCount - successCount;
            
            statisticDTO.setSuccessCount(successCount);
            statisticDTO.setFailCount(failCount);
            statisticDTO.setSensitiveCount(totalCount);
            
            // Calculate success rate
            if (totalCount > 0) {
                statisticDTO.setSuccessRate(String.format("%.2f%%", (double) successCount / totalCount * 100));
                statisticDTO.setSensitiveRate("100.00%"); // 鍥犱负閮芥槸鏁忔劅操作
            } else {
                statisticDTO.setSuccessRate("0.00%");
                statisticDTO.setSensitiveRate("0.00%");
            }
            
            // 缁熻鏁忔劅操作绫诲瀷鍒嗗竷
            Map<String, Long> typeDistribution = sensitiveLogs.stream()
                    .collect(Collectors.groupingBy(OperationLog::getOperationType, Collectors.counting()));
            statisticDTO.setDetail(typeDistribution);
            
        } catch (Exception e) {
            logger.error("缁熻鏁忔劅操作失败: {}", e.getMessage(), e);
            // 开傚父鎯呭喌涓嬭缃粯璁ゅ€?
            statisticDTO.setTotalCount(0L);
            statisticDTO.setSuccessCount(0L);
            statisticDTO.setFailCount(0L);
            statisticDTO.setSensitiveCount(0L);
            statisticDTO.setSuccessRate("0.00%");
            statisticDTO.setSensitiveRate("0.00%");
            statisticDTO.setDetail(new HashMap<>());
        }
        
        return statisticDTO;
    }

    @Override
    public List<Map<String, Object>> getTopNOperations(int topN, LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> topOperations = new ArrayList<>();
        
        try {
            // 查询鏃堕棿鑼冨洿鍐呯殑鎵€鏈夋搷浣?
            QueryWrapper<OperationLog> queryWrapper = Wrappers.query();
            queryWrapper.between("create_time", startTime, endTime);
            List<OperationLog> logs = operationLogService.list(queryWrapper);
            
            // 鎸夋搷浣滅被鍨嬪垎缁勭粺璁?
            Map<String, Long> operationCount = logs.stream()
                    .collect(Collectors.groupingBy(OperationLog::getOperationType, Collectors.counting()));
            
            // 鎺掑簭骞跺彇鍓峃涓?
            operationCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(topN)
                    .forEach(entry -> {
                        Map<String, Object> operationInfo = new HashMap<>();
                        operationInfo.put("operationType", entry.getKey());
                        operationInfo.put("count", entry.getValue());
                        
                        // 璁＄畻璇ユ搷浣滅殑成功鐜?
                        long typeTotal = entry.getValue();
                        long typeSuccess = logs.stream()
                                .filter(log -> log.getOperationType().equals(entry.getKey()) && "200".equals(log.getResultCode()))
                                .count();
                        operationInfo.put("percentage", (double) typeSuccess / typeTotal * 100);
                        
                        topOperations.add(operationInfo);
                    });
            
        } catch (Exception e) {
            logger.error("获取楂橀操作失败: {}", e.getMessage(), e);
        }
        
        return topOperations;
    }

    @Override
    public List<Map<String, Object>> analyzeSuccessRate(LocalDateTime startTime, LocalDateTime endTime, String groupBy) {
        final List<Map<String, Object>> successRateData = new ArrayList<>();
        
        try {
            // 查询鏃堕棿鑼冨洿鍐呯殑鎵€鏈夋搷浣?
            QueryWrapper<OperationLog> queryWrapper = Wrappers.query();
            queryWrapper.between("create_time", startTime, endTime);
            List<OperationLog> logs = operationLogService.list(queryWrapper);
            
            // Grouping variable
            Map<String, List<OperationLog>> groupedLogs;
            
            switch (groupBy) {
                case "operationType":
                    groupedLogs = logs.stream()
                            .collect(Collectors.groupingBy(OperationLog::getOperationType));
                    break;
                case "user":
                    groupedLogs = logs.stream()
                            .collect(Collectors.groupingBy(OperationLog::getUsername));
                    break;
                case "module":
                    // 绠€鍗曞鐞嗭紝浠巓perationContent涓彁鍙栨ā鍧椾俊鎭?
                    groupedLogs = logs.stream()
                            .collect(Collectors.groupingBy(log -> {
                                String content = log.getOperationContent();
                                // 杩欓噷闇€瑕佹牴鎹疄闄呯殑content鏍煎紡鎻愬彇妯″潡淇℃伅
                                // 鍋囪content鍖呭惈module瀛楁
                                if (content.contains("module")) {
                                    int start = content.indexOf("module") + 7;
                                    int end = content.indexOf(",", start);
                                    if (end > start) {
                                        return content.substring(start, end).trim().replace("\"", "");
                                    }
                                }
                                return "鏈煡妯″潡";
                            }));
                    break;
                default:
                    throw new IllegalArgumentException("涓嶆敮鎸佺殑鍒嗙粍瀛楁: " + groupBy);
            }
            
            // 璁＄畻姣忎釜鍒嗙粍鐨勬垚鍔熺巼
            groupedLogs.forEach((groupKey, groupLogs) -> {
                Map<String, Object> rateInfo = new HashMap<>();
                rateInfo.put("groupKey", groupKey);
                
                long total = groupLogs.size();
                long success = groupLogs.stream().filter(log -> "200".equals(log.getResultCode())).count();
                double rate = total > 0 ? (double) success / total * 100 : 0;
                
                rateInfo.put("totalCount", total);
                rateInfo.put("successCount", success);
                rateInfo.put("successRate", String.format("%.2f%%", rate));
                
                successRateData.add(rateInfo);
            });
            
            // 鎸夋垚鍔熺巼鎺掑簭
            successRateData.sort((a, b) -> {
                double rateA = Double.parseDouble(((String) a.get("successRate")).replace("%", ""));
                double rateB = Double.parseDouble(((String) b.get("successRate")).replace("%", ""));
                return Double.compare(rateB, rateA); // 闄嶅簭鎺掑垪
            });
            
        } catch (Exception e) {
            logger.error("鍒嗘瀽操作成功鐜囧け璐? {}", e.getMessage(), e);
        }
        
        return successRateData;
    }

    @Override
    public Map<String, Object> generateAuditReport(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // 鍩虹缁熻
            QueryWrapper<OperationLog> queryWrapper = Wrappers.query();
            queryWrapper.between("create_time", startTime, endTime);
            List<OperationLog> allLogs = operationLogService.list(queryWrapper);
            
            long totalOperations = allLogs.size();
            long successOperations = allLogs.stream().filter(log -> "200".equals(log.getResultCode())).count();
            long sensitiveOperations = allLogs.stream().filter(OperationLog::getIsSensitive).count();
            long failedOperations = totalOperations - successOperations;
            
            double overallSuccessRate = totalOperations > 0 ? (double) successOperations / totalOperations * 100 : 0;
            
            // 鎽樿淇℃伅锛堝搴攕ummary锛?
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalOperations", totalOperations);
            summary.put("totalUsers", allLogs.stream().map(OperationLog::getUserId).distinct().count());
            summary.put("timeRange", startTime + " 鑷?" + endTime);
            
            // User behavior statistics
            Map<String, Object> userBehavior = new HashMap<>();
            userBehavior.put("totalUsers", allLogs.stream().map(OperationLog::getUserId).distinct().count());
            userBehavior.put("topActiveUsers", allLogs.stream()
                    .collect(Collectors.groupingBy(OperationLog::getUsername, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(entry -> {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("username", entry.getKey());
                        userInfo.put("operationCount", entry.getValue());
                        return userInfo;
                    })
                    .collect(Collectors.toList()));
            
            // 开傚父操作
            List<OperationLog> abnormalOperations = detectAbnormalOperations(startTime, endTime);
            
            // Operation trend data
            List<Map<String, Object>> operationTrend = analyzeOperationTrend("day", startTime, endTime);
            
            // 鏁忔劅操作缁熻
            Map<String, Object> sensitiveOperationsStats = new HashMap<>();
            sensitiveOperationsStats.put("totalCount", sensitiveOperations);
            sensitiveOperationsStats.put("successCount", allLogs.stream()
                    .filter(OperationLog::getIsSensitive)
                    .filter(log -> "200".equals(log.getResultCode()))
                    .count());
            sensitiveOperationsStats.put("failCount", allLogs.stream()
                    .filter(OperationLog::getIsSensitive)
                    .filter(log -> !"200".equals(log.getResultCode()))
                    .count());
            
            // 楂橀操作TOP 5
            List<Map<String, Object>> topOperations = getTopNOperations(5, startTime, endTime);
            
            // Success rate summary
            Map<String, Object> successRate = new HashMap<>();
            successRate.put("overallRate", String.format("%.2f%%", overallSuccessRate));
            successRate.put("successCount", successOperations);
            successRate.put("failCount", failedOperations);
            successRate.put("totalCount", totalOperations);
            
            // 缁勮鎶ュ憡
            report.put("summary", summary);
            report.put("userBehavior", userBehavior);
            report.put("abnormalOperations", abnormalOperations);
            report.put("operationTrend", operationTrend);
            report.put("sensitiveOperations", sensitiveOperationsStats);
            report.put("topOperations", topOperations);
            report.put("successRate", successRate);
            report.put("generatedTime", LocalDateTime.now());
            
        } catch (Exception e) {
            logger.error("鐢熸垚瀹¤鎶ュ憡失败: {}", e.getMessage(), e);
            report.put("error", e.getMessage());
        }
        
        return report;
    }
}
