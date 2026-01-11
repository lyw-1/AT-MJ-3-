package com.mold.digitalization.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mold.process.common.Result;
import com.mold.digitalization.entity.dto.OperationLogDTO;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.audit.AuditAnalysisService;
import com.mold.digitalization.service.audit.LogExportService;
import com.mold.digitalization.service.system.OperationLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 操作日志审计Controller
 * 提供操作日志查询、统计分析、审计报告等功能
 */
@RestController
@RequestMapping("/v1/api/system/audit")
@Api(tags = "操作日志审计")
@RequiredArgsConstructor
public class OperationLogAuditController {

    private final OperationLogService operationLogService;
    private final AuditAnalysisService auditAnalysisService;
    private final LogExportService logExportService;

    /**
     * 分页查询操作日志
     */
    @GetMapping("/logs/page")
    @ApiOperation(value = "分页查询操作日志", notes = "支持多维度条件查询，包括用户名、操作类型、敏感级别等")
    public Result<Page<OperationLog>> queryLogPage(@Valid OperationLogQueryDTO queryDTO) {
        Page<OperationLog> page = operationLogService.queryByCondition(queryDTO);
        return Result.success(page);
    }

    /**
     * 获取单条操作日志详情
     */
    @GetMapping("/logs/{id}")
    @ApiOperation(value = "获取操作日志详情", notes = "根据日志ID获取完整的操作日志信息")
    public Result<OperationLogDTO> getLogDetail(@PathVariable Long id) {
        OperationLog operationLog = operationLogService.getById(id);
        if (operationLog == null) {
            return Result.failed("日志不存在");
        }
        // 转换为DTO
        OperationLogDTO dto = new OperationLogDTO();
        dto.setId(operationLog.getId());
        dto.setUsername(operationLog.getUsername());
        dto.setUserId(operationLog.getUserId());
        dto.setIp(operationLog.getUserIp());
        dto.setOperationType(operationLog.getOperationType());
        dto.setOperationDesc(operationLog.getOperationDesc());
        dto.setOperationContent(operationLog.getOperationContent());
        // 使用实体中的 result 字段（与表列 result 对应）
        dto.setResult(operationLog.getResult());
        dto.setIsSensitive(operationLog.getIsSensitive());
        dto.setCreateTime(operationLog.getOperationTime());
        return Result.success(dto);
    }

    /**
     * 查询敏感操作日志
     */
    @GetMapping("/logs/sensitive")
    @ApiOperation(value = "查询敏感操作日志", notes = "专门用于查询标记为敏感的操作日志记录")
    public Result<Page<OperationLog>> querySensitiveLogs(@Valid OperationLogQueryDTO queryDTO) {
        queryDTO.setIsSensitive(true);
        Page<OperationLog> page = operationLogService.queryByCondition(queryDTO);
        return Result.success(page);
    }

    /**
     * 批量删除操作日志
     */
    @DeleteMapping("/logs/batch")
    @ApiOperation(value = "批量删除操作日志", notes = "根据ID列表批量删除操作日志记录")
    public Result<Boolean> deleteBatchLogs(@RequestBody List<Long> ids) {
        Long[] idArray = ids.toArray(new Long[0]);
        boolean success = operationLogService.deleteBatch(idArray);
        return success ? Result.success(true) : Result.success(false);
    }

    /**
     * 清理指定时间之前的操作日志
     */
    @DeleteMapping("/logs/clean")
    @ApiOperation(value = "清理历史操作日志", notes = "清理指定时间之前的操作日志记录，保留关键日志")
    public Result<Integer> cleanOldLogs(@RequestParam LocalDateTime beforeTime) {
        int deletedCount = operationLogService.cleanOldLogs(beforeTime);
        return Result.success(deletedCount);
    }

    /**
     * 按操作类型统计
     */
    @GetMapping("/statistics/operation-type")
    @ApiOperation(value = "按操作类型统计", notes = "统计不同操作类型的日志数量")
    public Result<List<OperationLogStatisticDTO>> statisticByOperationType(
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime) {
        List<OperationLogStatisticDTO> statistics = operationLogService.statisticByOperationType(startTime, endTime);
        return Result.success(statistics);
    }

    /**
     * 按用户统计
     */
    @GetMapping("/statistics/user")
    @ApiOperation(value = "按用户统计", notes = "统计不同用户的操作日志数量")
    public Result<List<OperationLogStatisticDTO>> statisticByUser(
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime) {
        List<OperationLogStatisticDTO> statistics = operationLogService.statisticByUser(startTime, endTime);
        return Result.success(statistics);
    }

    /**
     * 按模块统计
     */
    @GetMapping("/statistics/module")
    @ApiOperation(value = "按模块统计", notes = "统计不同功能模块的操作日志数量")
    public Result<List<OperationLogStatisticDTO>> statisticByModule(
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime) {
        List<OperationLogStatisticDTO> statistics = operationLogService.statisticByModule(startTime, endTime);
        return Result.success(statistics);
    }

    /**
     * 分析用户行为
     */
    @GetMapping("/analysis/user-behavior")
    @ApiOperation(value = "分析用户行为", notes = "分析指定用户在时间范围内的操作行为模式")
    public Result<Map<String, Object>> analyzeUserBehavior(
            @RequestParam Long userId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        Map<String, Object> analysisResult = auditAnalysisService.analyzeUserBehavior(userId, startTime, endTime);
        return Result.success(analysisResult);
    }

