package com.mold.process.pool;

import com.mold.process.config.ConnectionPoolConfig;
import com.mold.process.config.RabbitMQConnectionPoolConfig;
import com.mold.process.exception.ConnectionPoolException;
import com.mold.process.exception.ConnectionPoolInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RabbitMQ连接姹犲伐鍘傜被
 * 璐熻矗创建鍜岀鐞嗚繛鎺ユ睜实例锛屾敮鎸佸连接姹犲満鏅?
 * 
 * @author backend team
 */
public class ConnectionPoolFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolFactory.class);
    private static final ConnectionPoolFactory INSTANCE = new ConnectionPoolFactory();
    
    // 瀛樺偍连接姹犲疄渚嬬殑鏄犲皠锛岄粯璁や娇鐢?default"浣滀负閿悕"
    private final Map<String, ConnectionPool> pools = new ConcurrentHashMap<>();
    
    // 绉佹湁鏋勯€犲嚱鏁帮紝闃叉澶栭儴实例鍖?
    private ConnectionPoolFactory() {
        // 娉ㄥ唽JVM关闭閽╁瓙锛岀‘淇濆湪JVM关闭鏃跺叧闂墍鏈夎繛鎺ユ睜
    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownAllPools));
    logger.info("ConnectionPoolFactory initialized");
    }
    
    /**
     * 获取连接姹犲伐鍘傚疄渚?
     * @return 连接姹犲伐鍘傚崟渚?
     */
    public static ConnectionPoolFactory getInstance() {
        return INSTANCE;
    }
    
    /**
     * 获取榛樿连接姹?
     * @return 榛樿连接姹犲疄渚?
     */
    public ConnectionPool getPool() {
        return getPool("default");
    }
    
    /**
     * 获取榛樿连接姹狅紙鍏煎鏃т唬鐮侊級
     * @return 榛樿连接姹犲疄渚?
     */
    public ConnectionPool getConnectionPool() {
        return getPool("default");
    }
    
    /**
     * 获取鎸囧畾鍚嶇О鐨勮繛鎺ユ睜
     * 濡傛灉连接姹犱笉瀛樺湪锛屽垯创建骞跺垵濮嬪寲涓€涓柊鐨勮繛鎺ユ睜
     * 
     * @param poolName 连接姹犲悕绉?     * @return 鎸囧畾鍚嶇О鐨勮繛鎺ユ睜实例
     */
    public ConnectionPool getPool(String poolName) {
        if (poolName == null || poolName.trim().isEmpty()) {
            poolName = "default";
        }
        
        return pools.computeIfAbsent(poolName, name -> {
            try {
                return createAndInitializePool(name);
            } catch (ConnectionPoolException e) {
                throw new RuntimeException("Failed to create connection pool: " + name, e);
            }
        });
    }
    
    /**
     * 浣跨敤鎸囧畾配置获取鎴栧垱寤鸿繛鎺ユ睜
     * 
     * @param poolName 连接姹犲悕绉?     * @param config 连接姹犻厤缃?     * @return 连接姹犲疄渚?     * @throws ConnectionPoolInitializationException 鍒濆鍖栧け璐ユ椂鎶涘嚭
     * @throws ConnectionPoolException 连接姹犲紓甯?     */
    public synchronized ConnectionPool getPool(String poolName, ConnectionPoolConfig config) throws ConnectionPoolInitializationException, ConnectionPoolException {
        if (poolName == null || poolName.trim().isEmpty()) {
            poolName = "default";
        }
        
        ConnectionPool existingPool = pools.get(poolName);
        if (existingPool != null) {
            logger.warn("Pool {} already exists, returning existing instance", poolName);
            return existingPool;
        }
        
        ConnectionPool newPool = createPool(config);
            try {
                newPool.initialize();
                pools.put(poolName, newPool);
                logger.info("Successfully created and initialized connection pool {}", poolName);
                return newPool;
            } catch (Exception e) {
                logger.error("Failed to create and initialize connection pool {}", poolName, e);
                throw new ConnectionPoolException("Failed to create connection pool: " + poolName, e);
            }
    }
    
    /**
     * 创建涓€涓嚜瀹氫箟连接姹犱絾涓嶈嚜鍔ㄥ垵濮嬪寲
     * 
     * @param config 连接姹犻厤缃?     * @return 鏈垵濮嬪寲鐨勮繛鎺ユ睜实例
     */
    public ConnectionPool createPool(ConnectionPoolConfig config) {
        return new RabbitMQConnectionPool(config);
    }
    
    /**
     * 关闭鎸囧畾鍚嶇О鐨勮繛鎺ユ睜
     * 
     * @param poolName 连接姹犲悕绉?     */
    public synchronized void shutdownPool(String poolName) {
        if (poolName == null || poolName.trim().isEmpty()) {
            poolName = "default";
        }
        
        ConnectionPool pool = pools.remove(poolName);
        if (pool != null) {
            try {
                pool.shutdown();
                logger.info("Successfully shut down connection pool {}", poolName);
            } catch (Exception e) {
                logger.error("Error shutting down connection pool {}", poolName, e);
            }
        } else {
            logger.warn("Pool {} does not exist, cannot shut down", poolName);
        }
    }
    
    /**
     * 关闭鎵€鏈夎繛鎺ユ睜
     */
    public synchronized void shutdownAllPools() {
    logger.info("Shutting down all connection pools, total pools: {}", pools.size());
        
        for (Map.Entry<String, ConnectionPool> entry : pools.entrySet()) {
            String poolName = entry.getKey();
            ConnectionPool pool = entry.getValue();
            
            try {
                pool.shutdown();
                logger.info("Successfully shut down connection pool {}", poolName);
            } catch (Exception e) {
                logger.error("Error shutting down connection pool {}", poolName, e);
            }
        }
        
        pools.clear();
        logger.info("All connection pools have been shut down");
    }
    
    /**
     * 获取褰撳墠娲昏穬鐨勮繛鎺ユ睜鏁伴噺
     * 
     * @return 连接姹犳暟閲?     */
    public int getPoolCount() {
        return pools.size();
    }
    
    /**
     * 妫€鏌ユ寚瀹氬悕绉扮殑连接姹犳槸鍚﹀瓨鍦?
     * 
     * @param poolName 连接姹犲悕绉?     * @return 鏄惁瀛樺湪
     */
    public boolean hasPool(String poolName) {
        if (poolName == null || poolName.trim().isEmpty()) {
            poolName = "default";
        }
        return pools.containsKey(poolName);
    }
    
    /**
     * 创建骞跺垵濮嬪寲连接姹?
     * 
     * @param poolName 连接姹犲悕绉?     * @return 鍒濆鍖栧悗鐨勮繛鎺ユ睜
     * @throws ConnectionPoolInitializationException 鍒濆鍖栧け璐ユ椂鎶涘嚭
     * @throws ConnectionPoolException 连接姹犲紓甯?     */
    private ConnectionPool createAndInitializePool(String poolName) throws ConnectionPoolInitializationException, ConnectionPoolException {
        ConnectionPoolConfig config = new RabbitMQConnectionPoolConfig();
        ConnectionPool pool = createPool(config);
        
            try {
                pool.initialize();
                logger.info("Successfully created and initialized connection pool {}", poolName);
                return pool;
            } catch (Exception e) {
                logger.error("Failed to create and initialize connection pool {}", poolName, e);
                throw new ConnectionPoolException("Failed to create connection pool: " + poolName, e);
            }
    }
}
