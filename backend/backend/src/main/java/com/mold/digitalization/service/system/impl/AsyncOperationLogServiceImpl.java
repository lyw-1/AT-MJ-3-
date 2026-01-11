package com.mold.digitalization.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.mapper.OperationLogMapper;
import com.mold.digitalization.service.system.AsyncOperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异步操作日志服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncOperationLogServiceImpl implements AsyncOperationLogService {
    
    private final OperationLogMapper operationLogMapper;
    
    /**
     * 默认批量处理大小
     */
    private static final int DEFAULT_BATCH_SIZE = 1000;

    /**
     * 日期时间格式化器
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveOperationLogsAsync(List<OperationLog> operationLogs) {
        if (operationLogs == null || operationLogs.isEmpty()) {
            return;
        }
        
        log.info("开始异步批量保存操作日志，数量：{}", operationLogs.size());
        
        try {
            // 分批保存，避免单批数据过大
            for (int i = 0; i < operationLogs.size(); i += DEFAULT_BATCH_SIZE) {
                int end = Math.min(i + DEFAULT_BATCH_SIZE, operationLogs.size());
                List<OperationLog> batchList = operationLogs.subList(i, end);
                
                // 使用MyBatis Plus的批量插入方法
                int insertCount = 0;
                for (OperationLog log : batchList) {
                    operationLogMapper.insert(log);
                    insertCount++;
                }
                log.info("批量保存操作日志批次完成，处理数量：{}, 成功插入：{}", batchList.size(), insertCount);
            }
            
            log.info("异步批量保存操作日志完成，总数量：{}", operationLogs.size());
        } catch (Exception e) {
            log.error("异步批量保存操作日志失败", e);
            throw e;
        }
    }

    @Override
    public int batchCleanOldLogsAsync(LocalDateTime cutoffTime, int batchSize, String sensitiveLevelThreshold) {
        if (cutoffTime == null) {
            return 0;
        }
        
        // 如果没有指定批量大小，使用默认值
        if (batchSize <= 0) {
            batchSize = DEFAULT_BATCH_SIZE;
        }
        
        log.info("开始异步分批清理历史日志，截止时间：{}, 批量大小：{}, 敏感级别阈值：{}", 
                cutoffTime.format(DATE_TIME_FORMATTER), batchSize, sensitiveLevelThreshold);
        
        int totalDeletedCount = 0;
        int batchCount = 0;
        
        try {
            while (true) {
                // 创建查询条件
                QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
                queryWrapper.lt("create_time", cutoffTime);
                
                // 添加敏感级别过滤条件
                if (sensitiveLevelThreshold != null && !sensitiveLevelThreshold.isEmpty()) {
                    // 保留高于阈值的敏感日志
                    queryWrapper.ne("sensitive_level", sensitiveLevelThreshold);
                    // 可以根据实际的敏感级别定义进行更复杂的过滤
                }
                
                // 查询要删除的数据ID
                queryWrapper.select("id");
                queryWrapper.last("LIMIT " + batchSize);
                
                List<Object> objIds = operationLogMapper.selectObjs(queryWrapper);
                List<Long> ids = objIds.stream()
                    .map(obj -> (Long) obj)
                    .collect(java.util.stream.Collectors.toList());
                
                // 如果没有更多数据，退出循环
                if (ids.isEmpty()) {
                    break;
                }
                
                // 批量删除
                int deletedCount = operationLogMapper.deleteBatchIds(ids);
                totalDeletedCount += deletedCount;
                batchCount++;
                
                log.info("日志清理批次 {} 完成，删除记录数：{}", batchCount, deletedCount);
                
                // 每处理10个批次后短暂休眠，避免长时间占用数据库连接
                if (batchCount % 10 == 0) {
                    Thread.sleep(100);
                }
            }
            
            log.info("异步分批清理历史日志完成，总计删除记录数：{}", totalDeletedCount);
            return totalDeletedCount;
        } catch (Exception e) {
            log.error("异步分批清理历史日志失败，已删除 {} 条记录，错误信息: {}", totalDeletedCount, e.getMessage());
            return totalDeletedCount; // 返回已删除的数量
        }
    }

    @Override
    public String asyncExportOperationLogs(Object queryParams, String exportFilePath) {
        log.info("开始异步导出操作日志，文件路径：{}", exportFilePath);
        
        try {
            // 确保导出目录存在
            Files.createDirectories(Paths.get(exportFilePath).getParent());
            
            // 创建查询条件
            QueryWrapper<OperationLog> queryWrapper = buildQueryWrapper(queryParams);
            
            // 分批查询并导出
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFilePath))) {
                // 写入CSV表头
                writer.write("id,username,userId,ip,operationType,operationContent,operationDesc,isSensitive,sensitiveLevel,createTime,result");
                writer.newLine();
                
                int pageNum = 1;
                int pageSize = 1000; // 每次查询1000条
                
                while (true) {
                    // 计算偏移量
                    int offset = (pageNum - 1) * pageSize;
                    queryWrapper.last("LIMIT " + offset + ", " + pageSize);
                    
                    List<OperationLog> logs = operationLogMapper.selectList(queryWrapper);
                    
                    if (logs.isEmpty()) {
                        break;
                    }
                    
                    // 写入数据
                    for (OperationLog log : logs) {
                        writer.write(formatLogToCsv(log));
                        writer.newLine();
                    }
                    
                    pageNum++;
                    // 每导出页后短暂休眠，避免长时间占用数据库
                    if (pageNum % 5 == 0) {
                        Thread.sleep(50);
                    }
                }
            }
            
            log.info("异步导出操作日志完成，文件路径：{}", exportFilePath);
            return exportFilePath;
        } catch (Exception e) {
            log.error("异步导出操作日志失败", e);
            // 导出失败时返回null
            return null;
        }
    }
    
    /**
     * 根据查询参数构建查询条件
     */
    private QueryWrapper<OperationLog> buildQueryWrapper(Object queryParams) {
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        
        // 如果queryParams是Map类型，直接使用
        if (queryParams instanceof Map) {
            Map<String, Object> params = (Map<String, Object>) queryParams;
            
            // 添加常见查询条件
            if (params.containsKey("username")) {
                queryWrapper.eq("username", params.get("username"));
            }
            if (params.containsKey("operationType")) {
                queryWrapper.eq("operation_type", params.get("operationType"));
            }
            if (params.containsKey("startTime")) {
                queryWrapper.ge("create_time", params.get("startTime"));
            }
            if (params.containsKey("endTime")) {
                queryWrapper.le("create_time", params.get("endTime"));
            }
            if (params.containsKey("isSensitive")) {
                queryWrapper.eq("is_sensitive", params.get("isSensitive"));
            }
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc("create_time");
        
        return queryWrapper;
    }
    
    /**
     * 将操作日志格式化为CSV格式
     */
    private String formatLogToCsv(OperationLog log) {
        StringBuilder sb = new StringBuilder();
        sb.append(log.getId()).append(",")
          .append(escapeCsvField(log.getUsername())).append(",")
          .append(log.getUserId()).append(",")
          .append(escapeCsvField(log.getUserIp())).append(",")
          .append(escapeCsvField(log.getOperationType())).append(",")
          .append(escapeCsvField(log.getOperationContent())).append(",")
          .append(escapeCsvField(log.getOperationDesc())).append(",")
          .append(log.getIsSensitive()).append(",")
          .append(escapeCsvField(log.getSensitiveLevel())).append(",")
          .append(log.getOperationTime() != null ? log.getOperationTime().format(DATE_TIME_FORMATTER) : "").append(",")
          .append(escapeCsvField(log.getResultCode())).append(",")
          .append(""); // errorMsg字段不存在，留空
        return sb.toString();
    }
    
    /**
     * 转义CSV字段，处理包含逗号或引号的情况
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        // 如果字段包含逗号、引号或换行符，需要用引号包裹并转义内部引号
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
