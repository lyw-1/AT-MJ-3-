package com.mold.process.pool;

import com.mold.process.config.ConnectionPoolConfig;
import com.mold.process.config.RabbitMQConnectionPoolConfig;
import com.mold.process.exception.ConnectionAcquireException;
import com.mold.process.exception.ConnectionPoolException;
import com.mold.process.exception.ConnectionPoolInitializationException;
import com.mold.process.exception.ConnectionReturnException;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RabbitMQ连接姹犵殑核心实现绫?
 * 
 * @author backend team
 */
public class RabbitMQConnectionPool implements ConnectionPool {
    
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConnectionPool.class);
    
    private final ConnectionPoolConfig config;
    private final ConnectionFactoryWrapper factoryWrapper;
    private final BlockingQueue<ConnectionWrapper> idleConnections;
    private final Set<ConnectionWrapper> activeConnections;
    private final RabbitMQPoolStatus status;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    private volatile Thread cleanupThread;
    
    /**
     * 创建榛樿配置鐨勮繛鎺ユ睜
     */
    public RabbitMQConnectionPool() {
        this(RabbitMQConnectionPoolConfig.getDefaultConfig());
    }
    
    /**
     * 创建鎸囧畾配置鐨勮繛鎺ユ睜
     * @param config 连接姹犻厤缃?
     */
    public RabbitMQConnectionPool(ConnectionPoolConfig config) {
        this(config, new ConnectionFactoryWrapper());
    }
    
    /**
     * 创建鎸囧畾配置鍜岃繛鎺ュ伐鍘傜殑连接姹?
     * @param config 连接姹犻厤缃?
     * @param factoryWrapper 连接宸ュ巶鍖呰鍣?
     */
    public RabbitMQConnectionPool(ConnectionPoolConfig config, ConnectionFactoryWrapper factoryWrapper) {
        this.config = config;
        this.factoryWrapper = factoryWrapper;
        this.idleConnections = new LinkedBlockingQueue<>();
        this.activeConnections = new HashSet<>();
        this.status = new RabbitMQPoolStatus(config.getMaxConnections());
    }
    
    /**
     * 鍒濆鍖栬繛鎺ユ睜
     */
    @Override
    public synchronized void initialize() throws ConnectionPoolInitializationException {
        if (initialized.get()) {
            logger.warn("Connection pool already initialized, skipping init");
            return;
        }
        
        try {
            logger.info("Initializing RabbitMQ connection pool, config: {}", config);
            
            // 创建鍒濆连接
            prewarm(config.getInitialConnections());
            
            // 鍚姩娓呯悊绾跨▼
            startCleanupThread();
            
            initialized.set(true);
            status.setInitialized(true);
            logger.info("RabbitMQ connection pool initialized successfully: {}", status);
        } catch (Exception e) {
            logger.error("Failed to initialize RabbitMQ connection pool", e);
            throw new ConnectionPoolInitializationException("Failed to initialize RabbitMQ connection pool", e);
        }
    }
    
    /**
     * 获取涓€涓猂abbitMQ连接
     */
    @Override
    public Connection getConnection() throws ConnectionPoolException {
        checkState();
        
        ConnectionWrapper wrapper = null;
        long startTime = System.currentTimeMillis();
        long timeoutTime = startTime + config.getAcquireTimeoutMs();
        
        while (wrapper == null) {
            // 灏濊瘯浠庣┖闂查槦鍒楄幏鍙栬繛鎺?
            wrapper = idleConnections.poll();
            
            if (wrapper != null) {
                // 验证连接鏈夋晥鎬?
                if (!isConnectionValid(wrapper)) {
                    logger.debug("Connection validation failed, closing and removing");
                    closeAndRemoveConnection(wrapper);
                    wrapper = null;
                    continue;
                }
                
                // 鏍囪连接涓烘椿璺?
                    markConnectionActive(wrapper);
                    logger.debug("Acquired connection from idle pool: {}", wrapper.getConnection().hashCode());
                break;
            }
            
            // 濡傛灉娌℃湁绌洪棽连接锛屽皾璇曞垱寤烘柊连接
            if (status.getTotalConnections() < config.getMaxConnections()) {
                try {
                    // 妫€鏌ユ槸鍚﹁繕鏈夋椂闂村垱寤烘柊连接
                    if (System.currentTimeMillis() > timeoutTime) {
                        throw new ConnectionAcquireException("Timed out acquiring connection");
                    }
                    
                    wrapper = createNewConnection();
                    markConnectionActive(wrapper);
                    logger.debug("Created new connection: {}", wrapper.getConnection().hashCode());
                    break;
                } catch (Exception e) {
                    logger.error("Failed to create new connection", e);
                    // continue to retry acquiring connection
                }
            }
            
            // 濡傛灉宸茶揪鍒版渶澶ц繛鎺ユ暟锛岀瓑寰呯┖闂茶繛鎺?
            try {
                long waitTime = timeoutTime - System.currentTimeMillis();
                    if (waitTime <= 0) {
                    throw new ConnectionAcquireException("Timed out acquiring connection; active connections reached max: " + config.getMaxConnections());
                }
                
                logger.debug("宸茶揪鍒版渶澶ц繛鎺ユ暟锛岀瓑寰呯┖闂茶繛鎺ワ紝鏈€澶氱瓑寰?{}ms", waitTime);
                wrapper = idleConnections.poll(waitTime, TimeUnit.MILLISECONDS);
                
                if (wrapper != null) {
                    // validate the connection
                    if (!isConnectionValid(wrapper)) {
                        logger.debug("Polled connection is invalid, closing and removing");
                        closeAndRemoveConnection(wrapper);
                        wrapper = null;
                        continue;
                    }

                    // mark connection active and return
                    markConnectionActive(wrapper);
                    logger.debug("Polled connection acquired: {}", wrapper.getConnection().hashCode());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ConnectionAcquireException("Interrupted while acquiring connection", e);
            }
            
            // 妫€鏌ユ槸鍚﹁秴鏃?
                if (System.currentTimeMillis() > timeoutTime) {
                throw new ConnectionAcquireException("Timed out acquiring connection");
            }
        }
        
        if (wrapper == null) {
            throw new ConnectionAcquireException("Unable to acquire connection");
        }
        
        return wrapper.getConnection();
    }
    
    /**
     * 褰掕繕连接鍒拌繛鎺ユ睜
     */
    @Override
    public void returnConnection(Connection connection) throws ConnectionPoolException {
        checkState();
        
        if (connection == null) {
            throw new ConnectionReturnException("Cannot return null connection");
        }
        
        // 鏌ユ壘瀵瑰簲鐨勫寘瑁呭櫒
        ConnectionWrapper wrapper = findConnectionWrapper(connection);
        if (wrapper == null) {
            logger.warn("Returned connection is not from this pool; closing it");
            try {
                connection.close();
            } catch (IOException e) {
                logger.error("Error closing returned connection", e);
            }
            return;
        }
        
        try {
            // 妫€鏌ヨ繛鎺ユ槸鍚︽湁鏁?
            if (!isConnectionValid(wrapper)) {
                logger.debug("Returned connection is invalid; closing and removing");
                closeAndRemoveConnection(wrapper);
                return;
            }
            
            logger.info("Successfully returned connection: {}", connection.hashCode());
            
            // 鍏堟爣璁拌繛鎺ヤ负绌洪棽锛堣繖閲屼細更新鐘舵€佽鏁帮級
            markConnectionIdle(wrapper);
            
            // 鍐嶆坊鍔犲埌绌洪棽闃熷垪
            idleConnections.offer(wrapper);
            
            logger.info("Connection handed back to pool: {}", connection.hashCode());
            logger.info("Pool status after return - active: {}, idle: {}", status.getActiveConnections(), status.getIdleConnections());
        } catch (Exception e) {
            throw new ConnectionReturnException("Failed to return connection", e);
        }
    }
    
    /**
     * 绔嬪嵆创建鎸囧畾鏁伴噺鐨勮繛鎺?
     */
    @Override
    public void prewarm(int count) {
        if (count <= 0) {
            return;
        }
        
        int created = 0;
        int maxToCreate = Math.min(count, config.getMaxConnections());
        
    logger.info("Prewarming connection pool, target to create: {} connections", maxToCreate);
    logger.info("Pool status before prewarm - active: {}, idle: {}", status.getActiveConnections(), status.getIdleConnections());
        
        for (int i = 0; i < maxToCreate; i++) {
            try {
                if (status.getTotalConnections() >= config.getMaxConnections()) {
                    logger.warn("Max connections reached, stop creating new connections");
                    break;
                }
                
                ConnectionWrapper wrapper = createNewConnection();
                // 姝ｇ‘鏍囪连接为空闂茬姸鎬侊紙杩欎細鑷姩更新鐘舵€佽鏁帮級
                markConnectionIdle(wrapper);
                idleConnections.offer(wrapper);
                created++;
                
                logger.info("Created connection #{}; pool active: {}, idle: {}", i+1, status.getActiveConnections(), status.getIdleConnections());
            } catch (Exception e) {
                logger.error("Error prewarming connection at index {}", i, e);
                // 缁х画灏濊瘯创建鍏朵粬连接
            }
        }

        logger.info("Prewarm complete, created {} connections", created);
        logger.info("Pool status after prewarm - active: {}, idle: {}", status.getActiveConnections(), status.getIdleConnections());
    }
    
    /**
     * 关闭连接姹狅紝閲婃斁鎵€鏈夎祫婧?
     */
    @Override
    public synchronized void shutdown() {
        if (shutdown.get()) {
            logger.warn("连接姹犲凡缁忓叧闂紝璺宠繃关闭操作");
            return;
        }
        
        shutdown.set(true);
        status.setShutdown(true);
        
    logger.info("Shutting down RabbitMQ connection pool");
        
        // 鍋滄娓呯悊绾跨▼
        stopCleanupThread();
        
        // 关闭鎵€鏈夌┖闂茶繛鎺?
        closeIdleConnections();
        
        // 关闭鎵€鏈夋椿璺冭繛鎺?
        closeActiveConnections();
        
        initialized.set(false);
        logger.info("RabbitMQ连接姹犲凡瀹屽叏关闭");
    }
    
    /**
     * 实现AutoCloseable接口鐨刢lose方法锛岃皟鐢╯hutdown方法
     */
    @Override
    public void close() {
        shutdown();
    }
    
    /**
     * 获取连接姹犵姸鎬佷俊鎭?
     */
    @Override
    public PoolStatus getStatus() {
        return status;
    }
    
    // 绉佹湁杈呭姪方法
    
    /**
     * 妫€鏌ヨ繛鎺ユ睜状态
     */
    private void checkState() {
        if (shutdown.get()) {
            throw new IllegalStateException("连接姹犲凡关闭");
        }
        if (!initialized.get()) {
            throw new IllegalStateException("连接姹犳湭鍒濆鍖栵紝璇峰厛璋冪敤initialize()方法");
        }
    }
    
    /**
     * 创建鏂扮殑连接
     */
    private ConnectionWrapper createNewConnection() throws IOException, TimeoutException {
        Connection connection = factoryWrapper.createConnection();
        return new ConnectionWrapper(connection);
    }
    
    /**
     * 验证连接鏈夋晥鎬?
     */
    private boolean isConnectionValid(ConnectionWrapper wrapper) {
        // 妫€鏌ヨ繛鎺ユ槸鍚︽墦开€
        if (!wrapper.isValid()) {
            return false;
        }
        
        // 妫€鏌ヨ繛鎺ユ槸鍚﹁秴杩囨渶澶у瓨娲绘椂闂?
        if (wrapper.getLifetime() > config.getConnectionMaxLifetimeMs()) {
            logger.debug("连接宸茶秴杩囨渶澶у瓨娲绘椂闂?{}ms > {}ms", 
                    wrapper.getLifetime(), config.getConnectionMaxLifetimeMs());
            return false;
        }
        
        // 可以添加鏇村鐨勫仴搴锋鏌ラ€昏緫
        return true;
    }
    
    /**
     * 鏍囪连接涓烘椿璺?
     */
    private synchronized void markConnectionActive(ConnectionWrapper wrapper) {
        // 鍏堟鏌ヨ繛鎺ユ槸鍚﹀湪绌洪棽鐘舵€侊紙鍦ㄨ缃椿璺冪姸鎬佷箣鍓嶏級
        boolean wasIdle = !wrapper.isActive();
        
        // 设置连接涓烘椿璺冪姸鎬?
        wrapper.setActive(true);
        activeConnections.add(wrapper);
        
        // 濡傛灉连接涔嬪墠鏄┖闂茬姸鎬侊紝鍑忓皯绌洪棽璁℃暟
        if (wasIdle) {
            status.decrementIdle();
        }
        status.incrementActive();
    }
    
    /**
     * 鏍囪连接为空闂?
     */
    private synchronized void markConnectionIdle(ConnectionWrapper wrapper) {
        boolean wasActive = wrapper.isActive();
        logger.info("markConnectionIdle开€濮? 连接: {}, 鍘熸椿璺冪姸鎬? {}", wrapper.getConnection().hashCode(), wasActive);
        logger.info("markConnectionIdle鍓嶇姸鎬? 娲昏穬: {}, 绌洪棽: {}", status.getActiveConnections(), status.getIdleConnections());
        
        wrapper.setActive(false);
        wrapper.updateLastUsedTime();
        
        // 濡傛灉连接涔嬪墠鏄椿璺冪姸鎬侊紝鍒欎粠娲昏穬闆嗗悎涓Щ闄ゅ苟鍑忓皯娲昏穬璁℃暟
            if (wasActive) {
            activeConnections.remove(wrapper);
            status.decrementActive();
            logger.info("Connection removed from active set; updated counts");
        }
        
        // 澧炲姞绌洪棽璁℃暟
        status.incrementIdle();
        logger.info("markConnectionIdle鍚庣姸鎬? 娲昏穬: {}, 绌洪棽: {}", status.getActiveConnections(), status.getIdleConnections());
        
        // 瀵逛簬鏂板垱寤虹殑连接锛堟棦涓嶅湪娲昏穬闆嗗悎涔熶笉鍦ㄧ┖闂查泦鍚堬級锛岄渶瑕佺‘淇濆畠琚纭鏁?
        // 鏂拌繛鎺ラ粯璁ctive=false锛屽湪prewarm杩囩▼涓皟鐢╩arkConnectionIdle鏃讹紝
        // 应该澧炲姞绌洪棽璁℃暟锛屽洜涓鸿繖鏄涓€娆℃爣璁颁负绌洪棽
    }
    
    /**
     * 鏌ユ壘连接瀵瑰簲鐨勫寘瑁呭櫒
     */
    private ConnectionWrapper findConnectionWrapper(Connection connection) {
        // 鍏堝湪娲昏穬连接涓煡鎵?
        for (ConnectionWrapper wrapper : activeConnections) {
            if (wrapper.getConnection() == connection) {
                return wrapper;
            }
        }
        
        // 鍐嶅湪绌洪棽连接涓煡鎵?
        for (ConnectionWrapper wrapper : idleConnections) {
            if (wrapper.getConnection() == connection) {
                return wrapper;
            }
        }
        
        return null;
    }
    
    /**
     * 关闭骞剁Щ闄よ繛鎺?
     */
    private synchronized void closeAndRemoveConnection(ConnectionWrapper wrapper) {
        // 浠庢墍鏈夐泦鍚堜腑绉婚櫎
        activeConnections.remove(wrapper);
        idleConnections.remove(wrapper);
        
        // 更新状态
        if (wrapper.isActive()) {
            status.decrementActive();
        } else {
            status.decrementIdle();
        }
        
        // 关闭连接
        wrapper.close();
    }
    
    /**
     * 鍚姩娓呯悊绾跨▼
     */
    private void startCleanupThread() {
        cleanupThread = new Thread(this::cleanupIdleConnections, "RabbitMQ-Connection-Cleanup");
        cleanupThread.setDaemon(true);
        cleanupThread.start();
        logger.info("连接姹犳竻鐞嗙嚎绋嬪凡鍚姩");
    }
    
    /**
     * 鍋滄娓呯悊绾跨▼
     */
    private void stopCleanupThread() {
        if (cleanupThread != null) {
            cleanupThread.interrupt();
            try {
                cleanupThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            cleanupThread = null;
            logger.info("连接姹犳竻鐞嗙嚎绋嬪凡鍋滄");
        }
    }
    
    /**
     * 娓呯悊绌洪棽连接
     */
    private void cleanupIdleConnections() {
        while (!shutdown.get()) {
            try {
                Thread.sleep(config.getHealthCheckIntervalMs());
                
                long idleTimeoutMs = config.getIdleTimeoutMs();
                Iterator<ConnectionWrapper> iterator = idleConnections.iterator();
                
                while (iterator.hasNext()) {
                    ConnectionWrapper wrapper = iterator.next();
                    
                    // 妫€鏌ユ槸鍚︾┖闂茶秴鏃舵垨连接鏃犳晥
                    if (wrapper.getIdleTime() > idleTimeoutMs || !isConnectionValid(wrapper)) {
                        logger.debug("娓呯悊杩囨湡鎴栨棤鏁堢殑绌洪棽连接锛岀┖闂叉椂闂? {}ms", wrapper.getIdleTime());
                        iterator.remove();
                        closeAndRemoveConnection(wrapper);
                    }
                }
                
                logger.debug("连接姹犳竻鐞嗗畬鎴愶紝褰撳墠状态 {}", status);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.error("连接姹犳竻鐞嗚繃绋嬩腑鍙戠敓开傚父", e);
            }
        }
    }
    
    /**
     * 关闭鎵€鏈夌┖闂茶繛鎺?
     */
    private void closeIdleConnections() {
        int count = 0;
        ConnectionWrapper wrapper;
        while ((wrapper = idleConnections.poll()) != null) {
            wrapper.close();
            count++;
        }
        status.setIdle(0);
        logger.info("宸插叧闂墍鏈夌┖闂茶繛鎺ワ紝鏁伴噺: {}", count);
    }
    
    /**
     * 关闭鎵€鏈夋椿璺冭繛鎺?
     */
    private void closeActiveConnections() {
        int count = activeConnections.size();
        for (ConnectionWrapper wrapper : activeConnections) {
            wrapper.close();
        }
        activeConnections.clear();
        status.setActive(0);
        logger.info("宸插叧闂墍鏈夋椿璺冭繛鎺ワ紝鏁伴噺: {}", count);
    }
}
