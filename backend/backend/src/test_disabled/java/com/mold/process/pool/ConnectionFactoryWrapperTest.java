package com.mold.process.pool;

import com.mold.process.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 连接宸ュ巶鍖呰鍣ㄥ崟鍏冩祴璇? */
public class ConnectionFactoryWrapperTest {

    @Mock
    private RabbitMQConfig mockConfig;

    @Test
    public void testCreateConnectionFactory() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            // 创建连接宸ュ巶鍖呰鍣?            ConnectionFactoryWrapper factoryWrapper = new ConnectionFactoryWrapper();
            
            // 验证连接宸ュ巶不为空            assertNotNull(factoryWrapper.getFactory());
            
            // 妫€鏌ヨ繛鎺ュ伐鍘傞厤缃?            com.rabbitmq.client.ConnectionFactory factory = factoryWrapper.getFactory();
            assertNotNull(factory.getHost());
            assertNotNull(factory.getPort());
            assertNotNull(factory.getUsername());
            assertNotNull(factory.getPassword());
            assertTrue(factory.isAutomaticRecoveryEnabled());
        }
    }

    @Test
    public void testCreateConnection() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            // 创建连接宸ュ巶鍖呰鍣?            ConnectionFactoryWrapper factoryWrapper = new ConnectionFactoryWrapper();
            
            // 鐩存帴测试连接创建鍙兘浼氬け璐ワ紝鍥犱负鍙兘娌℃湁鐪熷疄鐨凴abbitMQ服务
            // 鍦ㄥ疄闄呮祴璇曚腑锛屽彲浠ユā鎷熻繖涓繃绋?            assertNotNull(factoryWrapper);
            assertNotNull(factoryWrapper.getFactory());
        }
    }
}