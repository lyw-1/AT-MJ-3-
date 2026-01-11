package com.mold.digitalization.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发环境配置控制器
 * 用于暴露开发环境配置信息，支持前后端配置同步验证
 */
@RestController
@RequestMapping("/api/dev-config")
public class DevConfigController extends BaseController {

    @Value("${app.security.dev.token.enabled:false}")
    private boolean devTokenEnabled;

    @Value("${app.security.dev.token.value:}")
    private String devTokenValue;

    /**
     * 获取开发环境配置信息
     * 用于前后端配置同步验证
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDevConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("devTokenEnabled", devTokenEnabled);
        config.put("devTokenValue", devTokenValue);
        
        return success(config);
    }
}