    /**
     * 检测异常操作
     */
    @GetMapping("/analysis/abnormal-operations")
    @ApiOperation(value = "检测异常操作", notes = "检测并识别时间范围内的异常操作行为")
    public Result<List<OperationLog>> detectAbnormalOperations(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        List<OperationLog> abnormalOperations = auditAnalysisService.detectAbnormalOperations(startTime, endTime);
        return Result.success(abnormalOperations);
    }

    /**
     * 分析操作趋势
     */
    @GetMapping("/analysis/operation-trend")
    @ApiOperation(value = "分析操作趋势", notes = "按日、周、月维度分析操作趋势变化")
    public Result<List<Map<String, Object>>> analyzeOperationTrend(
            @RequestParam String dimension, // day, week, month
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        List<Map<String, Object>> trendData = auditAnalysisService.analyzeOperationTrend(dimension, startTime, endTime);
        return Result.success(trendData);
    }

    /**
     * 统计敏感操作
     */
    @GetMapping("/analysis/sensitive-statistics")
    @ApiOperation(value = "统计敏感操作", notes = "统计符合查询条件的敏感操作数量和分布")
    public Result<OperationLogStatisticDTO> statisticSensitiveOperations(
            @Valid OperationLogQueryDTO queryDTO) {
        OperationLogStatisticDTO statisticDTO = auditAnalysisService.statisticSensitiveOperations(queryDTO);
        return Result.success(statisticDTO);
    }

    /**
     * 获取高频操作TOP N
     */
    @GetMapping("/analysis/top-operations")
    @ApiOperation(value = "获取高频操作TOP N", notes = "获取指定时间范围内发生频率最高的N种操作")
    public Result<List<Map<String, Object>>> getTopNOperations(
            @RequestParam int topN,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        List<Map<String, Object>> topOperations = auditAnalysisService.getTopNOperations(topN, startTime, endTime);
        return Result.success(topOperations);
    }

    /**
     * 分析操作成功率
     */
    @GetMapping("/analysis/success-rate")
    @ApiOperation(value = "分析操作成功率", notes = "分析不同维度下操作的成功率分布")
    public Result<List<Map<String, Object>>> analyzeSuccessRate(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam String groupBy) { // operationType, user, module
        List<Map<String, Object>> successRateData = auditAnalysisService.analyzeSuccessRate(startTime, endTime, groupBy);
        return Result.success(successRateData);
    }

    /**
     * 生成审计报告
     */
    @GetMapping("/report/generate")
    @ApiOperation(value = "生成审计报告", notes = "生成指定时间范围内的综合审计报告")
    public Result<Map<String, Object>> generateAuditReport(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        Map<String, Object> report = auditAnalysisService.generateAuditReport(startTime, endTime);
        return Result.success(report);
    }

    /**
     * 导出操作日志为Excel
     */
    @GetMapping("/logs/export/excel")
    @ApiOperation(value = "导出操作日志为Excel", notes = "将符合条件的操作日志导出为Excel文件")
    public ResponseEntity<InputStreamResource> exportLogsToExcel(@Valid OperationLogQueryDTO queryDTO) {
        try {
            String filename = "operation_logs_" + System.currentTimeMillis() + ".xlsx";
            ResponseEntity<byte[]> response = logExportService.exportToExcel(queryDTO);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(response.getBody()));
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("导出Excel失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导出操作日志为CSV
     */
    @GetMapping("/logs/export/csv")
    @ApiOperation(value = "导出操作日志为CSV", notes = "将符合条件的操作日志导出为CSV文件")
    public ResponseEntity<InputStreamResource> exportLogsToCsv(@Valid OperationLogQueryDTO queryDTO) {
        try {
            String filename = "operation_logs_" + System.currentTimeMillis() + ".csv";
            ResponseEntity<byte[]> response = logExportService.exportToCsv(queryDTO);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(response.getBody()));
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("导出CSV失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导出审计报告为PDF
     */
    @GetMapping("/report/export/pdf")
    @ApiOperation(value = "导出审计报告为PDF", notes = "将审计报告导出为PDF文件")
    public ResponseEntity<InputStreamResource> exportAuditReportToPdf(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        try {
            String filename = "audit_report_" + System.currentTimeMillis() + ".pdf";
            // 创建查询DTO
            OperationLogQueryDTO queryDTO = new OperationLogQueryDTO();
            queryDTO.setStartTime(startTime);
            queryDTO.setEndTime(endTime);
            ResponseEntity<byte[]> response = logExportService.exportAuditReportToPdf(queryDTO);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(response.getBody()));
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("导出PDF报告失败: " + e.getMessage(), e);
        }
    }

    /**
     * 导出敏感操作日志为Excel
     */
    @GetMapping("/logs/sensitive/export")
    @ApiOperation(value = "导出敏感操作日志", notes = "将敏感操作日志导出为Excel文件")
    public ResponseEntity<InputStreamResource> exportSensitiveLogs(@Valid OperationLogQueryDTO queryDTO) {
        try {
            String filename = "sensitive_operation_logs_" + System.currentTimeMillis() + ".xlsx";
            queryDTO.setIsSensitive(true);
            ResponseEntity<byte[]> response = logExportService.exportSensitiveLogsToExcel(queryDTO);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(response.getBody()));
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("导出敏感操作日志失败: " + e.getMessage(), e);
        }
    }
}
