package com.mold.digitalization.config;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Import;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import com.mold.digitalization.service.RedisService;
import com.mold.digitalization.service.UserLockService;
import com.mold.digitalization.mapper.UserMapper;
import com.mold.digitalization.mapper.RoleMapper;
import com.mold.digitalization.entity.Role;
import org.mockito.Mockito;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 测试配置绫? * 涓洪泦鎴愭祴璇曟彁渚涘繀瑕佺殑配置鍜孊ean
 */
@TestConfiguration
@ActiveProfiles("test")
@org.springframework.boot.autoconfigure.EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
// 说明：WebMvcTest 切片测试不需要组件扫描、Mapper扫描与 MyBatisPlusConfig 导入，避免引入大量 Bean
// 导致上下文加载失败
@org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity(prePostEnabled = true)
public class TestConfig {

    // 测试配置绫伙紝绉婚櫎passwordEncoder bean瀹氫箟浠ラ伩鍏嶄笌涓婚厤缃啿绐? //
    // 测试环境灏嗕娇鐢ㄤ富配置涓殑passwordEncoder bean

    /**
     * H2内容瓨数据搴撻厤缃? * 涓烘祴璇曠幆澧冩彁渚涘唴瀛樻暟鎹簱
     */
    @Bean
    public DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    /**
     * SqlSessionFactory配置
     * 涓烘祴璇曠幆澧冩彁渚汳yBatis鐨凷qlSessionFactory
     * 鐩存帴浣跨敤涓婚厤缃被涓殑配置锛岀‘淇滾ambda缂撳瓨涓€鑷存€?
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, GlobalConfig globalConfig,
            MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // 配置MyBatis Plus鎷︽埅鍣紝杩欐槸Lambda缂撳瓨姝ｅ父宸ヤ綔鎵€蹇呴渶鐨?
        // sessionFactory.setPlugins(mybatisPlusInterceptor);

        // 浣跨敤涓婚厤缃被涓殑鍏ㄥ眬配置锛岀‘淇滾ambda缂撳瓨配置涓€鑷?
        // sessionFactory.setGlobalConfig(globalConfig);

        // 鏄惧紡配置Lambda搴忓垪鍖栧櫒锛岀‘淇濇祴璇曠幆澧冧腑鐨凩ambda缂撳瓨姝ｅ父宸ヤ綔
        com.baomidou.mybatisplus.core.MybatisConfiguration configuration = new com.baomidou.mybatisplus.core.MybatisConfiguration();
        configuration.setCacheEnabled(true);
        configuration.setLazyLoadingEnabled(true);
        configuration.setAggressiveLazyLoading(false);
        configuration.setMultipleResultSetsEnabled(true);
        configuration.setUseColumnLabel(true);
        configuration.setUseGeneratedKeys(false);
        configuration.setDefaultExecutorType(org.apache.ibatis.session.ExecutorType.SIMPLE);
        configuration.setDefaultStatementTimeout(25);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLazyLoadTriggerMethods(
                new java.util.HashSet<>(java.util.Arrays.asList("equals", "clone", "hashCode", "toString")));
        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }

    // 绉婚櫎閲嶅鐨凪yBatis Plus配置Bean锛岃测试环境鐩存帴浣跨敤涓婚厤缃被涓殑配置
    // 涓婚厤缃被MybatisPlusConfig宸茬粡閫氳繃@Import娉ㄨВ瀵煎叆锛屽寘鍚獿ambda缂撳瓨配置

    /**
     * Mock JavaMailSender配置
     * 涓烘祴璇曠幆澧冩彁渚汳ock鐨勯偖浠跺彂閫佸櫒
     */
    @Bean
    public JavaMailSender javaMailSender() {
        return new org.springframework.mail.javamail.JavaMailSenderImpl();
    }

