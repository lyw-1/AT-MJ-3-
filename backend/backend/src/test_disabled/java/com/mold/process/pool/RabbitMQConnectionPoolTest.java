package com.mold.process.pool;

import com.mold.process.config.ConnectionPoolConfig;
import com.mold.process.exception.ConnectionAcquireException;
import com.mold.process.exception.ConnectionPoolException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * RabbitMQ连接池单元测试
 */
public class RabbitMQConnectionPoolTest {

    @Mock
    private ConnectionFactoryWrapper mockConnectionFactory;

    @Mock
    private com.rabbitmq.client.Connection mockConnection;

    private RabbitMQConnectionPool pool;
    private AutoCloseable mockCloseable;

    @BeforeEach
    public void setUp() throws Exception {
        mockCloseable = MockitoAnnotations.openMocks(this);
        
        // 创建测试配置
        ConnectionPoolConfig testConfig = new ConnectionPoolConfig() {
            @Override
            public int getMaxConnections() {
                return 5;
            }
            
            @Override
            public int getInitialConnections() {
                return 2;
            }
            
            @Override
            public long getIdleTimeoutMs() {
                return 10000;
            }
            
            @Override
            public long getConnectionMaxLifetimeMs() {
                return 60000;
            }
            
            @Override
            public long getAcquireTimeoutMs() {
                return 3000;
            }
            
            @Override
            public long getHealthCheckIntervalMs() {
                return 20000;
            }
        };
        
        // 设置mock琛屼负 - 姣忔璋冪敤返回涓嶅悓鐨刴ock连接锛屽苟配置连接涓烘墦开€状态        com.rabbitmq.client.Connection mockConnection2 = mock(com.rabbitmq.client.Connection.class);
        when(mockConnection.isOpen()).thenReturn(true);
        when(mockConnection2.isOpen()).thenReturn(true);
        when(mockConnectionFactory.createConnection()).thenReturn(mockConnection, mockConnection2);
        
        // 创建连接池
        pool = new RabbitMQConnectionPool(testConfig, mockConnectionFactory);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (pool != null) {
            pool.close();
        }
        if (mockCloseable != null) {
            mockCloseable.close();
        }
    }

    @Test
    public void testInitialize() throws Exception {
        // 测试初始化
        pool.initialize();
        
        // 验证初始化创建了初始连接数
        verify(mockConnectionFactory, times(2)).createConnection();
        
        // 验证状态        PoolStatus status = pool.getStatus();
        assertEquals(2, status.getIdleConnections());
        assertEquals(0, status.getActiveConnections());
        assertEquals(2, status.getTotalConnections());
    }

    @Test
    public void testGetConnection() throws Exception {
        // 初始化连接池
        pool.initialize();
        
        // 获取连接
        com.rabbitmq.client.Connection connection1 = pool.getConnection();
        com.rabbitmq.client.Connection connection2 = pool.getConnection();
        
        // 验证连接非空涓旂姸鎬佹纭?        assertNotNull(connection1);
        assertNotNull(connection2);
        
        PoolStatus status = pool.getStatus();
        assertEquals(0, status.getIdleConnections());
        assertEquals(2, status.getActiveConnections());
        assertEquals(2, status.getTotalConnections());
    }

    @Test
    public void testReturnConnection() throws Exception {
        // 初始化连接池
        pool.initialize();
        
        // 获取骞跺綊杩樿繛鎺?        com.rabbitmq.client.Connection connection = pool.getConnection();
        pool.returnConnection(connection);
        
        // 验证状态        PoolStatus status = pool.getStatus();
        assertEquals(2, status.getIdleConnections()); // 褰掕繕鍚庣┖闂茶繛鎺ユ暟应该鍥炲埌2
        assertEquals(0, status.getActiveConnections());
        assertEquals(2, status.getTotalConnections());
    }

    @Test
    public void testAcquireTimeout() throws Exception {
        // 创建一个最大连接数为1的配置，初始连接数为1
        ConnectionPoolConfig smallConfig = new ConnectionPoolConfig() {
            @Override
            public int getMaxConnections() {
                return 1;
            }
            
            @Override
            public int getInitialConnections() {
                return 1;
            }
            
            @Override
            public long getIdleTimeoutMs() {
                return 10000;
            }
            
            @Override
            public long getConnectionMaxLifetimeMs() {
                return 60000;
            }
            
            @Override
            public long getAcquireTimeoutMs() {
                return 100;
            }
            
            @Override
            public long getHealthCheckIntervalMs() {
                return 20000;
            }
        };
        
        // 模拟连接创建寤惰繜
        when(mockConnectionFactory.createConnection()).thenAnswer(invocation -> {
            Thread.sleep(200); // 瓒呰繃瓒呮椂鏃堕棿
            return mockConnection;
        });
        
        RabbitMQConnectionPool smallPool = new RabbitMQConnectionPool(smallConfig, mockConnectionFactory);
        smallPool.initialize();
        
        // 获取第一个连接（应该成功）        com.rabbitmq.client.Connection connection1 = smallPool.getConnection();
        assertNotNull(connection1);
        
        // 尝试获取第二个连接（应该超时，因为最大连接数为1）        assertThrows(ConnectionAcquireException.class, () -> {
            smallPool.getConnection();
        });
        
        smallPool.close();
    }

    @Test
    public void testWarmUp() throws Exception {
        // 预热连接池
        pool.prewarm(2);
        
        // 验证创建了初始连接
        verify(mockConnectionFactory, times(2)).createConnection();
    }

    @Test
    public void testClose() throws Exception {
        // 初始化并关闭
        pool.initialize();
        
        // 纭繚连接鏄墦开€鐨?        when(mockConnection.isOpen()).thenReturn(true);
        
        pool.close();
        
        // 验证连接琚叧闂?        // 鐢变簬连接姹犲垵濮嬪寲鏃跺垱寤轰簡2涓繛鎺ワ紝关闭鏃舵瘡涓繛鎺ラ兘浼氳皟鐢╟lose()
        // 注意：mockConnection2在setUp方法中创建，但这里我们只验证mockConnection的关闭
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testMaxConnections() throws Exception {
        // 创建一个最大连接数为1的配置
        ConnectionPoolConfig maxConfig = new ConnectionPoolConfig() {
            @Override
            public int getMaxConnections() {
                return 2;
            }
            
            @Override
            public int getInitialConnections() {
                return 0;
            }
            
            @Override
            public long getIdleTimeoutMs() {
                return 10000;
            }
            
            @Override
            public long getConnectionMaxLifetimeMs() {
                return 60000;
            }
            
            @Override
            public long getAcquireTimeoutMs() {
                return 3000;
            }
            
            @Override
            public long getHealthCheckIntervalMs() {
                return 20000;
            }
        };
        
        RabbitMQConnectionPool maxPool = new RabbitMQConnectionPool(maxConfig, mockConnectionFactory);
        maxPool.initialize();
        
        // 获取鏈€澶ц繛鎺ユ暟
        com.rabbitmq.client.Connection conn1 = maxPool.getConnection();
        com.rabbitmq.client.Connection conn2 = maxPool.getConnection();
        
        // 验证状态        PoolStatus status = maxPool.getStatus();
        assertEquals(0, status.getIdleConnections());
        assertEquals(2, status.getActiveConnections());
        assertEquals(2, status.getTotalConnections());
        
        maxPool.close();
    }
}
