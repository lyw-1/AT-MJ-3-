package com.mold.digitalization.controller;

import com.mold.digitalization.entity.AccessoryInspectionRecord;
import com.mold.digitalization.service.AccessoryInspectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 閰嶄欢妫€楠岃褰曟帶鍒跺櫒
 * 澶勭悊閰嶄欢妫€楠岀浉鍏崇殑HTTP璇锋眰
 */
@RestController
@RequestMapping("/api/accessory-inspection")
public class AccessoryInspectionRecordController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AccessoryInspectionRecordController.class);

    @Autowired
    private AccessoryInspectionRecordService accessoryInspectionRecordService;

    /**
     * 鏍规嵁记录ID获取閰嶄欢妫€楠岃褰?
     * @param id 记录ID
     * @return 閰嶄欢妫€楠岃褰?
     */
    @GetMapping("/list")
    public ResponseEntity<List<AccessoryInspectionRecord>> listRecords(@RequestParam(required = false) String filter) {
        try {
            if (filter == null || filter.isEmpty()) {
                // No filter provided — return empty list to avoid using undefined service methods
                return ResponseEntity.ok(Collections.emptyList());
            }
            List<AccessoryInspectionRecord> records = accessoryInspectionRecordService.getByBatchNumber(filter);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            log.error("List inspection records error", e);
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    /**
     * Create an inspection record
     * @param record inspection record payload
     * @return created record or error
     */
    @PostMapping
    public ResponseEntity<AccessoryInspectionRecord> create(@RequestBody AccessoryInspectionRecord record) {
        try {
            Long id = accessoryInspectionRecordService.createInspectionRecord(record);
            if (id != null && id > 0) {
                // set id if service returns it
                record.setId(id);
                return ResponseEntity.ok(record);
            } else {
                return ResponseEntity.status(500).body(null);
            }
        } catch (Exception e) {
            log.error("Create inspection record failed", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 鏍规嵁閰嶄欢ID获取妫€楠岃褰曞垪琛?
     * @param accessoryId 閰嶄欢ID
     * @return 妫€楠岃褰曞垪琛?
     */
    @GetMapping("/accessory/{accessoryId}")
    public ResponseEntity<List<AccessoryInspectionRecord>> getByAccessoryId(@PathVariable Long accessoryId) {
        List<AccessoryInspectionRecord> records = accessoryInspectionRecordService.getByAccessoryId(accessoryId);
        return success(records);
    }

    /**
     * 鏍规嵁鎵规鍙疯幏鍙栨楠岃褰曞垪琛?
     * @param batchNumber 鎵规鍙?
     * @return 妫€楠岃褰曞垪琛?
     */
    @GetMapping("/batch/{batchNumber}")
    public ResponseEntity<List<AccessoryInspectionRecord>> getByBatchNumber(@PathVariable String batchNumber) {
        List<AccessoryInspectionRecord> records = accessoryInspectionRecordService.getByBatchNumber(batchNumber);
        return success(records);
    }

    /**
     * 获取涓嶅悎鏍肩殑妫€楠岃褰曞垪琛?
     * @return 涓嶅悎鏍艰褰曞垪琛?
     */
    @GetMapping("/unqualified")
    public ResponseEntity<List<AccessoryInspectionRecord>> getUnqualifiedRecords() {
        List<AccessoryInspectionRecord> records = accessoryInspectionRecordService.getUnqualifiedRecords();
        return success(records);
    }

    /**
     * 鏍规嵁日期鑼冨洿查询妫€楠岃褰?
     * @param startDate 开€濮嬫棩鏈?
     * @param endDate 缁撴潫日期
     * @return 妫€楠岃褰曞垪琛?
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<AccessoryInspectionRecord>> getByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<AccessoryInspectionRecord> records = accessoryInspectionRecordService.getByDateRange(startDate, endDate);
        return success(records);
    }

    /**
     * 更新妫€楠岀粨鏋?
     * @param id 记录ID
     * @param inspectionResult 妫€楠岀粨鏋?
     * @return 更新结果
     */
    @PutMapping("/{id}/result")
    public ResponseEntity<Void> updateInspectionResult(
            @PathVariable Long id,
            @RequestParam Integer inspectionResult) {
        boolean updated = accessoryInspectionRecordService.updateInspectionResult(id, inspectionResult);
        if (updated) {
            return success();
        } else {
            return notFoundGeneric("记录涓嶅瓨鍦ㄦ垨更新失败");
        }
    }

    /**
     * 更新妫€楠岃褰?
     * @param id 记录ID
     * @param record 更新鐨勮褰曚俊鎭?
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody AccessoryInspectionRecord record) {
        record.setId(id);
        boolean updated = accessoryInspectionRecordService.updateById(record);
        if (updated) {
            return success();
        } else {
            return notFoundGeneric("记录涓嶅瓨鍦ㄦ垨更新失败");
        }
    }

    /**
     * 删除妫€楠岃褰?
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = accessoryInspectionRecordService.removeById(id);
        if (deleted) {
            return success();
        } else {
            return notFoundGeneric("记录涓嶅瓨鍦ㄦ垨删除失败");
        }
    }

    /**
     * 获取妫€楠岀粺璁′俊鎭?
     * @return 缁熻淇℃伅
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getInspectionStats(@RequestParam LocalDateTime startDate,
                                                                  @RequestParam LocalDateTime endDate) {
        Map<String, Object> stats = accessoryInspectionRecordService.getInspectionStats(startDate, endDate);
        return success(stats);
    }

    /**
     * 获取鎵€鏈夋楠岃褰?
     * @return 妫€楠岃褰曞垪琛?
     */
    @GetMapping
    public ResponseEntity<List<AccessoryInspectionRecord>> getAll() {
        List<AccessoryInspectionRecord> records = accessoryInspectionRecordService.list();
        return success(records);
    }
}
