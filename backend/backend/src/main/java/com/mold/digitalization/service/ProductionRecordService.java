package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.ProductionRecord;

import java.util.List;

/**
 * 进度记录Service
 */
public interface ProductionRecordService extends IService<ProductionRecord> {
    /**
     * 根据任务ID查询进度记录
     */
    List<ProductionRecord> getByTaskId(Long taskId);
    
    /**
     * 根据任务ID和工序查询进度记录
     */
    ProductionRecord getByTaskIdAndProcess(Long taskId, String process);
    
    /**
     * 查询最新的进度记录
     */
    ProductionRecord getLatestByTaskId(Long taskId);
    
    /**
     * 根据模具ID查询进度记录
     */
    List<ProductionRecord> getProductionRecordsByMoldId(Long moldId);
    
    /**
     * 创建进度记录
     */
    boolean create(ProductionRecord record);
    
    /**
     * 更新进度记录
     */
    boolean updateRecord(ProductionRecord record);
    
    /**
     * 删除进度记录
     */
    boolean deleteRecord(Long id);
}
