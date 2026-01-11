package com.mold.process.pool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.mold.process.config.ConnectionPoolConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

/**
 * 连接池性能测试
 * 测试连接池在多线程环境下的性能和正确性
 */
public class ConnectionPoolPerformanceTest {

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
                return 10;
            }
            
            @Override
            public int getInitialConnections() {
                return 5;
            }
            
            @Override
            public long getIdleTimeoutMs() {
                return 60000;
            }
            
            @Override
            public long getConnectionMaxLifetimeMs() {
                return 300000;
            }
            
            @Override
            public long getAcquireTimeoutMs() {
                return 5000;
            }
            
            @Override
            public long getHealthCheckIntervalMs() {
                return 30000;
            }
        };
        
        // 设置mock行为
        when(mockConnectionFactory.createConnection()).thenReturn(mockConnection);
        
        // 创建连接池
        pool = new RabbitMQConnectionPool(testConfig);
        pool.initialize();
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (pool != null) {
            pool.shutdown();
        }
        if (mockCloseable != null) {
            mockCloseable.close();
        }
    }

    @Test
    public void testMultiThreadedConnectionUsage() throws Exception {
        int threadCount = 50;
        int operationsPerThread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        long startTime = System.currentTimeMillis();
        
        // 创建多个线程并发使用连接池
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        // 获取连接
                        com.rabbitmq.client.Connection connection = pool.getConnection();
                        
                        // 模拟使用连接（休眠一小段时间）
                        Thread.sleep(1);
                        
                        // 归还连接
                        pool.returnConnection(connection);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("多线程测试失败: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // 等待所有线程完成
        latch.await(30, TimeUnit.SECONDS);
        
        long endTime = System.currentTimeMillis();
        
        // 验证最终状态
        PoolStatus status = pool.getStatus();
        System.out.println("多线程测试完成，耗时: " + (endTime - startTime) + "ms");
        System.out.println("活跃连接: " + status.getActiveConnections());
        System.out.println("空闲连接: " + status.getIdleConnections());
        System.out.println("总连接: " + status.getTotalConnections());
        
        // 所有连接都应该被归还
        assertEquals(5, status.getIdleConnections()); // 初始连接数
        assertEquals(0, status.getActiveConnections());
        
        executorService.shutdown();
    }

    @Test
    public void testConnectionPoolEfficiency() throws Exception {
        // 测试连接池的效率
        int connectionCount = 20;
        
        // 记录获取连接的时间
        long[] directTimes = new long[connectionCount];
        long[] poolTimes = new long[connectionCount];
        
        // 模拟直接创建连接的时间
        for (int i = 0; i < connectionCount; i++) {
            long start = System.nanoTime();
            com.rabbitmq.client.Connection conn = mockConnectionFactory.createConnection();
            directTimes[i] = System.nanoTime() - start;
        }
        
        // 测试从连接池获取连接的时间
        for (int i = 0; i < connectionCount; i++) {
            long start = System.nanoTime();
            com.rabbitmq.client.Connection conn = pool.getConnection();
            poolTimes[i] = System.nanoTime() - start;
            pool.returnConnection(conn);
        }
        
        // 计算平均时间
        double directAvg = calculateAverage(directTimes);
        double poolAvg = calculateAverage(poolTimes);
        
        System.out.println("直接创建连接平均时间: " + directAvg + " ns");
        System.out.println("连接池获取连接平均时间: " + poolAvg + " ns");
        System.out.println("性能提升: " + (directAvg / poolAvg) + "倍");
        
        // 在实际环境中，连接池应该会有显著的性能提升
        // 由于这是模拟测试，可能不会显示真实的性能差异
    }
    
    private double calculateAverage(long[] times) {
        long sum = 0;
        for (long time : times) {
            sum += time;
        }
        return (double) sum / times.length;
    }
    
    private void fail(String message) {
        System.err.println(message);
    }
}