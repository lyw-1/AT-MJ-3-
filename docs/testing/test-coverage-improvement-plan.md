# 单元测试覆盖率提升计划

## 📊 当前状态评估

### 测试文件统计
- **Controller层测试**: 1个 (AdminAuthControllerTest.java)
- **Service层测试**: 1个 (ProcessPresetServiceTest.java)
- **总测试文件**: 66个（包含各种测试）
- **当前覆盖率**: 未知（需要运行测试查看）

### 测试覆盖缺口
- ❌ 大部分Controller缺少单元测试
- ❌ 大部分Service缺少单元测试
- ❌ 异常处理场景覆盖不足
- ❌ 边界条件测试不足

## 🎯 目标

### 短期目标（本周）
- [ ] 核心业务逻辑测试覆盖率达到50%
- [ ] 关键Controller测试覆盖率达到80%
- [ ] 所有异常场景都有测试

### 中期目标（本月）
- [ ] 整体测试覆盖率达到50%
- [ ] Service层测试覆盖率达到60%
- [ ] Controller层测试覆盖率达到70%

### 长期目标（本季度）
- [ ] 整体测试覆盖率达到70%
- [ ] 关键模块覆盖率达到80%
- [ ] 集成测试覆盖核心流程

## 📋 测试优先级

### 1️⃣ 高优先级（必须测试）
- [ ] 用户认证模块（登录、登出、权限）
- [ ] 模具管理模块（CRUD操作）
- [ ] 任务管理模块（状态流转）
- [ ] 错误处理和异常场景

### 2️⃣ 中优先级（应该测试）
- [ ] 数据验证逻辑
- [ ] 业务规则验证
- [ ] API参数校验
- [ ] 缓存和Redis操作

### 3️⃣ 低优先级（可选测试）
- [ ] 工具类和辅助方法
- [ ] 简单的getter/setter
- [ ] 第三方库调用

## 🛠️ 测试模板

### Controller测试模板
```java
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserController userController;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
    
    @Test
    @DisplayName("获取用户列表 - 成功")
    void getUsers_Success() throws Exception {
        // Given
        List<User> users = Arrays.asList(
            createTestUser(1L, "user1"),
            createTestUser(2L, "user2")
        );
        when(userService.getAllUsers()).thenReturn(users);
        
        // When & Then
        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));
        
        verify(userService).getAllUsers();
    }
    
    @Test
    @DisplayName("获取用户 - 用户不存在")
    void getUser_NotFound() throws Exception {
        // Given
        when(userService.getUserById(999L))
            .thenThrow(new BusinessException(ErrorCodeEnum.USER_NOT_FOUND));
        
        // When & Then
        mockMvc.perform(get("/api/v1/users/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ErrorCodeEnum.USER_NOT_FOUND.getCode()));
    }
    
    @Test
    @DisplayName("创建用户 - 参数验证失败")
    void createUser_ValidationError() throws Exception {
        // Given
        String invalidRequest = """
            {
                "username": "ab",  // 太短
                "email": "invalid-email"
            }
            """;
        
        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400));
    }
    
    private User createTestUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(username + "@test.com");
        return user;
    }
}
```

