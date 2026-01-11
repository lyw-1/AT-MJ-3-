package com.mold.digitalization.controller;

import com.mold.digitalization.entity.ProductionTask;
import com.mold.digitalization.service.ProductionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * 鐢熶骇浠诲姟控制鍣?
 * 澶勭悊鐢熶骇浠诲姟鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/production-tasks")
public class ProductionTaskController extends BaseController {
    
    @Autowired
    private ProductionTaskService productionTaskService;
    
    /**
     * 鏍规嵁鐢熶骇浠诲姟ID获取鐢熶骇浠诲姟淇℃伅
     * @param id 鐢熶骇浠诲姟ID
     * @return 鐢熶骇浠诲姟淇℃伅
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductionTask> getProductionTaskById(@PathVariable Long id) {
        ProductionTask task = productionTaskService.getById(id);
        if (task != null) {
            return success(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 鏍规嵁鐢熶骇浠诲姟缂栫爜获取鐢熶骇浠诲姟淇℃伅
     * @param taskCode 鐢熶骇浠诲姟缂栫爜
     * @return 鐢熶骇浠诲姟淇℃伅
     */
    @GetMapping("/code/{taskCode}")
    public ResponseEntity<ProductionTask> getProductionTaskByCode(@PathVariable String taskCode) {
        ProductionTask task = productionTaskService.getProductionTaskByCode(taskCode);
        if (task != null) {
            return success(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 鏍规嵁璁惧ID获取鐢熶骇浠诲姟鍒楄〃
     * @param equipmentId 璁惧ID
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<ProductionTask>> getProductionTasksByEquipmentId(@PathVariable Long equipmentId) {
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByEquipmentId(equipmentId);
        return success(tasks);
    }
    
    /**
     * 鏍规嵁鐘舵€佽幏鍙栫敓浜т换鍔″垪琛?
     * @param status 浠诲姟状态
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductionTask>> getProductionTasksByStatus(@PathVariable Integer status) {
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByStatus(status);
        return success(tasks);
    }
    
    /**
     * 鏍规嵁妯″叿ID获取鐢熶骇浠诲姟鍒楄〃
     * @param moldId 妯″叿ID
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    @GetMapping("/mold/{moldId}")
    public ResponseEntity<List<ProductionTask>> getProductionTasksByMoldId(@PathVariable Long moldId) {
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByMoldId(moldId);
        return success(tasks);
    }
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿获取鐢熶骇浠诲姟鍒楄〃
     * @param startTime 开€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    @GetMapping("/time-range")
    public ResponseEntity<List<ProductionTask>> getProductionTasksByTimeRange(
            @RequestParam Date startTime, @RequestParam Date endTime) {
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByTimeRange(startTime, endTime);
        return success(tasks);
    }
    
    /**
     * 创建鏂扮敓浜т换鍔?
     * @param task 鐢熶骇浠诲姟淇℃伅
     * @return 创建鐨勭敓浜т换鍔′俊鎭?
     */
    @PostMapping
    public ResponseEntity<ProductionTask> createProductionTask(@RequestBody ProductionTask task) {
        boolean created = productionTaskService.createProductionTask(task);
        if (created) {
            return created(task);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新鐢熶骇浠诲姟淇℃伅
     * @param id 鐢熶骇浠诲姟ID
     * @param task 鐢熶骇浠诲姟淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductionTask> updateProductionTask(@PathVariable Long id, @RequestBody ProductionTask task) {
        task.setId(id);
        boolean updated = productionTaskService.updateProductionTask(task);
        if (updated) {
            return success(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 更新鐢熶骇浠诲姟状态
     * @param id 鐢熶骇浠诲姟ID
     * @param status 鏂扮姸鎬?
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ProductionTask> updateProductionTaskStatus(@PathVariable Long id, @RequestParam Integer status) {
        ProductionTask task = productionTaskService.getById(id);
        if (task != null) {
            boolean updated = productionTaskService.updateProductionTaskStatus(id, status);
            if (updated) {
                return success(task);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除鐢熶骇浠诲姟
     * @param id 鐢熶骇浠诲姟ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductionTask> deleteProductionTask(@PathVariable Long id) {
        ProductionTask task = productionTaskService.getById(id);
        if (task != null) {
            boolean deleted = productionTaskService.deleteProductionTask(id);
            if (deleted) {
                return success(task);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取鎵€鏈夌敓浜т换鍔″垪琛?
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<ProductionTask>> getAllProductionTasks() {
        List<ProductionTask> tasks = productionTaskService.getAllProductionTasks();
        return success(tasks);
    }
}
