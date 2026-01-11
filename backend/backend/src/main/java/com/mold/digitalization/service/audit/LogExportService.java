package com.mold.digitalization.service.audit;

import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * 日志导出服务接口
 */
public interface LogExportService {

    /**
     * 导出操作日志为Excel文件
     * @param queryDTO 查询条件
     * @return ResponseEntity 包含Excel文件的响应
     * @throws IOException IO异常
     */
    ResponseEntity<byte[]> exportToExcel(OperationLogQueryDTO queryDTO) throws IOException;

    /**
     * 导出操作日志为CSV文件
     * @param queryDTO 查询条件
     * @return ResponseEntity 包含CSV文件的响应
     * @throws IOException IO异常
     */
    ResponseEntity<byte[]> exportToCsv(OperationLogQueryDTO queryDTO) throws IOException;

    /**
     * 导出审计报告为PDF文件
     * @param queryDTO 查询条件
     * @return ResponseEntity 包含PDF文件的响应
     * @throws IOException IO异常
     */
    ResponseEntity<byte[]> exportAuditReportToPdf(OperationLogQueryDTO queryDTO) throws IOException;

    /**
     * 导出敏感操作日志为Excel文件
     * @param queryDTO 查询条件
     * @return ResponseEntity 包含Excel文件的响应
     * @throws IOException IO异常
     */
    ResponseEntity<byte[]> exportSensitiveLogsToExcel(OperationLogQueryDTO queryDTO) throws IOException;
}
