package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldStorageRecord;
import com.mold.digitalization.service.MoldStorageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 妯″叿鍏ュ簱记录控制鍣?
 * 澶勭悊妯″叿鍏ュ簱鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/mold/storage")
public class MoldStorageRecordController extends BaseController {
    
    @Autowired
    private MoldStorageRecordService moldStorageRecordService;
    
    /**
     * 鏍规嵁记录ID获取鍏ュ簱记录
     * @param id 记录ID
     * @return 鍏ュ簱记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoldStorageRecord> getById(@PathVariable Long id) {
        MoldStorageRecord record = moldStorageRecordService.getById(id);
        if (record != null) {
            return success(record);
        } else {
            return notFoundGeneric("Record not found");
        }
    }
    
    /**
     * 创建鍏ュ簱记录
     * @param record 鍏ュ簱记录淇℃伅
     * @return 创建鐨勮褰?
     */
    @PostMapping
    public ResponseEntity<MoldStorageRecord> create(@RequestBody MoldStorageRecord record) {
        boolean created = moldStorageRecordService.createRecord(record);
        if (created) {
            return success(record);
        } else {
            // 返回鐘舵€佺爜500鍜宯ull瀵硅薄锛屼互鍖归厤方法返回绫诲瀷
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * 鏍规嵁妯″叿ID查询鍏ュ簱记录
     * @param moldId 妯″叿ID
     * @return 鍏ュ簱记录鍒楄〃
     */
    @GetMapping("/mold/{moldId}")
    public ResponseEntity<List<MoldStorageRecord>> getByMoldId(@PathVariable Long moldId) {
        List<MoldStorageRecord> records = moldStorageRecordService.getByMoldId(moldId);
        return success(records);
    }
    
    /**
     * 鏍规嵁鎵规鍙锋煡璇㈠叆搴撹褰?
     * @param batchNumber 鎵规鍙?
     * @return 鍏ュ簱记录鍒楄〃
     */
    @GetMapping("/batch/{batchNumber}")
    public ResponseEntity<List<MoldStorageRecord>> getByBatchNumber(@PathVariable String batchNumber) {
        List<MoldStorageRecord> records = moldStorageRecordService.getByBatchNumber(batchNumber);
        return success(records);
    }
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询鍏ュ簱记录
     * @param startDate 开€濮嬫棩鏈?
     * @param endDate 缁撴潫日期
     * @return 鍏ュ簱记录鍒楄〃
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<MoldStorageRecord>> getByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // 瑙ｆ瀽鏃堕棿瀛楃涓诧紝鏀寔澶氱鏍煎紡
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<MoldStorageRecord> records = moldStorageRecordService.getByDateRange(start, end);
            return success(records);
        } catch (Exception e) {
            // 返回绌哄垪琛ㄨ€屼笉鏄敊璇秷鎭紝淇濇寔返回绫诲瀷涓€鑷?
            return success(List.of());
        }
    }
    
    /**
     * 鏍规嵁璐ㄦ结果查询鍏ュ簱记录
     * @param inspectionResult 璐ㄦ结果锛?:鍚堟牸, 0:涓嶅悎鏍硷級
     * @return 鍏ュ簱记录鍒楄〃
     */
    @GetMapping("/inspection/{inspectionResult}")
    public ResponseEntity<List<MoldStorageRecord>> getByInspectionResult(@PathVariable Integer inspectionResult) {
        List<MoldStorageRecord> records = moldStorageRecordService.getByInspectionResult(inspectionResult);
        return success(records);
    }
    
    /**
     * 更新璐ㄦ结果
     * @param id 记录ID
     * @param inspectionResult 璐ㄦ结果
     * @return 更新结果
     */
    @PutMapping("/{id}/inspection")
    public ResponseEntity<Void> updateInspectionResult(
            @PathVariable Long id,
            @RequestParam Integer inspectionResult) {
        boolean updated = moldStorageRecordService.updateInspectionResult(id, inspectionResult);
        if (updated) {
            return success();
        } else {
            // 返回404鐘舵€佺爜鍜宯ull锛屼繚鎸佽繑鍥炵被鍨嬩竴鑷?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    /**
     * 更新鍏ュ簱记录
     * @param id 记录ID
     * @param record 更新鐨勮褰曚俊鎭?
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MoldStorageRecord record) {
        record.setId(id);
        boolean updated = moldStorageRecordService.updateRecord(record);
        if (updated) {
            return success();
        } else {
            // 返回404鐘舵€佺爜鍜宯ull锛屼繚鎸佽繑鍥炵被鍨嬩竴鑷?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    /**
     * 删除鍏ュ簱记录
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = moldStorageRecordService.deleteRecord(id);
        if (deleted) {
            return success();
        } else {
            // 返回404鐘舵€佺爜鍜宯ull锛屼繚鎸佽繑鍥炵被鍨嬩竴鑷?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    /**
     * 获取鍏ュ簱记录缁熻淇℃伅
     * @return 缁熻淇℃伅
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        try {
            Map<String, Object> stats = moldStorageRecordService.getStorageStats();
            return success(stats);
        } catch (Exception e) {
            return success(Map.of());
        }
    }
    
    /**
     * 获取鎵€鏈夊叆搴撹褰?
     * @return 鍏ュ簱记录鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<MoldStorageRecord>> getAll() {
        try {
            List<MoldStorageRecord> records = moldStorageRecordService.getAllRecords();
            return success(records);
        } catch (Exception e) {
            return success(List.of());
        }
    }
}
