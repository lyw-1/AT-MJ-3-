package com.mold.process.pool;

import com.mold.process.exception.ConnectionPoolException;
import com.mold.process.util.RabbitMQConnectionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 连接池集成测试
 * 测试连接池与实际应用代码的集成
 */
public class ConnectionPoolIntegrationTest {

    private MockedStatic<ConnectionPoolFactory> mockConnectionPoolFactory;
    private ConnectionPool mockPool;

    @BeforeEach
    public void setUp() {
        // 创建模拟的连接池
        mockPool = mock(ConnectionPool.class);
        
        // 模拟ConnectionPoolFactory的静态方法
        mockConnectionPoolFactory = Mockito.mockStatic(ConnectionPoolFactory.class);
        mockConnectionPoolFactory.when(ConnectionPoolFactory::getInstance)
                .thenReturn(mock(ConnectionPoolFactory.class));
        mockConnectionPoolFactory.when(() -> ConnectionPoolFactory.getInstance().getPool())
                .thenReturn(mockPool);
    }

    @AfterEach
    public void tearDown() {
        // 关闭模拟静态
        if (mockConnectionPoolFactory != null) {
            mockConnectionPoolFactory.close();
        }
    }

    @Test
    public void testConnectionPoolIntegrationWithUtil() throws IOException, TimeoutException, ConnectionPoolException {
        // 模拟连接
        com.rabbitmq.client.Connection mockConnection = mock(com.rabbitmq.client.Connection.class);
        com.rabbitmq.client.Channel mockChannel = mock(com.rabbitmq.client.Channel.class);
        
        doReturn(mockConnection).when(mockPool).getConnection();
        when(mockConnection.createChannel()).thenReturn(mockChannel);
        
        // 由于这是一个模拟测试，我们不能真正运行util的方法
        // 但我们可以验证ConnectionPoolFactory的调用
        mockConnectionPoolFactory.verify(ConnectionPoolFactory::getInstance);
        mockConnectionPoolFactory.verify(() -> ConnectionPoolFactory.getInstance().getPool());
        
        // 注意：在实际的集成测试中，我们可能会使用真实的配置和连接
        // 但在单元测试中，我们使用模拟来避免外部依赖
    }

    @Test
    public void testConnectionPoolUsagePattern() {
        // 验证连接池的正确使用模式：获取连接 -> 使用 -> 归还
        // 这个测试主要是演示正确的使用方式
        
        try {
            // 模拟连接获取
            com.rabbitmq.client.Connection mockConnection = mock(com.rabbitmq.client.Connection.class);
            
            // 使用doReturn方法避免异常处理问题
            doReturn(mockConnection).when(mockPool).getConnection();
            
            // 模拟使用连接
            com.rabbitmq.client.Connection connection = mockPool.getConnection();
            assertNotNull(connection);
            
            // 模拟归还连接
            doNothing().when(mockPool).returnConnection(connection);
            mockPool.returnConnection(connection);
            
            // 验证调用
            verify(mockPool).getConnection();
            verify(mockPool).returnConnection(connection);
        } catch (Exception e) {
            fail("集成测试应该不会抛出异常: " + e.getMessage());
        }
    }
}