package com.mold.process.pool;

import com.mold.process.exception.ConnectionPoolException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 连接姹犲伐鍘傛祴璇? */
public class ConnectionPoolFactoryTest {

    @BeforeEach
    public void setUp() {
        // 娓呴櫎鐜版湁鐨勮繛鎺ユ睜实例
        ConnectionPoolFactory.getInstance().shutdownAllPools();
    }

    @AfterEach
    public void tearDown() {
        // 关闭鎵€鏈夎繛鎺ユ睜
        ConnectionPoolFactory.getInstance().shutdownAllPools();
    }

    @Test
    public void testGetDefaultPool() throws ConnectionPoolException {
        // 获取榛樿连接姹?        ConnectionPool defaultPool = ConnectionPoolFactory.getInstance().getPool();
        
        // 验证榛樿连接姹犱笉为空
        assertNotNull(defaultPool);
        
        // 鍐嶆获取榛樿连接姹狅紝应该鏄悓涓€涓疄渚?        ConnectionPool anotherDefaultPool = ConnectionPoolFactory.getInstance().getPool();
        assertSame(defaultPool, anotherDefaultPool);
    }

    @Test
    public void testGetNamedPool() throws ConnectionPoolException {
        // 获取鍛藉悕连接姹?        String poolName = "testPool";
        ConnectionPool namedPool = ConnectionPoolFactory.getInstance().getPool(poolName);
        
        // 验证鍛藉悕连接姹犱笉为空
        assertNotNull(namedPool);
        
        // 鍐嶆获取鐩稿悓鍚嶇О鐨勮繛鎺ユ睜锛屽簲璇ユ槸鍚屼竴涓疄渚?        ConnectionPool anotherNamedPool = ConnectionPoolFactory.getInstance().getPool(poolName);
        assertSame(namedPool, anotherNamedPool);
    }

    @Test
    public void testDifferentPoolNames() throws ConnectionPoolException {
        // 获取涓や釜涓嶅悓鍚嶇О鐨勮繛鎺ユ睜
        ConnectionPool pool1 = ConnectionPoolFactory.getInstance().getPool("pool1");
        ConnectionPool pool2 = ConnectionPoolFactory.getInstance().getPool("pool2");
        
        // 验证瀹冧滑鏄笉鍚岀殑实例
        assertNotSame(pool1, pool2);
    }

    @Test
    public void testCloseAllPools() throws ConnectionPoolException {
        // 获取澶氫釜连接姹?        ConnectionPool defaultPool = ConnectionPoolFactory.getInstance().getPool();
        ConnectionPool namedPool = ConnectionPoolFactory.getInstance().getPool("testPool");
        
        // 关闭鎵€鏈夎繛鎺ユ睜
        ConnectionPoolFactory.getInstance().shutdownAllPools();
        
        // 验证获取鏂扮殑连接姹犳槸鏂扮殑实例
        ConnectionPool newDefaultPool = ConnectionPoolFactory.getInstance().getPool();
        assertNotSame(defaultPool, newDefaultPool);
    }

    @Test
    public void testSingletonPattern() {
        // 验证宸ュ巶鏄崟渚?        ConnectionPoolFactory factory1 = ConnectionPoolFactory.getInstance();
        ConnectionPoolFactory factory2 = ConnectionPoolFactory.getInstance();
        assertSame(factory1, factory2);
    }
}