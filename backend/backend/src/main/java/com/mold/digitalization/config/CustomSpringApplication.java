package com.mold.digitalization.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 自定义Spring Application类
 * 用于跳过对特定Runner的调用
 */
public class CustomSpringApplication extends SpringApplication {

    /**
     * 构造方法
     * @param primarySources 主要源
     */
    public CustomSpringApplication(Class<?>... primarySources) {
        super(primarySources);
        this.setWebApplicationType(WebApplicationType.SERVLET);
    }

    /**
     * 运行应用
     * @param args 命令行参数
     * @return 应用上下文
     */
    @Override
    public ConfigurableApplicationContext run(String... args) {
        // 调用父类的run方法
        return super.run(args);
    }
}
