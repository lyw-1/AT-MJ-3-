package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.ProcessPreset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序预设置Mapper
 */
@Mapper
public interface ProcessPresetMapper extends BaseMapper<ProcessPreset> {
    
    /**
     * 根据模具ID和工序代码查询预设置列表
     */
    List<ProcessPreset> getByMoldIdAndProcessCode(@Param("moldId") Long moldId, @Param("processCode") String processCode);
    
    /**
     * 根据模具ID查询预设置列表
     */
    List<ProcessPreset> getByMoldId(@Param("moldId") Long moldId);
    
    /**
     * 根据工序代码查询预设置列表
     */
    List<ProcessPreset> getByProcessCode(@Param("processCode") String processCode);
    
    /**
     * 根据模具ID和工序代码删除预设置
     */
    int deleteByMoldIdAndProcessCode(@Param("moldId") Long moldId, @Param("processCode") String processCode);
    
    /**
     * 根据模具ID删除预设置
     */
    int deleteByMoldId(@Param("moldId") Long moldId);
}
