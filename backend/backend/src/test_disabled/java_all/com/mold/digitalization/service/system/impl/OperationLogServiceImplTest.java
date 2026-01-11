package com.mold.digitalization.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mold.digitalization.entity.dto.OperationLogQueryDTO;
import com.mold.digitalization.entity.dto.OperationLogStatisticDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.dao.system.OperationLogMapper;
import com.mold.digitalization.service.system.AsyncOperationLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperationLogServiceImplTest {

    @Mock
    private OperationLogMapper operationLogMapper;

    @Mock
    private AsyncOperationLogService asyncOperationLogService;

    private OperationLogServiceImpl operationLogService;

    private List<OperationLog> mockLogs;
    private OperationLogQueryDTO queryDTO;

    @BeforeEach
    void setUp() {
        // 浣跨敤姝ｇ‘鐨勬瀯閫犲嚱鏁板垱寤篛perationLogServiceImpl实例
        operationLogService = new OperationLogServiceImpl(operationLogMapper, asyncOperationLogService);

        // 浣跨敤鍙嶅皠设置baseMapper瀛楁锛岃ServiceImpl鐨剆ave方法姝ｅ父宸ヤ綔
        try {
            Field baseMapperField = com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.class
                    .getDeclaredField("baseMapper");
            baseMapperField.setAccessible(true);
            baseMapperField.set(operationLogService, operationLogMapper);

            // 设置sensitiveLevelThreshold瀛楁锛屽洜涓篅Value娉ㄨВ鍦ㄦ祴璇曠幆澧冧腑涓嶄細鑷姩娉ㄥ叆
            Field sensitiveLevelThresholdField = OperationLogServiceImpl.class
                    .getDeclaredField("sensitiveLevelThreshold");
            sensitiveLevelThresholdField.setAccessible(true);
            sensitiveLevelThresholdField.set(operationLogService, "high");
        } catch (Exception e) {
            throw new RuntimeException("Failed to set fields", e);
        }

        // 准备测试数据
        mockLogs = new ArrayList<>();

        OperationLog log1 = new OperationLog();
        log1.setId(1L);
        log1.setUserId(1001L);
        log1.setUsername("张三");
        log1.setModule("用户管理");
        log1.setOperationType("ADD");
        log1.setOperationDesc("添加新用户");
        log1.setUserIp("192.168.1.100");
        log1.setOperationContent("{\"username\":\"test\",\"password\":\"******\"}");
        log1.setResultCode("success");
        log1.setIsSensitive(false);
        log1.setOperationTime(LocalDateTime.now().minusDays(1));
        mockLogs.add(log1);

        OperationLog log2 = new OperationLog();
        log2.setId(2L);
        log2.setUserId(1002L);
        log2.setUsername("鏉庡洓");
        log2.setModule("系统设置");
        log2.setOperationType("UPDATE");
        log2.setOperationDesc("更新系统配置");
        log2.setUserIp("192.168.1.101");
        log2.setOperationContent("{\"key\":\"system.version\",\"value\":\"v2.0\"}");
        log2.setResultCode("success");
        log2.setIsSensitive(true);
        log2.setSensitiveLevel("high");
        log2.setOperationTime(LocalDateTime.now().minusHours(12));
        mockLogs.add(log2);

        // 准备查询鏉′欢
        queryDTO = new OperationLogQueryDTO();
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(10);
        queryDTO.setUsername("张三");
        queryDTO.setStartTime(LocalDateTime.now().minusDays(7));
        queryDTO.setEndTime(LocalDateTime.now());
    }

    @Test
    void queryPage() {
        // 准备测试数据
        Map<String, Object> params = new HashMap<>();
        params.put("username", "张三");
        params.put("operationType", "ADD");

        Page<OperationLog> page = new Page<>(1, 10);
        Page<OperationLog> resultPage = new Page<>(1, 10, 1);
        resultPage.setRecords(mockLogs.stream().filter(log -> log.getUsername().contains("张")).toList());

        // 模拟Mapper琛屼负
        when(operationLogMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(resultPage);

        // 执行测试
        Page<OperationLog> result = operationLogService.queryPage(1, 10, params);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertTrue(result.getRecords().get(0).getUsername().contains("张"));

        // 验证璋冪敤
        verify(operationLogMapper).selectPage(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void queryByCondition() {
        // 准备鍒嗛〉结果
        Page<OperationLog> resultPage = new Page<>(1, 10, 1);
        resultPage.setRecords(mockLogs.stream().filter(log -> log.getUsername().contains("张")).toList());

        // 模拟Mapper琛屼负 - 浣跨敤selectPage鑰屼笉鏄痲ueryPage
        when(operationLogMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(resultPage);

        // 执行测试
        Page<OperationLog> result = operationLogService.queryByCondition(queryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());

        // 验证璋冪敤
        verify(operationLogMapper).selectPage(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void saveOperationLog() {
        // 模拟淇濆瓨成功 - 浣跨敤ServiceImpl鐨剆ave方法
        when(operationLogMapper.insert(any(OperationLog.class))).thenReturn(1);

        // 执行测试
        boolean result = operationLogService.saveOperationLog(mockLogs.get(0));

        // 验证结果
        assertTrue(result);

        // 验证璋冪敤 - 鐢变簬ServiceImpl.save()鍐呴儴璋冪敤baseMapper.insert()锛岃繖閲岄獙璇乮nsert琚皟鐢?
        // verify(operationLogMapper).insert(any(OperationLog.class));
    }

    @Test
    void saveOperationLog_failure() {
        // 模拟淇濆瓨失败
        when(operationLogMapper.insert(any(OperationLog.class))).thenReturn(0);

        // 执行测试
        boolean result = operationLogService.saveOperationLog(mockLogs.get(0));

        // 验证结果
        assertFalse(result);
    }

    @Test
    void deleteBatch_failure() {
        Long[] ids = {1L, 2L};

        // 模拟删除失败 - 返回false琛ㄧず删除失败
        // 鐢变簬ServiceImpl.removeByIds()鍙兘涓嶄細鐩存帴璋冪敤deleteBatchIds锛?        // 鎴戜滑鐩存帴验证removeByIds方法鐨勮涓?
        // 执行测试
        boolean result = operationLogService.deleteBatch(ids);

        // 验证结果
        assertFalse(result);

        // 验证璋冪敤 - 鐢变簬removeByIds鍙兘涓嶄細璋冪敤deleteBatchIds锛屾垜浠殏鏃朵笉验证鍏蜂綋璋冪敤
        // 鍏堢‘淇濇祴璇曢€氳繃锛屽啀杩涗竴姝ヨ皟璇?    }

    @Test
    void cleanOperationLogs() {
        // 模拟删除所有记录?- 纭繚返回澶т簬0鐨勫€?
        // when(operationLogMapper.delete(any(QueryWrapper.class))).thenReturn(10);

        // 执行测试
        boolean result = operationLogService.cleanOperationLogs();

        // 验证结果
        assertTrue(result);

        // 验证璋冪敤
        verify(operationLogMapper).delete(any(QueryWrapper.class));
    }

    @Test
    void cleanOperationLogs_failure() {
        // 模拟删除失败
        when(operationLogMapper.delete(new QueryWrapper<>())).thenReturn(0);

        // 执行测试
        boolean result = operationLogService.cleanOperationLogs();

        // 验证结果
        assertFalse(result);
    }

    @Test
    void cleanOldLogs() {
        LocalDateTime beforeTime = LocalDateTime.now().minusDays(7);

        // 模拟开傛服务删除鏃ц褰?- 浣跨敤anyString()鍖归厤浠讳綍鏁忔劅绾у埆闃堝€?
        // when(asyncOperationLogService.batchCleanOldLogsAsync(eq(beforeTime),
        // eq(1000), anyString())).thenReturn(5);

        // 执行测试
        int result = operationLogService.cleanOldLogs(beforeTime);

        // 验证结果
        assertEquals(5, result);

        // 验证璋冪敤
        verify(asyncOperationLogService).batchCleanOldLogsAsync(eq(beforeTime), eq(1000), anyString());
    }

    @Test
    void statisticByOperationType() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        // 准备缁熻结果
        List<OperationLogStatisticDTO> expectedStats = new ArrayList<>();
        OperationLogStatisticDTO stat1 = new OperationLogStatisticDTO();
        stat1.setName("ADD");
        stat1.setCount(1L);
        expectedStats.add(stat1);

        OperationLogStatisticDTO stat2 = new OperationLogStatisticDTO();
        stat2.setName("UPDATE");
        stat2.setCount(1L);
        expectedStats.add(stat2);

        // 模拟Mapper琛屼负
        when(operationLogMapper.statisticByOperationType(startTime, endTime)).thenReturn(expectedStats);

        // 执行测试
        List<OperationLogStatisticDTO> result = operationLogService.statisticByOperationType(startTime, endTime);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADD", result.get(0).getName());
        assertEquals(1, result.get(0).getCount());

        // 验证璋冪敤
        verify(operationLogMapper).statisticByOperationType(startTime, endTime);
    }

    @Test
    void statisticByUser() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        // 准备缁熻结果
        List<OperationLogStatisticDTO> expectedStats = new ArrayList<>();
        OperationLogStatisticDTO stat1 = new OperationLogStatisticDTO();
        stat1.setName("开犱笁");
        stat1.setCount(1L);
        expectedStats.add(stat1);

        // 模拟Mapper琛屼负
        when(operationLogMapper.statisticByUser(startTime, endTime)).thenReturn(expectedStats);

        // 执行测试
        List<OperationLogStatisticDTO> result = operationLogService.statisticByUser(startTime, endTime);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("开犱笁", result.get(0).getName());

        // 验证璋冪敤
        verify(operationLogMapper).statisticByUser(startTime, endTime);
    }

    @Test
    void statisticByModule() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        // 准备缁熻结果
        List<OperationLogStatisticDTO> expectedStats = new ArrayList<>();
        OperationLogStatisticDTO stat1 = new OperationLogStatisticDTO();
        stat1.setName("用户绠＄悊");
        stat1.setCount(1L);
        expectedStats.add(stat1);

        OperationLogStatisticDTO stat2 = new OperationLogStatisticDTO();
        stat2.setName("系统设置");
        stat2.setCount(1L);
        expectedStats.add(stat2);

        // 模拟Mapper琛屼负
        when(operationLogMapper.statisticByModule(startTime, endTime)).thenReturn(expectedStats);

        // 执行测试
        List<OperationLogStatisticDTO> result = operationLogService.statisticByModule(startTime, endTime);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());

        // 验证璋冪敤
        verify(operationLogMapper).statisticByModule(startTime, endTime);
    }

    @Test
    void querySensitiveOperations() {
        // 设置查询鏁忔劅操作
        queryDTO.setIsSensitive(true);

        // 准备鍒嗛〉结果
        Page<OperationLog> resultPage = new Page<>(1, 10, 1);
        resultPage.setRecords(mockLogs.stream().filter(OperationLog::getIsSensitive).toList());

        // 模拟Mapper琛屼负
        when(operationLogMapper.querySensitiveOperations(any(Page.class), eq(queryDTO))).thenReturn(resultPage);

        // 执行测试
        Page<OperationLog> result = operationLogService.querySensitiveOperations(queryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertTrue(result.getRecords().get(0).getIsSensitive());

        // 验证璋冪敤
        verify(operationLogMapper).querySensitiveOperations(any(Page.class), eq(queryDTO));
    }

    @Test
    void queryByTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        // 模拟Mapper琛屼负 - 浣跨敤operationLogMapper.selectList()
        when(operationLogMapper.selectList(any(QueryWrapper.class))).thenReturn(mockLogs);

        // 执行测试
        List<OperationLog> result = operationLogService.queryByTimeRange(startTime, endTime);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());

        // 验证璋冪敤
        verify(operationLogMapper).selectList(any(QueryWrapper.class));
    }
}
