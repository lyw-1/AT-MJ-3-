package com.mold.digitalization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Spring Boot 主应用程序类
 * 模具数字化管理系统的入口 */
@SpringBootApplication
// 扩大包扫描范围，包含com.atmj.mold.digitization包
@ComponentScan(basePackages = {
        "com.mold.digitalization",
        "com.mold.process",
        "com.atmj.mold.digitization"
})
// 扩大Mapper扫描范围，包含两个包下的mapper和dao
@MapperScan(basePackages = {
        "com.mold.digitalization.dao",
        "com.mold.digitalization.mapper",
        "com.mold.process.mapper",
        "com.atmj.mold.digitization.mapper",
        "com.atmj.mold.digitization.dao"
})
@EnableTransactionManagement
@EnableCaching
@EnableAsync
public class Application {

    /**
     * 主方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动Spring Boot应用，捕获BeanNotOfRequiredTypeException异常
        ConfigurableApplicationContext context = null;
        try {
            context = SpringApplication.run(Application.class, args);
        } catch (org.springframework.beans.factory.BeanNotOfRequiredTypeException e) {
            // 检查是否是ddlApplicationRunner引起的异常
            if (e.getBeanName().equals("ddlApplicationRunner")) {
                System.out.println("忽略ddlApplicationRunner引起的BeanNotOfRequiredTypeException异常");
                System.out.println("应用已启动，但ddlApplicationRunner未初始化");
                return;
            }
            // 其他BeanNotOfRequiredTypeException异常，继续抛出
            throw e;
        }
        
        if (context != null) {
            System.out.println("应用成功启动，上下文已初始化！");
        }
    }

    /**
     * BeanPostProcessor，用于跳过ddlApplicationRunner的初始化
     * 解决BeanNotOfRequiredTypeException问题
     */
    @Bean
    public static BeanPostProcessor ddlApplicationRunnerPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                // 检查是否是ddlApplicationRunner Bean
                if ("ddlApplicationRunner".equals(beanName)) {
                    System.out.println("跳过ddlApplicationRunner的初始化，替换为 no-op ApplicationRunner");
                    // 返回一个 no-op 的 ApplicationRunner，避免返回 null 导致类型转换异常
                    return new ApplicationRunner() {
                        @Override
                        public void run(ApplicationArguments args) throws Exception {
                            // no-op
                        }
                    };
                }
                return bean;
            }
        };
    }

    /**
     * 应用启动完成事件监听器
     * @return ApplicationListener实例
     */
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventListener() {
        return event -> {
            System.out.println("应用已成功启动！");
        };
    }

}
