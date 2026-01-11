package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.ProcessPreset;
import java.util.List;
import java.util.Map;

/**
 * 工序预设置Service
 */
public interface ProcessPresetService extends IService<ProcessPreset> {
    
    /**
     * 根据模具ID和工序代码查询预设置列表
     * @param moldId 模具ID
     * @param processCode 工序代码
     * @return 预设置列表
     */
    List<ProcessPreset> getByMoldIdAndProcessCode(Long moldId, String processCode);
    
    /**
     * 根据模具ID查询所有预设置
     * @param moldId 模具ID
     * @return 预设置列表
     */
    List<ProcessPreset> getByMoldId(Long moldId);
    
    /**
     * 根据工序代码查询所有预设置
     * @param processCode 工序代码
     * @return 预设置列表
     */
    List<ProcessPreset> getByProcessCode(String processCode);
    
    /**
     * 批量保存预设置
     * @param presets 预设置列表
     * @return 是否保存成功
     */
    boolean saveBatchPresets(List<ProcessPreset> presets);
    
    /**
     * 保存或更新预设置
     * @param preset 预设置
     * @return 是否保存成功
     */
    boolean saveOrUpdatePreset(ProcessPreset preset);
    
    /**
     * 根据模具ID和工序代码删除预设置
     * @param moldId 模具ID
     * @param processCode 工序代码
     * @return 是否删除成功
     */
    boolean deleteByMoldIdAndProcessCode(Long moldId, String processCode);
    
    /**
     * 根据模具ID删除所有预设置
     * @param moldId 模具ID
     * @return 是否删除成功
     */
    boolean deleteByMoldId(Long moldId);
    
    /**
     * 根据预设置键获取预设置值
     * @param moldId 模具ID
     * @param processCode 工序代码
     * @param presetKey 预设置键
     * @return 预设置值
     */
    String getPresetValue(Long moldId, String processCode, String presetKey);
    
    /**
     * 获取预设置映射
     * @param moldId 模具ID
     * @param processCode 工序代码
     * @return 预设置映射
     */
    Map<String, String> getPresetMap(Long moldId, String processCode);
}
