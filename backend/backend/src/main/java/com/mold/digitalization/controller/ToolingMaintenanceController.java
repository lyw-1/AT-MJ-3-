package com.mold.digitalization.controller;

import com.mold.digitalization.entity.ToolingMaintenance;
import com.mold.digitalization.service.ToolingMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 妯″叿缁存姢记录控制鍣? * 澶勭悊妯″叿缁存姢鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/tooling-maintenance")
public class ToolingMaintenanceController extends BaseController {

    @Autowired
    private ToolingMaintenanceService toolingMaintenanceService;

    /**
     * 鏍规嵁记录ID获取缁存姢记录
     * @param id 记录ID
     * @return 缁存姢记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<ToolingMaintenance> getById(@PathVariable Long id) {
        ToolingMaintenance record = toolingMaintenanceService.getById(id);
        if (record != null) {
            return ResponseEntity.ok(record);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 创建妯″叿缁存姢记录
     * @param record 缁存姢记录淇℃伅
     * @return 创建鐨勮褰?     */
    @PostMapping
    public ResponseEntity<String> create(@RequestBody ToolingMaintenance record) {
        Long id = toolingMaintenanceService.createMaintenance(record);
        if (id != null) {
            return success("缁存姢记录创建成功锛孖D: " + id);
        } else {
            return internalServerError("创建缁存姢记录失败");
        }
    }

    /**
     * 鏍规嵁妯″叿ID获取缁存姢记录鍒楄〃
     * @param moldId 妯″叿ID
     * @return 缁存姢记录鍒楄〃
     */
    @GetMapping("/mold/{moldId}")
    public ResponseEntity<List<ToolingMaintenance>> getByMoldId(@PathVariable Long moldId) {
        List<ToolingMaintenance> records = toolingMaintenanceService.getByMoldId(moldId);
        return ResponseEntity.ok(records);
    }

    /**
     * 鏍规嵁缁存姢鐘舵€佽幏鍙栫淮鎶よ褰曞垪琛?     * @param status 缁存姢状态     * @return 缁存姢记录鍒楄〃
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ToolingMaintenance>> getByStatus(@PathVariable Integer status) {
        List<ToolingMaintenance> records = toolingMaintenanceService.getByStatus(status);
        return success(records);
    }

    /**
     * 鏍规嵁日期鑼冨洿查询缁存姢记录
     * @param startDate 开€濮嬫棩鏈?     * @param endDate 缁撴潫日期
     * @return 缁存姢记录鍒楄〃
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<ToolingMaintenance>> getByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<ToolingMaintenance> records = toolingMaintenanceService.getByDateRange(startDate, endDate);
        return success(records);
    }

    /**
     * 更新缁存姢记录状态     * @param id 记录ID
     * @param status 缁存姢状态     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        boolean updated = toolingMaintenanceService.updateMaintenanceStatus(id, status);
        if (updated) {
            return success();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 更新缁存姢记录
     * @param id 记录ID
     * @param record 更新鐨勮褰曚俊鎭?     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ToolingMaintenance record) {
        record.setId(id);
        boolean result = toolingMaintenanceService.updateById(record);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 删除缁存姢记录
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean result = toolingMaintenanceService.removeById(id);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 获取缁存姢缁熻淇℃伅
     * @return 缁熻淇℃伅
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMaintenanceStats() {
        List<Map<String, Object>> typeStats = toolingMaintenanceService.getMaintenanceTypeStats();
        Map<String, Object> costStats = toolingMaintenanceService.getMaintenanceCostStats(
            LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        
        Map<String, Object> result = new HashMap<>();
        result.put("typeStats", typeStats);
        result.put("costStats", costStats);
        
        return success(result);
    }

    /**
     * 更新缁存姢结果
     * @param id 记录ID
     * @param result 缁存姢结果
     * @param nextMaintenanceTime 涓嬫缁存姢鏃堕棿
     * @return 更新结果
     */
    @PutMapping("/{id}/result")
    public ResponseEntity<Void> updateMaintenanceResult(
            @PathVariable Long id,
            @RequestParam String result,
            @RequestParam String nextMaintenanceTime) {
        boolean updated = toolingMaintenanceService.updateMaintenanceResult(id, result, nextMaintenanceTime);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 获取鎵€鏈夌淮鎶よ褰?     * @return 缁存姢记录鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<ToolingMaintenance>> getAll() {
        List<ToolingMaintenance> records = toolingMaintenanceService.list();
        return success(records);
    }
}