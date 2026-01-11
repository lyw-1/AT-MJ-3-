package com.mold.digitalization.controller;

import com.mold.digitalization.entity.ProductionRecord;
import com.mold.digitalization.service.ProductionRecordService;
import com.mold.digitalization.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 进度记录Controller
 */
@RestController
@RequestMapping("/api/production-records")
public class ProductionRecordController {

    @Autowired
    private ProductionRecordService service;

    /**
     * 根据任务ID查询进度记录
     */
    @GetMapping("/task/{taskId}")
    public ResponseDTO<List<ProductionRecord>> getByTaskId(@PathVariable Long taskId) {
        List<ProductionRecord> records = service.getByTaskId(taskId);
        return ResponseDTO.success(records);
    }

    /**
     * 根据任务ID和工序查询进度记录
     */
    @GetMapping("/task/{taskId}/process/{process}")
    public ResponseDTO<ProductionRecord> getByTaskIdAndProcess(@PathVariable Long taskId, @PathVariable String process) {
        ProductionRecord record = service.getByTaskIdAndProcess(taskId, process);
        return ResponseDTO.success(record);
    }

    /**
     * 查询最新的进度记录
     */
    @GetMapping("/task/{taskId}/latest")
    public ResponseDTO<ProductionRecord> getLatestByTaskId(@PathVariable Long taskId) {
        ProductionRecord record = service.getLatestByTaskId(taskId);
        return ResponseDTO.success(record);
    }

    /**
     * 创建进度记录
     */
    @PostMapping
    public ResponseDTO<ProductionRecord> create(@RequestBody ProductionRecord record) {
        boolean success = service.create(record);
        return success ? ResponseDTO.success(record) : ResponseDTO.error("创建进度记录失败");
    }

    /**
     * 更新进度记录
     */
    @PutMapping("/{id}")
    public ResponseDTO<ProductionRecord> update(@PathVariable Long id, @RequestBody ProductionRecord record) {
        record.setId(id);
        boolean success = service.updateRecord(record);
        return success ? ResponseDTO.success(record) : ResponseDTO.error("更新进度记录失败");
    }

    /**
     * 删除进度记录
     */
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable Long id) {
        boolean success = service.deleteRecord(id);
        return success ? ResponseDTO.success(true) : ResponseDTO.error("删除进度记录失败");
    }
}
