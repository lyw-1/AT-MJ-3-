package com.mold.digitalization.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动Runner
 * 用于在应用启动后执行一些初始化操作
 */
@Component
public class AppStartupRunner implements CommandLineRunner {

    /**
     * 应用启动后执行
     * @param args 命令行参数
     * @throws Exception 执行过程中可能抛出的异常
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("应用启动完成，执行初始化操作...");
    }
}
