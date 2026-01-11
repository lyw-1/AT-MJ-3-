package com.mold.digitalization.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Import;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mail.javamail.JavaMailSender;
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
 * 测试配置类
 * 为单元/切片测试提供必要的配置与 Bean
 */
@TestConfiguration
@ActiveProfiles("test")
@org.springframework.boot.autoconfigure.EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
// 说明：WebMvcTest 切片测试不需要组件扫描、Mapper扫描与 MyBatisPlusConfig 导入，避免引入大量 Bean
// 导致上下文加载失败
public class TestConfig {

    // 测试配置类中不再自定义 passwordEncoder bean，避免与主配置冲突
    // 测试环境将使用主配置中的 passwordEncoder bean

    // 注意：测试切片场景不需要数据源/事务/SqlSessionFactory等基础设施，
    // 避免与主配置的自动配置产生重复（例如 metaDataSourceAdvisor 冲突）。

    // 移除重复的 MyBatis Plus 配置 Bean，使测试环境直接使用主配置中的设置
    // 主配置 MybatisPlusConfig 已通过 @Import 引入，包含 Lambda 缓存等必要配置

    /**
     * Mock JavaMailSender 配置
     * 提供邮件发送器的测试替身
     */
    @Bean
    public JavaMailSender javaMailSender() {
        return new org.springframework.mail.javamail.JavaMailSenderImpl();
    }

    /**
     * Mock RedisConnectionFactory 用于测试
     * 避免依赖真实的 Redis 连接，同时满足 Spring Cache 自动配置的需要
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        return Mockito.mock(RedisConnectionFactory.class);
    }

    /**
     * Mock RedisService 用于测试
     * 避免依赖真实的 Redis 连接
     */
    @Bean
    @Primary
    public RedisService redisService() {
        RedisService mockRedisService = Mockito.mock(RedisService.class);
        // 配置 Mock 行为
        Mockito.when(mockRedisService.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(mockRedisService.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRedisService.expire(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(mockRedisService.sAdd(Mockito.anyString(), Mockito.any())).thenReturn(1L);
        Mockito.when(mockRedisService.keys(Mockito.anyString())).thenReturn(new HashSet<String>());
        // void 方法使用 doNothing() 避免抛异常
        // Mockito.doNothing().when(mockRedisService).delete(Mockito.anyString());
        return mockRedisService;
    }

    /**
     * Mock UserDetailsService 用于测试
     * 避免依赖真实的数据库查询
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
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("用户不存在: " + username);
        };
    }

    /**
     * Mock UserLockService 用于测试
     * 避免依赖真实的数据库操作
     */
    @Bean
    @Primary
    public UserLockService userLockService() {
        UserLockService mockUserLockService = Mockito.mock(UserLockService.class);
        // 配置 Mock 行为
        Mockito.doNothing().when(mockUserLockService).resetLoginFailedCount(Mockito.anyLong());
        Mockito.when(mockUserLockService.unlockUser(Mockito.anyLong())).thenReturn(true);
        Mockito.when(mockUserLockService.lockUser(Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        Mockito.when(mockUserLockService.isUserLocked(Mockito.anyLong())).thenReturn(false);
        return mockUserLockService;
    }

    /**
     * Mock UserMapper 用于测试
     * 避免依赖真实的数据库操作
     */
    @Bean
    @Primary
    public UserMapper userMapper() {
        UserMapper mockUserMapper = Mockito.mock(UserMapper.class);
        // 配置 Mock 行为
        Mockito.when(mockUserMapper.updateLastLoginInfo(Mockito.anyLong(), Mockito.anyString(),
                Mockito.any(LocalDateTime.class))).thenReturn(1);
        Mockito.when(mockUserMapper.resetLoginFailedCount(Mockito.anyLong())).thenReturn(1);
        Mockito.when(mockUserMapper.incrementLoginFailedCount(Mockito.anyLong())).thenReturn(1);
        return mockUserMapper;
    }

    /**
     * Mock RoleMapper 用于测试
     * 避免依赖真实的数据库操作
     */
    @Bean
    @Primary
    public RoleMapper roleMapper() {
        RoleMapper mockRoleMapper = Mockito.mock(RoleMapper.class);
        // 创建模拟角色
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setRoleCode("ROLE_ADMIN");
        adminRole.setRoleName("管理员");

        // 配置 Mock 行为
        Mockito.when(mockRoleMapper.selectByRoleCode(Mockito.anyString())).thenReturn(adminRole);
        return mockRoleMapper;
    }

    // 事务管理器不在测试配置中声明，交由主应用或切片测试环境自行处理。

}
