package com.mold.digitalization.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.mapper.OperationLogMapper;
import com.mold.digitalization.service.system.AsyncOperationLogService;
import com.mold.digitalization.service.system.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作鏃ュ織服务实现绫?
 *
 * @author mold
 * @date 2023-10-01
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    private final OperationLogMapper operationLogMapper;
    private final AsyncOperationLogService asyncOperationLogService;

    @Value("${mold.operation-log.sensitive-level-threshold:high}")
    private String sensitiveLevelThreshold;

    @Value("${mold.operation-log.async-threshold:100}")
    private int asyncThreshold;

    @Autowired
    public OperationLogServiceImpl(OperationLogMapper operationLogMapper, AsyncOperationLogService asyncOperationLogService) {
        this.operationLogMapper = operationLogMapper;
        this.asyncOperationLogService = asyncOperationLogService;
    }

    @Override
    public Page<OperationLog> queryByCondition(OperationLogQueryDTO queryDTO) {
        try {
            Page<OperationLog> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
            QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();

            // 鏋勫缓查询鏉′欢
            if (queryDTO.getUsername() != null && !queryDTO.getUsername().isEmpty()) {
                queryWrapper.like("username", queryDTO.getUsername());
            }
            if (queryDTO.getUserId() != null) {
                queryWrapper.eq("user_id", queryDTO.getUserId());
            }
            if (queryDTO.getOperationType() != null && !queryDTO.getOperationType().isEmpty()) {
                queryWrapper.eq("operation_type", queryDTO.getOperationType());
            }
            if (queryDTO.getOperationDesc() != null && !queryDTO.getOperationDesc().isEmpty()) {
                queryWrapper.like("operation_desc", queryDTO.getOperationDesc());
            }
            if (queryDTO.getResult() != null && !queryDTO.getResult().isEmpty()) {
                queryWrapper.eq("result", queryDTO.getResult());
            }
            // 暂不筛选 is_sensitive（表中尚无该列，避免SQL错误）
            if (queryDTO.getStartTime() != null) {
                queryWrapper.ge("create_time", queryDTO.getStartTime());
            }
            if (queryDTO.getEndTime() != null) {
                queryWrapper.le("create_time", queryDTO.getEndTime());
            }
            if (queryDTO.getIp() != null && !queryDTO.getIp().isEmpty()) {
                queryWrapper.like("ip", queryDTO.getIp());
            }

            // 鎸夊垱寤烘椂闂村€掑簭鎺掑簭
            queryWrapper.orderByDesc("create_time");

            return this.page(page, queryWrapper);
        } catch (Exception e) {
            logger.error("查询操作鏃ュ織鍒嗛〉失败: " + e.getMessage(), e);
            return new Page<>();
        }
    }

    @Override
    public Page<OperationLog> queryPage(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        // 创建鍒嗛〉瀵硅薄
        Page<OperationLog> page = new Page<>(pageNum, pageSize);
        
        // 创建查询鏉′欢
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        
        // 添加查询鏉′欢
        if (params.containsKey("username") && params.get("username") != null) {
            queryWrapper.like("username", params.get("username"));
        }
        if (params.containsKey("userId") && params.get("userId") != null) {
            queryWrapper.eq("user_id", params.get("userId"));
        }
        if (params.containsKey("operationType") && params.get("operationType") != null) {
            queryWrapper.eq("operation_type", params.get("operationType"));
        }
        if (params.containsKey("operationDesc") && params.get("operationDesc") != null) {
            queryWrapper.like("operation_desc", params.get("operationDesc"));
        }
        if (params.containsKey("result") && params.get("result") != null) {
            queryWrapper.eq("result", params.get("result"));
        }
        // 暂不筛选 is_sensitive（表中尚无该列，避免SQL错误）
        if (params.containsKey("startTime") && params.get("startTime") != null) {
            queryWrapper.ge("create_time", params.get("startTime"));
        }
        if (params.containsKey("endTime") && params.get("endTime") != null) {
            queryWrapper.le("create_time", params.get("endTime"));
        }
        if (params.containsKey("ip") && params.get("ip") != null) {
            queryWrapper.like("ip", params.get("ip"));
        }
        
        // 鎸夊垱寤烘椂闂村€掑簭鎺掑簭
        queryWrapper.orderByDesc("create_time");
        
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOperationLog(OperationLog operationLog) {
        try {
            // 妫€鏌ユ槸鍚︿负鏁忔劅操作
            if (operationLog.getIsSensitive() == null) {
                operationLog.setIsSensitive(isSensitiveOperation(operationLog.getOperationType()));
            }

            // 设置鏁忔劅绾у埆
            if (operationLog.getSensitiveLevel() == null || operationLog.getSensitiveLevel().isEmpty()) {
                operationLog.setSensitiveLevel(determineSensitiveLevel(operationLog));
            }

            return this.save(operationLog);
        } catch (Exception e) {
            logger.error("淇濆瓨操作鏃ュ織失败: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * 鍒ゆ柇鏄惁涓烘晱鎰熸搷浣?
     */
    private boolean isSensitiveOperation(String operationType) {
        if (operationType == null) {
            return false;
        }
        // 鏁忔劅操作绫诲瀷
        String[] sensitiveOperations = {
            "DELETE", "UPDATE", "RESET_PASSWORD", "EXPORT", "IMPORT", 
            "LOGIN", "LOGOUT", "GRANT", "REVOKE", "CONFIG_CHANGE"
        };
        
        for (String sensitiveOp : sensitiveOperations) {
            if (operationType.toUpperCase().contains(sensitiveOp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 纭畾鏁忔劅绾у埆
     */
    private String determineSensitiveLevel(OperationLog operationLog) {
        String operationType = operationLog.getOperationType();
        if (operationType == null) {
            return "normal";
        }
        
        String opType = operationType.toUpperCase();
        if (opType.contains("DELETE") || opType.contains("RESET_PASSWORD") || opType.contains("GRANT") || opType.contains("REVOKE")) {
            return "critical";
        } else if (opType.contains("UPDATE") || opType.contains("EXPORT") || opType.contains("IMPORT") || opType.contains("CONFIG_CHANGE")) {
            return "high";
        } else if (opType.contains("LOGIN") || opType.contains("LOGOUT")) {
            return "medium";
        } else {
            return "normal";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Long[] ids) {
        try {
            return this.removeByIds(java.util.Arrays.asList(ids));
        } catch (Exception e) {
            logger.error("鎵归噺删除操作鏃ュ織失败: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanOperationLogs() {
        try {
            QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
            return operationLogMapper.delete(queryWrapper) > 0;
        } catch (Exception e) {
            logger.error("娓呯┖操作鏃ュ織失败: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanOldLogs(LocalDateTime beforeTime) {
        try {
            // 记录娓呯悊浠诲姟开€濮?
            logger.info("开€濮嬫竻鐞嗘搷浣滄棩蹇楋紝娓呯悊鏃堕棿鐐癸細" + beforeTime);

            // 浣跨敤开傛鍒嗘壒娓呯悊锛岄伩鍏嶅ぇ数据閲忎笅鐨勯暱鏃堕棿浜嬪姟
            int deletedCount = asyncOperationLogService.batchCleanOldLogsAsync(beforeTime, 1000, sensitiveLevelThreshold);

            logger.info("Async cleanup completed, deletedCount=" + deletedCount);
            return deletedCount;
        } catch (Exception e) {
            logger.error("娓呯悊操作鏃ュ織失败: " + e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<OperationLogStatisticDTO> statisticByOperationType(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            return operationLogMapper.statisticByOperationType(startTime, endTime);
        } catch (Exception e) {
            logger.error("鎸夋搷浣滅被鍨嬬粺璁″け璐ワ細" + e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<OperationLogStatisticDTO> statisticByUser(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            return operationLogMapper.statisticByUser(startTime, endTime);
        } catch (Exception e) {
            logger.error("鎸夌敤鎴风粺璁″け璐ワ細" + e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<OperationLogStatisticDTO> statisticByModule(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            // 浠巓perationContent涓В鏋愭ā鍧椾俊鎭紝杩欓噷绠€鍗曞鐞嗭紝实际鍙兘闇€瑕佹洿澶嶆潅鐨勮В鏋愰€昏緫
            return operationLogMapper.statisticByModule(startTime, endTime);
        } catch (Exception e) {
            logger.error("鎸夋ā鍧楃粺璁″け璐ワ細" + e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public Page<OperationLog> querySensitiveOperations(OperationLogQueryDTO queryDTO) {
        // 设置isSensitive涓簍rue
        queryDTO.setIsSensitive(true);
        Page<OperationLog> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        return operationLogMapper.querySensitiveOperations(page, queryDTO);
    }

    @Override
    public List<OperationLog> queryBySensitiveLevel(String sensitiveLevel) {
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sensitive_level", sensitiveLevel);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<OperationLog> queryByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
            
            if (startTime != null) {
                queryWrapper.ge("create_time", startTime);
            }
            if (endTime != null) {
                queryWrapper.le("create_time", endTime);
            }
            
            queryWrapper.orderByDesc("create_time");
            return baseMapper.selectList(queryWrapper);
        } catch (Exception e) {
            logger.error("鎸夋椂闂磋寖鍥存煡璇㈡搷浣滄棩蹇楀け璐ワ細" + e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public String exportOperationLogs(OperationLogQueryDTO queryDTO) {
        // build export file path
        String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String exportFilePath = "/tmp/operation_logs_export_" + timestamp + ".csv";

        // convert DTO to params map
        Map<String, Object> queryParams = new HashMap<>();
        if (queryDTO.getUsername() != null) {
            queryParams.put("username", queryDTO.getUsername());
        }
        if (queryDTO.getOperationType() != null) {
            queryParams.put("operationType", queryDTO.getOperationType());
        }
        if (queryDTO.getStartTime() != null) {
            queryParams.put("startTime", queryDTO.getStartTime());
        }
        if (queryDTO.getEndTime() != null) {
            queryParams.put("endTime", queryDTO.getEndTime());
        }
        if (queryDTO.getIsSensitive() != null) {
            queryParams.put("isSensitive", queryDTO.getIsSensitive());
        }

        // use async service to export data
        asyncOperationLogService.asyncExportOperationLogs(queryParams, exportFilePath);

        logger.info("Triggered async export, path: " + exportFilePath);
        return exportFilePath; // return the export path
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanOldLogsByLevel(LocalDateTime normalBeforeTime, LocalDateTime criticalBeforeTime) {
        try {
            logger.info("cleanOldLogsByLevel requested: normalBeforeTime=" + normalBeforeTime + ", criticalBeforeTime=" + criticalBeforeTime);

            // handle deletes for different sensitivity levels
            int normalDeleted = 0;
            int highDeleted = 0;
            int criticalDeleted = 0;

            QueryWrapper<OperationLog> normalWrapper = new QueryWrapper<>();
            normalWrapper.eq("sensitive_level", "normal")
                        .lt("create_time", normalBeforeTime);
            normalDeleted = operationLogMapper.delete(normalWrapper);

            QueryWrapper<OperationLog> highWrapper = new QueryWrapper<>();
            highWrapper.eq("sensitive_level", "high")
                      .lt("create_time", normalBeforeTime);
            highDeleted = operationLogMapper.delete(highWrapper);

            QueryWrapper<OperationLog> criticalWrapper = new QueryWrapper<>();
            criticalWrapper.eq("sensitive_level", "critical")
                          .lt("create_time", criticalBeforeTime);
            criticalDeleted = operationLogMapper.delete(criticalWrapper);

            int totalDeleted = normalDeleted + highDeleted + criticalDeleted;
            logger.info("Deleted counts: normal=" + normalDeleted + ", high=" + highDeleted + ", critical=" + criticalDeleted + ", total=" + totalDeleted);

            return totalDeleted;
        } catch (Exception e) {
            logger.error("鎸夋晱鎰熺骇鍒竻鐞嗘搷浣滄棩蹇楀け璐ワ細" + e.getMessage(), e);
            return 0;
        }
    }
    
}
