package com.mold.process.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ连接姹犻厤缃殑榛樿实现
 * 
 * @author backend team
 */
@Configuration
public class RabbitMQConnectionPoolConfig implements ConnectionPoolConfig {
    
    // 榛樿鍊奸厤缃?
    private static final int DEFAULT_MAX_CONNECTIONS = 50;
    private static final int DEFAULT_INITIAL_CONNECTIONS = 10;
    private static final long DEFAULT_IDLE_TIMEOUT_MS = 60000; // 1鍒嗛挓
    private static final long DEFAULT_CONNECTION_MAX_LIFETIME_MS = 3600000; // 1灏忔椂
    private static final long DEFAULT_ACQUIRE_TIMEOUT_MS = 5000; // 5绉?
    private static final long DEFAULT_HEALTH_CHECK_INTERVAL_MS = 30000; // 30绉?
    
    // 配置灞炴€?   @Value("${spring.rabbitmq.pool.max-connections:50}")
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;
    
    @Value("${spring.rabbitmq.pool.initial-connections:10}")
    private int initialConnections = DEFAULT_INITIAL_CONNECTIONS;
    
    @Value("${spring.rabbitmq.pool.idle-timeout:60000}")
    private long idleTimeoutMs = DEFAULT_IDLE_TIMEOUT_MS;
    
    @Value("${spring.rabbitmq.pool.connection-max-lifetime:3600000}")
    private long connectionMaxLifetimeMs = DEFAULT_CONNECTION_MAX_LIFETIME_MS;
    
    @Value("${spring.rabbitmq.pool.acquire-timeout:5000}")
    private long acquireTimeoutMs = DEFAULT_ACQUIRE_TIMEOUT_MS;
    
    @Value("${spring.rabbitmq.pool.health-check-interval:30000}")
    private long healthCheckIntervalMs = DEFAULT_HEALTH_CHECK_INTERVAL_MS;
    
    /**
     * 获取鏈€澶ц繛鎺ユ暟
     */
    @Override
    public int getMaxConnections() {
        return maxConnections;
    }
    
    /**
     * 获取鍒濆连接鏁?
     */
    @Override
    public int getInitialConnections() {
        return initialConnections;
    }
    
    /**
     * 获取绌洪棽连接瓒呮椂鏃堕棿锛堟绉掞級
     */
    @Override
    public long getIdleTimeoutMs() {
        return idleTimeoutMs;
    }
    
    /**
     * 获取连接鏈€澶у瓨娲绘椂闂达紙姣锛?
     */
    @Override
    public long getConnectionMaxLifetimeMs() {
        return connectionMaxLifetimeMs;
    }
    
    /**
     * 获取获取连接瓒呮椂鏃堕棿锛堟绉掞級
     */
    @Override
    public long getAcquireTimeoutMs() {
        return acquireTimeoutMs;
    }
    
    /**
     * 获取鍋ュ悍妫€鏌ラ棿闅旓紙姣锛?
     */
    @Override
    public long getHealthCheckIntervalMs() {
        return healthCheckIntervalMs;
    }
    
    // 闈欐€佹柟娉曪紝鐢ㄤ簬闈濻pring环境获取榛樿配置
    public static ConnectionPoolConfig getDefaultConfig() {
        return new ConnectionPoolConfig() {
            @Override
            public int getMaxConnections() {
                return DEFAULT_MAX_CONNECTIONS;
            }
            
            @Override
            public int getInitialConnections() {
                return DEFAULT_INITIAL_CONNECTIONS;
            }
            
            @Override
            public long getIdleTimeoutMs() {
                return DEFAULT_IDLE_TIMEOUT_MS;
            }
            
            @Override
            public long getConnectionMaxLifetimeMs() {
                return DEFAULT_CONNECTION_MAX_LIFETIME_MS;
            }
            
            @Override
            public long getAcquireTimeoutMs() {
                return DEFAULT_ACQUIRE_TIMEOUT_MS;
            }
            
            @Override
            public long getHealthCheckIntervalMs() {
                return DEFAULT_HEALTH_CHECK_INTERVAL_MS;
            }
        };
    }
}
