package com.mold.process.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.InitializingBean;

/**
 * RabbitMQ配置类
 * 从application.properties读取RabbitMQ连接参数
 */
@Component
@Configuration
public class RabbitMQConfig implements InitializingBean {
    
    @Value("${spring.rabbitmq.host:localhost}")
    private String host;
    
    @Value("${spring.rabbitmq.port:5672}")
    private int port;
    
    @Value("${spring.rabbitmq.username:guest}")
    private String username;
    
    @Value("${spring.rabbitmq.password:guest}")
    private String password;
    
    @Value("${spring.rabbitmq.virtual-host:/}")
    private String virtualHost;
    
    // Getters
    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    /**
     * 获取RabbitMQ虚拟主机
     * @return 虚拟主机
     */
    public String getVirtualHost() {
        return virtualHost;
    }
    
    // 用于非Spring环境下的静态访问方法
    // 修复：声明 static 变量 instance，初始化为 null
    private static RabbitMQConfig instance = null;
    
    @Override
    public void afterPropertiesSet() {
        // Spring初始化时自动设置实例
        instance = this;
    }
    
    public void setInstance() {
        instance = this;
    }
    
    public static String getStaticHost() {
        return instance != null ? instance.getHost() : "localhost";
    }
    
    public static int getStaticPort() {
        return instance != null ? instance.getPort() : 5672;
    }
    
    public static String getStaticUsername() {
        return instance != null ? instance.getUsername() : "guest";
    }
    
    public static String getStaticPassword() {
        return instance != null ? instance.getPassword() : "guest";
    }
    
    public static String getStaticVirtualHost() {
        return instance != null ? instance.getVirtualHost() : "/";
    }
}
