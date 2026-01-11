package com.mold.process.util;

import com.mold.process.config.RabbitMQConfig;
import com.mold.process.exception.ConnectionAcquireException;
import com.mold.process.exception.ConnectionPoolException;
import com.mold.process.exception.ConnectionReturnException;
import com.mold.process.pool.ConnectionFactoryWrapper;
import com.mold.process.pool.ConnectionPool;
import com.mold.process.pool.ConnectionPoolFactory;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ连接宸ュ叿绫? * 鎻愪緵连接测试銆佽祫婧愮鐞嗙瓑功能
 */
public class RabbitMQConnectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConnectionUtil.class);
    
    // 浣跨敤配置绫昏幏鍙栬繛鎺ュ弬鏁?
    private static String getHost() {
        return RabbitMQConfig.getStaticHost();
    }
    
    private static int getPort() {
        return RabbitMQConfig.getStaticPort();
    }
    
    private static String getUsername() {
        return RabbitMQConfig.getStaticUsername();
    }
    
    private static String getPassword() {
        return RabbitMQConfig.getStaticPassword();
    }
    
    private static String getVirtualHost() {
        return RabbitMQConfig.getStaticVirtualHost();
    }
    
    /**
 * 测试涓嶳abbitMQ服务鍣ㄧ殑连接
 * @param host 涓绘満鍦板潃
 * @param port 绔彛鍙? * @param username 用户名? * @param password 密码
 * @param virtualHost 铏氭嫙涓绘満
 * @return 连接鐘舵€佸璞? */
    public static ConnectionStatus testConnection(String host, int port, String username, String password, String virtualHost) {
        ConnectionStatus status = new ConnectionStatusImpl(host, port, username);
        Connection connection = null;
        Channel channel = null;
        
        try {
            try {
                // 浣跨敤连接姹犺幏鍙栬繛鎺?
                ConnectionPool connectionPool = ConnectionPoolFactory.getInstance().getConnectionPool();
                connection = connectionPool.getConnection();
            } catch (ConnectionPoolException e) {
                logger.error("浠庤繛鎺ユ睜获取连接失败", e);
                throw new IOException("鏃犳硶获取RabbitMQ连接", e);
            }
            logger.info("成功连接鍒癛abbitMQ服务鍣?{}:{}", host, port);
            System.out.println("鉁?成功连接鍒癛abbitMQ服务鍣?" + host + ":" + port);
            
            // 创建閫氶亾
            channel = connection.createChannel();
            logger.info("成功创建RabbitMQ閫氶亾");
            
            // 灏濊瘯获取鍩烘湰淇℃伅
            try {
                // 鍒楀嚭鍙敤鐨勪氦鎹㈡満
                channel.exchangeDeclarePassive("amq.direct");
                ((ConnectionStatusImpl)status).addExchange("amq.direct");
                
                channel.exchangeDeclarePassive("amq.topic");
                ((ConnectionStatusImpl)status).addExchange("amq.topic");
                
                logger.info("Successfully found default exchanges");
            System.out.println("Successfully found default exchanges");
            } catch (Exception e) {
                logger.warn("获取浜ゆ崲鏈轰俊鎭椂鍑洪敊: {}", e.getMessage());
            }
            
            // 鏍囪连接成功
            status.setConnected(true);
            
        } catch (Exception e) {
            logger.error("连接RabbitMQ失败", e);
            System.err.println("鉁?连接RabbitMQ失败: " + e.getMessage());
            status.setLastError(e);
        } finally {
            // 关闭璧勬簮
            closeResources(channel, connection);
        }
        
        return status;
    }
    
    /**
 * 浣跨敤榛樿配置测试连接
 * @return 连接鐘舵€佸璞? */
    public static ConnectionStatus testConnection() {
        String host = getHost();
        int port = getPort();
        String username = getUsername();
        
        System.out.println("姝ｅ湪测试RabbitMQ连接...");
        System.out.println("连接淇℃伅: host=" + host + ", port=" + port + ", username=" + username);
        
        logger.info("浣跨敤榛樿配置测试RabbitMQ连接");
        return testConnection(host, port, username, getPassword(), getVirtualHost());
    }
    
    /**
 * 鍙戦€佹祴璇曟秷鎭? * @param status 连接鐘舵€佸璞? * @param message 测试娑堟伅内容
 * @return 鏄惁鍙戦€佹垚鍔? */
    public static boolean sendTestMessage(ConnectionStatus status, String message) {
        if (!status.isConnected()) {
            logger.warn("Cannot send test message: not connected");
            System.out.println("Cannot send test message: not connected");
            return false;
        }
        
        Connection connection = null;
        Channel channel = null;
        boolean sent = false;
        
        try {
            try {
                // 浣跨敤连接姹犺幏鍙栬繛鎺?
                ConnectionPool connectionPool = ConnectionPoolFactory.getInstance().getConnectionPool();
                connection = connectionPool.getConnection();
            } catch (ConnectionPoolException e) {
                logger.error("浠庤繛鎺ユ睜获取连接失败", e);
                throw new IOException("鏃犳硶获取RabbitMQ连接", e);
            }
            channel = connection.createChannel();

            // 澹版槑涓存椂闃熷垪
            String queueName = channel.queueDeclare().getQueue();

            // 鍙戦€佹祴璇曟秷鎭? 
            String exchangeName = "";
            String routingKey = queueName;
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());

            logger.info("测试娑堟伅鍙戦€佹垚鍔?{}", message);
            System.out.println("鉁?测试娑堟伅鍙戦€佹垚鍔?" + message);
            sent = true;
            
        } catch (Exception e) {
            logger.error("Failed to send test message", e);
            System.err.println("Failed to send test message: " + e.getMessage());
            status.setLastError(e);
        } finally {
            closeResources(channel, connection);
        }
        
        return sent;
    }
    
    /**
     * 关闭璧勬簮
     * @param channel 閫氶亾瀵硅薄
     * @param connection 连接瀵硅薄
     */
    private static void closeResources(Channel channel, Connection connection) {
        try {
                if (channel != null && channel.isOpen()) {
                channel.close();
                logger.info("RabbitMQ channel closed");
            }
        } catch (Exception e) {
            logger.error("Error closing RabbitMQ channel", e);
        }
        
        try {
            if (connection != null) {
                // 褰掕繕连接鍒拌繛鎺ユ睜锛岃€屼笉鏄洿鎺ュ叧闂?
                ConnectionPool connectionPool = ConnectionPoolFactory.getInstance().getConnectionPool();
                connectionPool.returnConnection(connection);
                logger.info("Returned connection to RabbitMQ pool");
            }
        } catch (ConnectionPoolException e) {
            logger.error("Error returning connection to pool", e);
            // 濡傛灉褰掕繕失败锛屽皾璇曠洿鎺ュ叧闂繛鎺?
            try {
                if (connection != null && connection.isOpen()) {
                    connection.close();
                    logger.warn("连接褰掕繕失败锛屽凡鐩存帴关闭连接");
                }
            } catch (Exception closeEx) {
                logger.error("鐩存帴关闭连接鏃朵篃鍑洪敊", closeEx);
            }
        }
    }
    
    /**
 * 妫€鏌abbitMQ服务鏄惁可达
 * @param host 涓绘満鍦板潃
 * @param port 绔彛鍙? * @return 鏄惁可达
 */
    public static boolean isServerReachable(String host, int port) {
        try (java.net.Socket socket = new java.net.Socket()) {
            socket.connect(new java.net.InetSocketAddress(host, port), 3000);
            logger.info("RabbitMQ服务鍣ㄥ彲杈?{}:{}", host, port);
            return true;
        } catch (Exception e) {
            logger.warn("RabbitMQ服务鍣ㄤ笉可达: {}:{}", host, port);
            System.out.println("鉁?RabbitMQ服务鍣ㄤ笉可达: " + host + ":" + port);
            return false;
        }
    }
    
    /**
     * 鍘熷鐨勬祴璇曟柟娉曪紝淇濇寔鍚戝悗鍏煎
     * @return 鏄惁连接成功
     */
    public static boolean testConnectionLegacy() {
        ConnectionStatus status = testConnection();
        return status.isConnected();
    }
    
    /**
     * 涓绘柟娉曪紝鐢ㄤ簬鐩存帴杩愯测试
     */
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("RABBITMQ连接验证");
        System.out.println("==========================================");
        
        // 1. 妫€鏌ユ湇鍔″櫒鏄惁可达
        System.out.println("1. 妫€鏌ユ湇鍔″櫒鏄惁可达...");
        boolean reachable = isServerReachable(getHost(), getPort());
        if (!reachable) {
            System.out.println("\n测试结果: 失败 - 服务鍣ㄤ笉可达");
            System.exit(1);
        }
        
        // 2. 测试连接
        System.out.println("\n2. 测试RabbitMQ连接...");
        ConnectionStatus status = testConnection();
        
        // 3. 濡傛灉连接成功锛屽皾璇曞彂閫佹祴璇曟秷鎭?
        if (status.isConnected()) {
            System.out.println("\n3. 灏濊瘯鍙戦€佹祴璇曟秷鎭?..");
            sendTestMessage(status, "Hello RabbitMQ! Connection test successful.");
        }
        
        System.out.println("\n==========================================");
        System.out.println("连接璇︽儏:");
        System.out.println(status.getConnectionDetails());
        System.out.println("==========================================");
        
        System.out.println("\n测试结果: " + (status.isConnected() ? "閫氳繃" : "失败"));
        System.exit(status.isConnected() ? 0 : 1);
    }
}