### Service测试模板
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    @DisplayName("获取用户 - 成功")
    void getUserById_Success() {
        // Given
        Long userId = 1L;
        User expectedUser = createTestUser(userId);
        when(userMapper.selectById(userId)).thenReturn(expectedUser);
        
        // When
        User result = userService.getUserById(userId);
        
        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(userMapper).selectById(userId);
    }
    
    @Test
    @DisplayName("获取用户 - 用户不存在")
    void getUserById_NotFound() {
        // Given
        Long userId = 999L;
        when(userMapper.selectById(userId)).thenReturn(null);
        
        // When & Then
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> userService.getUserById(userId)
        );
        
        assertEquals(ErrorCodeEnum.USER_NOT_FOUND.getCode(), exception.getCode());
    }
    
    @Test
    @DisplayName("创建用户 - 用户名已存在")
    void createUser_UsernameExists() {
        // Given
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("existinguser");
        request.setPassword("Password123!");
        request.setEmail("new@test.com");
        
        when(userMapper.selectByUsername("existinguser"))
            .thenReturn(createTestUser(1L));
        
        // When & Then
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> userService.createUser(request)
        );
        
        assertEquals(ErrorCodeEnum.USERNAME_ALREADY_EXISTS.getCode(), exception.getCode());
    }
    
    private User createTestUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("encryptedPassword");
        return user;
    }
}
```

## 📅 实施时间表

### 第1周：基础测试（覆盖率达到20%）
- [ ] 为核心Controller添加测试（AuthController, UserController）
- [ ] 为核心Service添加测试（AuthService, UserService）
- [ ] 添加异常场景测试

### 第2周：业务逻辑测试（覆盖率达到35%）
- [ ] 为模具管理模块添加测试
- [ ] 为任务管理模块添加测试
- [ ] 添加参数验证测试

### 第3周：完善测试（覆盖率达到45%）
- [ ] 添加边界条件测试
- [ ] 添加集成测试
- [ ] 优化测试数据准备

### 第4周：查漏补缺（覆盖率达到50%）
- [ ] 运行完整测试套件
- [ ] 分析未覆盖的代码
- [ ] 补充遗漏的测试

## 🧪 测试工具和配置

### Maven命令
```bash
# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=UserControllerTest

# 运行测试并生成覆盖率报告
mvn jacoco:report

# 查看覆盖率报告
open target/site/jacoco/index.html

# 运行测试（跳过集成测试）
mvn test -DskipITs

# 运行集成测试
mvn verify -Dit.test=*
```

### IDE配置
- **IntelliJ IDEA**: 安装JUnit插件，配置测试覆盖率
- **VS Code**: 安装Java Test Runner扩展

## 📊 覆盖率目标细分

### 按模块划分
| 模块 | 当前覆盖率 | 目标覆盖率 | 测试文件数 |
|------|-----------|-----------|-----------|
| Controller层 | ~10% | 70% | 5个 |
| Service层 | ~15% | 60% | 8个 |
| 工具类 | ~50% | 80% | 3个 |
| 配置类 | ~30% | 50% | 2个 |

### 按测试类型划分
| 测试类型 | 当前数量 | 目标数量 |
|---------|---------|---------|
| 单元测试 | 66个 | 120个 |
| 集成测试 | 5个 | 20个 |
| 端到端测试 | 2个 | 10个 |

## 🎯 质量标准

### 测试质量检查清单
- [ ] 每个测试方法只测试一个功能点
- [ ] 使用有意义的测试方法名
- [ ] 遵循Arrange-Act-Assert模式
- [ ] 避免测试中的逻辑复杂度
- [ ] 使用测试数据工厂模式
- [ ] 保持测试的独立性和可重复性

### 代码质量要求
- [ ] 测试代码遵循项目编码规范
- [ ] 避免在测试中使用魔法数字
- [ ] 适当使用测试替身（Mock, Stub）
- [ ] 清理测试数据

## 🔧 测试数据工厂

创建测试数据工厂类：
```java
@Component
public class TestDataFactory {
    
    public User createUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("user" + id);
        user.setEmail("user" + id + "@test.com");
        user.setPassword("Password123!");
        return user;
    }
    
    public UserCreateRequest createUserCreateRequest() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("testuser");
        request.setPassword("Password123!");
        request.setEmail("test@test.com");
        return request;
    }
    
    // 更多测试数据创建方法...
}
```

## 📞 支持和资源

### 文档
- 测试模板: `docs/testing/templates/`
- 测试工具: `docs/testing/tools/`
- 最佳实践: `docs/testing/best-practices/`

### 工具
- JUnit 5: https://junit.org/junit5/
- Mockito: https://site.mockito.org/
- AssertJ: https://assertj.github.io/doc/
- Testcontainers: https://www.testcontainers.org/

### 培训资源
- 测试驱动开发(TDD)介绍
- Mock和Stub的区别
- 测试金字塔理论
- 行为驱动开发(BDD)实践
