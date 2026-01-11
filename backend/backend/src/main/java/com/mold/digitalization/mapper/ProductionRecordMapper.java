package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.ProductionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 进度记录Mapper
 */
@Mapper
public interface ProductionRecordMapper extends BaseMapper<ProductionRecord> {
    /**
     * 根据任务ID查询进度记录
     */
    List<ProductionRecord> selectByTaskId(@Param("taskId") Long taskId);
    
    /**
     * 根据任务ID和工序查询进度记录
     */
    ProductionRecord selectByTaskIdAndProcess(@Param("taskId") Long taskId, @Param("process") String process);
    
    /**
     * 查询最新的进度记录
     */
    ProductionRecord selectLatestByTaskId(@Param("taskId") Long taskId);
    
    /**
     * 根据模具ID查询进度记录
     */
    List<ProductionRecord> selectByMoldId(@Param("moldId") Long moldId);
}
