package com.mold.digitalization.controller;

import com.mold.digitalization.entity.ProcessPreset;
import com.mold.digitalization.service.ProcessPresetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工序预设置控制器
 */
@RestController
@RequestMapping("/api/process-preset")
public class ProcessPresetController extends BaseController {
    
    @Autowired
    private ProcessPresetService processPresetService;
    
    /**
     * 获取预设置列表
     * @param moldId 模具ID
     * @param processCode 工序代码
     * @return 预设置列表
     */
    @GetMapping
    public ResponseEntity<List<ProcessPreset>> list(
            @RequestParam(required = false) Long moldId,
            @RequestParam(required = false) String processCode) {
        if (moldId != null && processCode != null) {
            return success(processPresetService.getByMoldIdAndProcessCode(moldId, processCode));
        } else if (moldId != null) {
            return success(processPresetService.getByMoldId(moldId));
        } else if (processCode != null) {
            return success(processPresetService.getByProcessCode(processCode));
        } else {
            return success(processPresetService.list());
        }
    }
    
    /**
     * 获取预设置详情
     * @param id 预设置ID
     * @return 预设置详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcessPreset> getById(@PathVariable Long id) {
        ProcessPreset preset = processPresetService.getById(id);
        if (preset == null) {
            return notFoundGeneric("预设置不存在");
        }
        return success(preset);
    }
    
    /**
     * 创建预设置
     * @param preset 预设置信息
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<ProcessPreset> create(@RequestBody ProcessPreset preset) {
        boolean success = processPresetService.saveOrUpdatePreset(preset);
        if (success) {
            return success(preset);
        } else {
            return notFoundGeneric("创建预设置失败");
        }
    }
    
    /**
     * 批量创建预设置
     * @param presets 预设置列表
     * @return 创建结果
     */
    @PostMapping("/batch")
    public ResponseEntity<Void> createBatch(@RequestBody List<ProcessPreset> presets) {
        boolean success = processPresetService.saveBatchPresets(presets);
        if (success) {
            return success();
        } else {
            return notFoundGeneric("批量创建预设置失败");
        }
    }
    
    /**
     * 更新预设置
     * @param id 预设置ID
     * @param preset 预设置信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcessPreset> update(@PathVariable Long id, @RequestBody ProcessPreset preset) {
        preset.setId(id);
        boolean success = processPresetService.saveOrUpdatePreset(preset);
        if (success) {
            return success(preset);
        } else {
            return notFoundGeneric("更新预设置失败");
        }
    }
    
    /**
     * 删除预设置
     * @param id 预设置ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean success = processPresetService.removeById(id);
        if (success) {
            return success();
        } else {
            return notFoundGeneric("删除预设置失败");
        }
    }
    
    /**
     * 根据模具ID和工序代码删除预设置
     * @param moldId 模具ID
     * @param processCode 工序代码
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteByMoldIdAndProcessCode(
            @RequestParam Long moldId,
            @RequestParam String processCode) {
        boolean success = processPresetService.deleteByMoldIdAndProcessCode(moldId, processCode);
        if (success) {
            return success();
        } else {
            return notFoundGeneric("删除预设置失败");
        }
    }
    
    /**
     * 获取预设置映射
     * @param moldId 模具ID
     * @param processCode 工序代码
     * @return 预设置映射
     */
    @GetMapping("/map")
    public ResponseEntity<Map<String, String>> getPresetMap(
            @RequestParam Long moldId,
            @RequestParam String processCode) {
        return success(processPresetService.getPresetMap(moldId, processCode));
    }
    
    /**
     * 获取预设置值
     * @param moldId 模具ID
     * @param processCode 工序代码
     * @param presetKey 预设置键
     * @return 预设置值
     */
    @GetMapping("/value")
    public ResponseEntity<Map<String, String>> getPresetValue(
            @RequestParam Long moldId,
            @RequestParam String processCode,
            @RequestParam String presetKey) {
        String value = processPresetService.getPresetValue(moldId, processCode, presetKey);
        return success(Map.of("value", value));
    }
}
