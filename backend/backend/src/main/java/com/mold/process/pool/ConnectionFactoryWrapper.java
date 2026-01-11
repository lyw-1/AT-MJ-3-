package com.mold.process.pool;

import com.mold.process.config.RabbitMQConfig;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 连接工厂包装类，封装RabbitMQ连接工厂，提供连接创建功能
 * 
 * @author backend team
 */
public class ConnectionFactoryWrapper {
    
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactoryWrapper.class);
    private final ConnectionFactory factory;
    
    /**
     * 创建连接工厂包装器
     */
    public ConnectionFactoryWrapper() {
        this.factory = new ConnectionFactory();
        initializeFactory();
    }
    
    /**
     * 初始化连接工厂配置
     */
    private void initializeFactory() {
        // 从配置获取RabbitMQ连接参数
        RabbitMQConfig config = new RabbitMQConfig();
        
        // 设置连接参数
        factory.setHost(config.getHost());
        factory.setPort(config.getPort());
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        
        // 设置虚拟主机（如果配置了的话）
        String virtualHostValue = config.getVirtualHost();
        if (virtualHostValue != null && !virtualHostValue.isEmpty()) {
            factory.setVirtualHost(virtualHostValue);
        }
        
        // 设置鍏朵粬连接灞烇拷?        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(5000); // 5绉掓仮澶嶉棿锟?        
        logger.info("RabbitMQ连接宸ュ巶宸插垵濮嬪寲锛屼富锟? {}, 绔彛: {}", config.getHost(), config.getPort());
    }
    
    /**
     * 创建鏂扮殑RabbitMQ连接
     * @return 鏂板垱寤虹殑连接
     * @throws IOException IO开傚父
     * @throws TimeoutException 瓒呮椂开傚父
     */
    public Connection createConnection() throws IOException, TimeoutException {
        logger.debug("姝ｅ湪创建鏂扮殑RabbitMQ连接");
        Connection connection = factory.newConnection();
        logger.debug("成功创建RabbitMQ连接: {}", connection.hashCode());
        return connection;
    }
    
    /**
     * 获取连接宸ュ巶锛堜緵测试浣跨敤锟?     * @return 连接宸ュ巶实例
     */
    ConnectionFactory getFactory() {
        return factory;
    }
}
