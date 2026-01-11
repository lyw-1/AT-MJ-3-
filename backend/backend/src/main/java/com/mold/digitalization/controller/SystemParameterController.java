package com.mold.digitalization.controller;

import com.mold.digitalization.annotation.SensitiveOperation;
import com.mold.digitalization.entity.SystemParameter;
import com.mold.digitalization.service.SystemParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 系统参数控制器
 * 处理系统参数相关的HTTP请求
 */
@RestController
@RequestMapping("/api/system-parameters")
public class SystemParameterController extends BaseController {
    
    @Autowired
    private SystemParameterService systemParameterService;
    
    /**
     * 根据系统参数ID获取系统参数信息
     * @param id 系统参数ID
     * @return 系统参数信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemParameter> getSystemParameterById(@PathVariable Long id) {
        SystemParameter parameter = systemParameterService.getById(id);
        if (parameter != null) {
            return success(parameter);
        } else {
            return notFoundGeneric("System parameter not found");
        }
    }
    
    /**
     * 根据参数键名获取系统参数信息
     * @param paramKey 参数键名
     * @return 系统参数信息
     */
    @GetMapping("/key/{paramKey}")
    public ResponseEntity<SystemParameter> getSystemParameterByKey(@PathVariable String paramKey) {
        SystemParameter parameter = systemParameterService.getSystemParameterByKey(paramKey);
        if (parameter != null) {
            return success(parameter);
        } else {
            return notFoundGeneric("System parameter not found");
        }
    }
    
    /**
     * 根据参数类型获取系统参数列表
     * @param paramType 参数类型
     * @return 系统参数列表
     */
    @GetMapping("/type/{paramType}")
    public ResponseEntity<List<SystemParameter>> getSystemParametersByType(@PathVariable String paramType) {
        List<SystemParameter> parameters = systemParameterService.getSystemParametersByType(paramType);
        return success(parameters);
    }
    
    /**
     * 创建新系统参数
     * @param parameter 系统参数信息
     * @return 创建的系统参数信息
     */
    @PostMapping
    @SensitiveOperation(level = "high", description = "Create system parameter")
    public ResponseEntity<SystemParameter> createSystemParameter(@RequestBody SystemParameter parameter) {
        boolean created = systemParameterService.createSystemParameter(parameter);
        if (created) {
            return success(parameter);
        } else {
            return notFoundGeneric("创建系统参数失败");
        }
    }
    
    /**
     * 更新系统参数信息
     * @param id 系统参数ID
     * @param parameter 系统参数信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @SensitiveOperation(level = "high", description = "Update system parameter")
    public ResponseEntity<Void> updateSystemParameter(@PathVariable Long id, @RequestBody SystemParameter parameter) {
        parameter.setId(id);
        boolean updated = systemParameterService.updateSystemParameter(parameter);
        if (updated) {
            return success();
        } else {
            return notFoundGeneric("System parameter not found or update failed");
        }
    }
    
    /**
     * 删除系统参数
     * @param id 系统参数ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @SensitiveOperation(level = "critical", description = "删除系统参数操作")
    public ResponseEntity<Void> deleteSystemParameter(@PathVariable Long id) {
        boolean deleted = systemParameterService.deleteSystemParameter(id);
        if (deleted) {
            return success();
        } else {
            return notFoundGeneric("System parameter not found or delete failed");
        }
    }
    
    /**
     * 获取所有系统参数列表
     * @return 系统参数列表
     */
    @GetMapping
    public ResponseEntity<List<SystemParameter>> getAllSystemParameters() {
        List<SystemParameter> parameters = systemParameterService.getAllSystemParameters();
        return success(parameters);
    }
}
