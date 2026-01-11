package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.ProcessPreset;
import com.mold.digitalization.mapper.ProcessPresetMapper;
import com.mold.digitalization.service.ProcessPresetService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工序预设置Service实现
 */
@Service
public class ProcessPresetServiceImpl extends ServiceImpl<ProcessPresetMapper, ProcessPreset> implements ProcessPresetService {
    
    @Override
    public List<ProcessPreset> getByMoldIdAndProcessCode(Long moldId, String processCode) {
        QueryWrapper<ProcessPreset> wrapper = new QueryWrapper<>();
        wrapper.eq("mold_id", moldId)
               .eq("process_code", processCode);
        return list(wrapper);
    }
    
    @Override
    public List<ProcessPreset> getByMoldId(Long moldId) {
        QueryWrapper<ProcessPreset> wrapper = new QueryWrapper<>();
        wrapper.eq("mold_id", moldId);
        return list(wrapper);
    }
    
    @Override
    public List<ProcessPreset> getByProcessCode(String processCode) {
        QueryWrapper<ProcessPreset> wrapper = new QueryWrapper<>();
        wrapper.eq("process_code", processCode);
        return list(wrapper);
    }
    
    @Override
    public boolean saveBatchPresets(List<ProcessPreset> presets) {
        if (presets == null || presets.isEmpty()) {
            return true;
        }
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        for (ProcessPreset preset : presets) {
            preset.setCreateTime(now);
            preset.setUpdateTime(now);
        }
        
        return saveBatch(presets);
    }
    
    @Override
    public boolean saveOrUpdatePreset(ProcessPreset preset) {
        if (preset == null) {
            return false;
        }
        
        // 设置更新时间
        preset.setUpdateTime(LocalDateTime.now());
        
        if (preset.getId() == null) {
            // 新增
            preset.setCreateTime(LocalDateTime.now());
            return baseMapper.insert(preset) > 0;
        } else {
            // 更新
            return baseMapper.updateById(preset) > 0;
        }
    }
    
    @Override
    public boolean deleteByMoldIdAndProcessCode(Long moldId, String processCode) {
        QueryWrapper<ProcessPreset> wrapper = new QueryWrapper<>();
        wrapper.eq("mold_id", moldId)
               .eq("process_code", processCode);
        return baseMapper.delete(wrapper) > 0;
    }
    
    @Override
    public boolean deleteByMoldId(Long moldId) {
        QueryWrapper<ProcessPreset> wrapper = new QueryWrapper<>();
        wrapper.eq("mold_id", moldId);
        return baseMapper.delete(wrapper) > 0;
    }
    
    @Override
    public String getPresetValue(Long moldId, String processCode, String presetKey) {
        List<ProcessPreset> presets = getByMoldIdAndProcessCode(moldId, processCode);
        for (ProcessPreset preset : presets) {
            if (preset.getPresetKey().equals(presetKey)) {
                return preset.getPresetValue();
            }
        }
        return null;
    }
    
    @Override
    public Map<String, String> getPresetMap(Long moldId, String processCode) {
        List<ProcessPreset> presets = getByMoldIdAndProcessCode(moldId, processCode);
        Map<String, String> presetMap = new HashMap<>();
        for (ProcessPreset preset : presets) {
            presetMap.put(preset.getPresetKey(), preset.getPresetValue());
        }
        return presetMap;
    }
}
