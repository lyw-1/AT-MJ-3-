package com.mold.digitalization.service.audit.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.audit.LogExportService;
import com.mold.digitalization.service.system.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鏃ュ織瀵煎嚭服务实现绫?
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LogExportServiceImpl implements LogExportService {

    private final OperationLogService operationLogService;

    @Override
    public ResponseEntity<byte[]> exportToExcel(OperationLogQueryDTO queryDTO) throws IOException {
        // 鏋勫缓查询鏉′欢
        QueryWrapper<OperationLog> wrapper = buildQueryWrapper(queryDTO);
        // 查询数据
        List<OperationLog> logs = operationLogService.list(wrapper);
        
    // convert to export data
    List<Map<String, Object>> exportData = convertToExportData(logs);
        
    // create Excel writer
    ExcelWriter writer = ExcelUtil.getWriter(true);
        
        // 设置琛ㄥご鏍峰紡
        setHeaderStyle(writer);
        
    // define headers (English)
    String[] headers = {"ID", "User ID", "Username", "Module", "Operation Type", "Operation Desc",
              "IP", "Operation Content", "Result", "IsSensitive", "CreateTime"};
        String[] keys = {"id", "userId", "username", "module", "operationType", "operationDesc", 
                       "ip", "operationContent", "result", "isSensitive", "createTime"};
        
        // 鍐欏叆琛ㄥご
        writer.writeHeadRow(List.of(headers));
        
        // 鍐欏叆数据
        for (Map<String, Object> row : exportData) {
            List<Object> dataRow = new ArrayList<>();
            for (String key : keys) {
                // 纭繚鏁忔劅绾у埆瀛楁姝ｇ‘设置
                if ("sensitiveLevel".equals(key) && row.get(key) == null) {
                    // 濡傛灉鏁忔劅绾у埆涓簄ull锛岄粯璁や负normal
                    dataRow.add("normal");
                } else {
                    dataRow.add(row.get(key));
                }
            }
            writer.writeRow(dataRow);
        }
        
    // output to byte array
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.flush(outputStream);
        writer.close();
        
        byte[] bytes = outputStream.toByteArray();
        outputStream.close();
        
    // set response headers
    HttpHeaders responseHeaders = new HttpHeaders();
    String fileName = "operation_logs_" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + ".xlsx";
    responseHeaders.setContentDispositionFormData("attachment",
        URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()));
    responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        return new ResponseEntity<>(bytes, responseHeaders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> exportToCsv(OperationLogQueryDTO queryDTO) throws IOException {
        // 鏋勫缓查询鏉′欢
        QueryWrapper<OperationLog> wrapper = buildQueryWrapper(queryDTO);
        // 查询数据
        List<OperationLog> logs = operationLogService.list(wrapper);
        
    // build CSV content
        StringBuilder csvContent = new StringBuilder();
        // 添加BOM浠ユ敮鎸丒xcel姝ｇ‘璇嗗埆UTF-8缂栫爜
        csvContent.append("\uFEFF");
        
    // append CSV header (English)
    csvContent.append("ID,UserID,Username,Module,OperationType,OperationDesc,IP,OperationContent,Result,IsSensitive,CreateTime\n");
        
        // 鍐欏叆数据
        for (OperationLog log : logs) {
            csvContent.append(log.getId()).append(",")
                    .append(log.getUserId()).append(",")
                    .append(escapeCsv(log.getUsername())).append(",")
                    .append(escapeCsv(log.getModule())).append(",")
                    .append(escapeCsv(log.getOperationType())).append(",")
                    .append(escapeCsv(log.getOperationDesc())).append(",")
                    .append(log.getUserIp()).append(",")
                    .append(escapeCsv(log.getOperationContent())).append(",")
                    .append(escapeCsv(log.getResultCode())).append(",")
                    .append(log.getIsSensitive() ? "true" : "false").append(",")
                    .append(DateUtil.format(log.getOperationTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        }
        
    byte[] bytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);

    // set CSV response headers
    HttpHeaders csvHeaders = new HttpHeaders();
    String csvFileName = "operation_logs_" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + ".csv";
    csvHeaders.setContentDispositionFormData("attachment",
        URLEncoder.encode(csvFileName, StandardCharsets.UTF_8.toString()));
    csvHeaders.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));

    return new ResponseEntity<>(bytes, csvHeaders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> exportAuditReportToPdf(OperationLogQueryDTO queryDTO) throws IOException {
        // 鐢变簬PDF鐢熸垚闇€瑕侀澶栦緷璧栵紝杩欓噷鎻愪緵涓€涓畝鍗曠殑实现
        // 实际项目涓彲浠ヤ娇鐢╥Text銆丄pache PDFBox绛夊簱
        
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("===== 操作鏃ュ織瀹¤鎶ュ憡 =====\n");
        reportContent.append("鐢熸垚鏃堕棿: " + DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss") + "\n");
        reportContent.append("查询鏉′欢:\n");
        if (queryDTO.getStartTime() != null) {
            reportContent.append("- 开€濮嬫椂闂? " + DateUtil.format(queryDTO.getStartTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        }
        if (queryDTO.getEndTime() != null) {
            reportContent.append("- 缁撴潫鏃堕棿: " + DateUtil.format(queryDTO.getEndTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        }
        if (queryDTO.getUsername() != null) {
            reportContent.append("- 用户名? " + queryDTO.getUsername() + "\n");
        }
        if (queryDTO.getModule() != null) {
            reportContent.append("- 操作妯″潡: " + queryDTO.getModule() + "\n");
        }
        
        // 查询缁熻淇℃伅
        QueryWrapper<OperationLog> wrapper = buildQueryWrapper(queryDTO);
        long totalCount = operationLogService.count(wrapper);
        long successCount = operationLogService.count(wrapper.clone().eq("result_code", "200"));
        long sensitiveCount = operationLogService.count(wrapper.clone().eq("is_sensitive", true));
        
        reportContent.append("\n缁熻淇℃伅:\n");
        reportContent.append("- 鎬绘搷浣滄暟: " + totalCount + "\n");
        reportContent.append("- 成功操作鏁? " + successCount + "\n");
        reportContent.append("- 鏁忔劅操作鏁? " + sensitiveCount + "\n");
        reportContent.append("- 成功鐜? " + (totalCount > 0 ? String.format("%.2f%%", (double) successCount / totalCount * 100) : "0%"));
        
    byte[] bytes = reportContent.toString().getBytes(StandardCharsets.UTF_8);

    // set response headers
    HttpHeaders reportHeaders = new HttpHeaders();
    String reportFileName = "audit_report_" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + ".txt";
    reportHeaders.setContentDispositionFormData("attachment",
        URLEncoder.encode(reportFileName, StandardCharsets.UTF_8.toString()));
    reportHeaders.setContentType(MediaType.TEXT_PLAIN);

    return new ResponseEntity<>(bytes, reportHeaders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> exportSensitiveLogsToExcel(OperationLogQueryDTO queryDTO) throws IOException {
        // 设置涓轰粎查询鏁忔劅操作
        queryDTO.setIsSensitive(true);
        
        // 鏋勫缓查询鏉′欢
        QueryWrapper<OperationLog> wrapper = buildQueryWrapper(queryDTO);
        wrapper.eq("is_sensitive", true);
        
        // 查询数据
        List<OperationLog> logs = operationLogService.list(wrapper);
        
        // convert to export data
        List<Map<String, Object>> exportData = convertToExportData(logs);

        // create Excel writer
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // set header style
        setHeaderStyle(writer);

        // define headers (English)
        String[] headers = {"ID", "User ID", "Username", "Module", "Operation Type", "Operation Desc",
                          "IP", "Operation Content", "Result", "SensitiveLevel", "CreateTime"};
        String[] keys = {"id", "userId", "username", "module", "operationType", "operationDesc",
                       "ip", "operationContent", "result", "sensitiveLevel", "createTime"};

        // write header
        writer.writeHeadRow(List.of(headers));

        // write data
        for (Map<String, Object> row : exportData) {
            List<Object> dataRow = new ArrayList<>();
            for (String key : keys) {
                dataRow.add(row.getOrDefault(key, ""));
            }
            writer.writeRow(dataRow);
        }

        // output to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.flush(outputStream);
        writer.close();

        byte[] bytes = outputStream.toByteArray();
        outputStream.close();

        // set response headers
        HttpHeaders responseHeaders = new HttpHeaders();
        String fileName = "sensitive_operation_logs_" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + ".xlsx";
        responseHeaders.setContentDispositionFormData("attachment",
                URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()));
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(bytes, responseHeaders, HttpStatus.OK);
    }
    
    /**
     * 鏋勫缓查询鏉′欢
     */
    private QueryWrapper<OperationLog> buildQueryWrapper(OperationLogQueryDTO queryDTO) {
        QueryWrapper<OperationLog> wrapper = Wrappers.query();
        
        if (queryDTO != null) {
            if (queryDTO.getUsername() != null) {
                wrapper.eq("username", queryDTO.getUsername());
            }
            if (queryDTO.getModule() != null) {
                wrapper.eq("module", queryDTO.getModule());
            }
            if (queryDTO.getOperationType() != null) {
                wrapper.eq("operation_type", queryDTO.getOperationType());
            }
            if (queryDTO.getIsSensitive() != null) {
                wrapper.eq("is_sensitive", queryDTO.getIsSensitive());
            }
            if (queryDTO.getStartTime() != null) {
                wrapper.ge("operation_time", queryDTO.getStartTime());
            }
            if (queryDTO.getEndTime() != null) {
                wrapper.le("operation_time", queryDTO.getEndTime());
            }
            if (queryDTO.getResult() != null) {
                wrapper.eq("result_code", queryDTO.getResult());
            }
        }
        
        wrapper.orderByDesc("operation_time");
        
        return wrapper;
    }
    
    /**
     * 杞崲涓哄鍑烘暟鎹牸开?     */
    private List<Map<String, Object>> convertToExportData(List<OperationLog> logs) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (OperationLog log : logs) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", log.getId());
            row.put("userId", log.getUserId());
            row.put("username", log.getUsername());
            row.put("module", log.getModule());
            row.put("operationType", log.getOperationType());
            row.put("operationDesc", log.getOperationDesc());
            row.put("ip", log.getUserIp());
            row.put("operationContent", log.getOperationContent());
            row.put("result", log.getResultCode());
            row.put("isSensitive", log.getIsSensitive() ? "true" : "false");
            row.put("sensitiveLevel", log.getSensitiveLevel() != null ? log.getSensitiveLevel() : "");
            row.put("createTime", DateUtil.format(log.getOperationTime(), "yyyy-MM-dd HH:mm:ss"));
            
            result.add(row);
        }
        
        return result;
    }
    
    /**
     * 设置琛ㄥご鏍峰紡
     */
    private void setHeaderStyle(ExcelWriter writer) {
        CellStyle headStyle = writer.getHeadCellStyle();
        Font font = writer.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        headStyle.setFont(font);
        headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
    
    /**
     * CSV内容杞箟
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
