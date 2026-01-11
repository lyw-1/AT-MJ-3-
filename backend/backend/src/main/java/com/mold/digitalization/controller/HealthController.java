package com.mold.digitalization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 鍋ュ悍妫€鏌ユ帶鍒跺櫒
 * 鎻愪緵系统鍋ュ悍鐘舵€佹鏌ョ鐐? */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * 系统鍋ュ悍妫€鏌ョ鐐?     * @return 鍋ュ悍鐘舵€佷俊鎭?     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", LocalDateTime.now());
        healthInfo.put("application", "Mold Digitalization System");
        return healthInfo;
    }
}
