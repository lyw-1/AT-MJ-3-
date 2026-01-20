package com.mold.digitalization.controller.v1;

import com.mold.digitalization.dto.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例控制器 - 展示API版本控制规范
 * 
 * 使用说明：
 * 1. 所有新Controller请放在controller/v1包下
 * 2. 使用@RequestMapping("/api/v1/")统一版本前缀
 * 3. 遵循RESTful设计规范
 * 4. 保持向后兼容性
 */
@RestController
@RequestMapping("/api/v1/examples")  // 统一使用v1版本前缀
@Api(tags = "示例管理")
public class ExampleController {

    /**
     * 获取示例列表
     * GET /api/v1/examples
     */
    @GetMapping
    @ApiOperation("获取示例列表")
    public ResponseDTO<Map<String, Object>> getExamples() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "这是API版本控制的示例");
        data.put("version", "v1");
        data.put("timestamp", System.currentTimeMillis());
        
        return ResponseDTO.success(data);
    }

    /**
     * 获取单个示例
     * GET /api/v1/examples/{id}
     */
    @GetMapping("/{id}")
    @ApiOperation("获取单个示例")
    public ResponseDTO<Map<String, Object>> getExample(@PathVariable Long id) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", "示例项目");
        data.put("description", "这是API版本控制演示项目");
        
        return ResponseDTO.success(data);
    }

    /**
     * 创建示例
     * POST /api/v1/examples
     */
    @PostMapping
    @ApiOperation("创建示例")
    public ResponseDTO<Map<String, Object>> createExample(@RequestBody Map<String, Object> request) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", System.currentTimeMillis());
        data.put("created", true);
        
        return ResponseDTO.success(data, "创建成功");
    }

    /**
     * 更新示例
     * PUT /api/v1/examples/{id}
     */
    @PutMapping("/{id}")
    @ApiOperation("更新示例")
    public ResponseDTO<Map<String, Object>> updateExample(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> request) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("updated", true);
        
        return ResponseDTO.success(data, "更新成功");
    }

    /**
     * 删除示例
     * DELETE /api/v1/examples/{id}
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除示例")
    public ResponseDTO<String> deleteExample(@PathVariable Long id) {
        return ResponseDTO.success(null, "删除成功");
    }
}