    /**
     * Mock RedisConnectionFactory鐢ㄤ簬测试
     * 閬垮厤渚濊禆鐪熷疄鐨凴edis连接锛屽悓鏃舵弧瓒砈pring Cache鑷姩配置鐨勯渶姹?
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        return Mockito.mock(RedisConnectionFactory.class);
    }

    /**
     * Mock RedisService鐢ㄤ簬测试
     * 閬垮厤渚濊禆鐪熷疄鐨凴edis连接
     */
    @Bean
    @Primary
    public RedisService redisService() {
        RedisService mockRedisService = Mockito.mock(RedisService.class);
        // 配置Mock琛屼负
        Mockito.when(mockRedisService.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(mockRedisService.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRedisService.expire(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(mockRedisService.sAdd(Mockito.anyString(), Mockito.any())).thenReturn(1L);
        Mockito.when(mockRedisService.keys(Mockito.anyString())).thenReturn(new HashSet<String>());
        // void方法浣跨敤doNothing()鏉ラ伩鍏嶅紓甯?
        // Mockito.doNothing().when(mockRedisService).delete(Mockito.anyString());
        return mockRedisService;
    }

    /**
     * Mock UserDetailsService鐢ㄤ簬测试
     * 閬垮厤渚濊禆鐪熷疄鐨勬暟鎹簱查询
     */
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return username -> {
            if ("admin".equals(username) || "testuser".equals(username)) {
                return new User(
                        username,
                        "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV6UiM", // password
                        true, // enabled
                        true, // accountNonExpired
                        true, // credentialsNonExpired
                        true, // accountNonLocked
                        java.util.Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_ADMIN")));
            }
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("用户涓嶅瓨鍦? " + username);
        };
    }

    /**
     * Mock UserLockService鐢ㄤ簬测试
     * 閬垮厤渚濊禆鐪熷疄鐨勬暟鎹簱操作
     */
    @Bean
    @Primary
    public UserLockService userLockService() {
        UserLockService mockUserLockService = Mockito.mock(UserLockService.class);
        // 配置Mock琛屼负
        Mockito.doNothing().when(mockUserLockService).resetLoginFailedCount(Mockito.anyLong());
        Mockito.when(mockUserLockService.unlockUser(Mockito.anyLong())).thenReturn(true);
        Mockito.when(mockUserLockService.lockUser(Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        Mockito.when(mockUserLockService.isUserLocked(Mockito.anyLong())).thenReturn(false);
        return mockUserLockService;
    }

    /**
     * Mock UserMapper鐢ㄤ簬测试
     * 閬垮厤渚濊禆鐪熷疄鐨勬暟鎹簱操作
     */
    @Bean
    @Primary
    public UserMapper userMapper() {
        UserMapper mockUserMapper = Mockito.mock(UserMapper.class);
        // 配置Mock琛屼负
        Mockito.when(mockUserMapper.updateLastLoginInfo(Mockito.anyLong(), Mockito.anyString(),
                Mockito.any(LocalDateTime.class))).thenReturn(1);
        Mockito.when(mockUserMapper.resetLoginFailedCount(Mockito.anyLong())).thenReturn(1);
        Mockito.when(mockUserMapper.incrementLoginFailedCount(Mockito.anyLong())).thenReturn(1);
        return mockUserMapper;
    }

    /**
     * Mock RoleMapper鐢ㄤ簬测试
     * 閬垮厤渚濊禆鐪熷疄鐨勬暟鎹簱操作
     */
    @Bean
    @Primary
    public RoleMapper roleMapper() {
        RoleMapper mockRoleMapper = Mockito.mock(RoleMapper.class);
        // 创建模拟瑙掕壊
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setRoleCode("ROLE_ADMIN");
        adminRole.setRoleName("管理员");

        // 配置Mock琛屼负
        Mockito.when(mockRoleMapper.selectByRoleCode(Mockito.anyString())).thenReturn(adminRole);
        return mockRoleMapper;
    }

    /**
     * 浜嬪姟绠＄悊鍣ㄩ厤缃? * 涓烘祴璇曠幆澧冩彁渚涗簨鍔＄鐞嗘敮鎸?
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
