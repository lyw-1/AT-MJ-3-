package com.mold.digitalization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工序模板控制器
 */
@RestController
@RequestMapping("/api/process/templates")
public class ProcessTemplateController extends BaseController {

    /**
     * 获取工序模板列表
     */
    @GetMapping
    public ResponseEntity<?> list() {
        return success(Map.of(
                "data", List.of(
                        Map.of(
                                "id", 1,
                                "code", "TEMPLATE_001",
                                "name", "标准模板",
                                "description", "标准工序模板",
                                "category", "standard",
                                "fields", List.of(
                                        Map.of(
                                                "id", 1,
                                                "fieldName", "temperature",
                                                "fieldLabel", "温度",
                                                "fieldType", "number",
                                                "required", true,
                                                "defaultValue", 200,
                                                "min", 100,
                                                "max", 500,
                                                "step", 5
                                        ),
                                        Map.of(
                                                "id", 2,
                                                "fieldName", "pressure",
                                                "fieldLabel", "压力",
                                                "fieldType", "number",
                                                "required", true,
                                                "defaultValue", 10,
                                                "min", 1,
                                                "max", 50,
                                                "step", 1
                                        )
                                ),
                                "status", "active",
                                "createdAt", "2026-01-18T00:00:00",
                                "updatedAt", "2026-01-18T00:00:00"
                        )
                )
        ));
    }

    /**
     * 获取所有工序模板（不分页）
     */
    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return success(Map.of(
                "data", List.of(
                        Map.of(
                                "id", 1,
                                "code", "TEMPLATE_001",
                                "name", "标准模板",
                                "description", "标准工序模板",
                                "category", "standard",
                                "fields", List.of(
                                        Map.of(
                                                "id", 1,
                                                "fieldName", "temperature",
                                                "fieldLabel", "温度",
                                                "fieldType", "number",
                                                "required", true,
                                                "defaultValue", 200,
                                                "min", 100,
                                                "max", 500,
                                                "step", 5
                                        ),
                                        Map.of(
                                                "id", 2,
                                                "fieldName", "pressure",
                                                "fieldLabel", "压力",
                                                "fieldType", "number",
                                                "required", true,
                                                "defaultValue", 10,
                                                "min", 1,
                                                "max", 50,
                                                "step", 1
                                        )
                                ),
                                "status", "active",
                                "createdAt", "2026-01-18T00:00:00",
                                "updatedAt", "2026-01-18T00:00:00"
                        )
                )
        ));
    }

    /**
     * 获取工序模板详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        return success(Map.of(
                "data", Map.of(
                        "id", id,
                        "code", "TEMPLATE_001",
                        "name", "标准模板",
                        "description", "标准工序模板",
                        "category", "standard",
                        "fields", List.of(
                                Map.of(
                                        "id", 1,
                                        "fieldName", "temperature",
                                        "fieldLabel", "温度",
                                        "fieldType", "number",
                                        "required", true,
                                        "defaultValue", 200,
                                        "min", 100,
                                        "max", 500,
                                        "step", 5
                                ),
                                Map.of(
                                        "id", 2,
                                        "fieldName", "pressure",
                                        "fieldLabel", "压力",
                                        "fieldType", "number",
                                        "required", true,
                                        "defaultValue", 10,
                                        "min", 1,
                                        "max", 50,
                                        "step", 1
                                )
                        ),
                        "status", "active",
                        "createdAt", "2026-01-18T00:00:00",
                        "updatedAt", "2026-01-18T00:00:00"
                )
        ));
    }

    /**
     * 获取工序模板类型
     */
    @GetMapping("/categories")
    public ResponseEntity<?> categories() {
        return success(Map.of(
                "data", List.of(
                        Map.of("label", "标准模板", "value", "standard"),
                        Map.of("label", "自定义模板", "value", "custom")
                )
        ));
    }

    /**
     * 创建工序模板
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> data) {
        return success(data);
    }

    /**
     * 更新工序模板
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return success(Map.of("id", id, "updated", true));
    }

    /**
     * 删除工序模板
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return success(Map.of("id", id, "deleted", true));
    }

    /**
     * 批量删除工序模板
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> data) {
        return success(Map.of("ids", data.get("ids"), "deleted", true));
    }
}