package com.mold.digitalization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置类
 * 启用Spring的定时任务功能
 */
@Configuration
@EnableScheduling
public class TaskConfig {
    // 定时任务配置
    // 启用@Scheduled注解的定时任务
}
