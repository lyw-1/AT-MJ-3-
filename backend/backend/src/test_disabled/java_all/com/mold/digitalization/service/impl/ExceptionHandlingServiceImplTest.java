package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessException;
import com.mold.digitalization.enums.ExceptionLevel;
import com.mold.digitalization.enums.ExceptionStatus;
import com.mold.digitalization.enums.ExceptionType;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.mapper.ProcessExceptionMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 开傚父澶勭悊服务实现绫诲崟鍏冩祴璇?
 * 测试开傚父绠＄悊鐨勬牳蹇冧笟鍔￠€昏緫
 */
@ExtendWith(MockitoExtension.class)
class ExceptionHandlingServiceImplTest {

    @Mock
    private ProcessExceptionMapper processExceptionMapper;

    @Mock
    private MoldProcessMapper moldProcessMapper;

    @InjectMocks
    private ExceptionHandlingServiceImpl exceptionHandlingService;

    private ProcessException testException;
    private MoldProcess testProcess;
    private Page<ProcessException> testPage;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曟暟鎹?
        testException = new ProcessException();
        testException.setId(1L);
        testException.setProcessId(1L);
        testException.setExceptionType(ExceptionType.EQUIPMENT_FAILURE);
        testException.setExceptionLevel(ExceptionLevel.HIGH);
        testException.setTitle("璁惧鏁呴殰");
        testException.setDescription("CNC鏈哄簥涓昏酱开傚父");
        testException.setExceptionStatus(ExceptionStatus.OPEN);
        testException.setReportedBy("OP001");
        testException.setAssignedTo("TECH001");
        testException.setReportTime(LocalDateTime.now());
        testException.setResolveTime(null);
        testException.setResolution(null);
        testException.setCreateTime(LocalDateTime.now());
        testException.setUpdateTime(LocalDateTime.now());

        testProcess = new MoldProcess();
        testProcess.setId(1L);
        testProcess.setMoldId("M001");
        testProcess.setProcessType("CNC");
        testProcess.setCurrentStatus(com.mold.digitalization.enums.ProcessStatus.IN_PROGRESS);

        // 鍒濆鍖栧垎椤垫祴璇曟暟鎹?
        testPage = new Page<>();
        testPage.add(testException);
        testPage.setTotal(1);
    }

    @Test
    void testCreateException() {
        // 准备测试数据
        ProcessException newException = new ProcessException();
        newException.setProcessId(2L);
        newException.setExceptionType(ExceptionType.QUALITY_ISSUE);
        newException.setExceptionLevel(ExceptionLevel.MEDIUM);
        newException.setTitle("璐ㄩ噺闂");
        newException.setDescription("鍔犲伐灏哄鍋忓樊");
        newException.setReportedBy("OP002");

        // 模拟Mapper琛屼负
        when(processExceptionMapper.insert(any(ProcessException.class))).thenReturn(1);
        when(processExceptionMapper.selectById(anyLong())).thenReturn(newException);

        // 执行测试
        ProcessException result = exceptionHandlingService.createException(newException);

        // 验证结果
        assertNotNull(result);
        assertEquals(2L, result.getProcessId());
        assertEquals(ExceptionType.QUALITY_ISSUE, result.getExceptionType());
        assertEquals(ExceptionLevel.MEDIUM, result.getExceptionLevel());
        assertEquals(ExceptionStatus.OPEN, result.getExceptionStatus());

        // 验证方法璋冪敤
        verify(processExceptionMapper).insert(newException);
    }

    @Test
    void testGetExceptionById() {
        // 模拟Mapper琛屼负
        when(processExceptionMapper.selectById(1L)).thenReturn(testException);

        // 执行测试
        ProcessException result = exceptionHandlingService.getExceptionById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getProcessId());
        assertEquals(ExceptionType.EQUIPMENT_FAILURE, result.getExceptionType());

        // 验证方法璋冪敤
        verify(processExceptionMapper).selectById(1L);
    }

    @Test
    void testUpdateException() {
        // 准备更新数据
        testException.setDescription("更新鍚庣殑开傚父鎻忚堪");
        testException.setExceptionStatus(ExceptionStatus.IN_PROGRESS);

        // 模拟Mapper琛屼负
        when(processExceptionMapper.updateById(any(ProcessException.class))).thenReturn(1);
        when(processExceptionMapper.selectById(1L)).thenReturn(testException);

        // 执行测试
        ProcessException result = exceptionHandlingService.updateException(testException);

        // 验证结果
        assertNotNull(result);
        assertEquals("更新鍚庣殑开傚父鎻忚堪", result.getDescription());
        assertEquals(ExceptionStatus.IN_PROGRESS, result.getExceptionStatus());

        // 验证方法璋冪敤
        verify(processExceptionMapper).updateById(testException);
    }

    @Test
    void testDeleteException() {
        // 模拟Mapper琛屼负
        when(processExceptionMapper.deleteById(1L)).thenReturn(1);

        // 执行测试
        boolean result = exceptionHandlingService.deleteException(1L);

        // 验证结果
        assertTrue(result);

        // 验证方法璋冪敤
        verify(processExceptionMapper).deleteById(1L);
    }

    @Test
    void testGetExceptionsByProcessId() {
        // 模拟Mapper琛屼负
        when(processExceptionMapper.selectByProcessId(1L)).thenReturn(Arrays.asList(testException));

        // 执行测试
        List<ProcessException> result = exceptionHandlingService.getExceptionsByProcessId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProcessId());

        // 验证方法璋冪敤
        verify(processExceptionMapper).selectByProcessId(1L);
    }

    @Test
    void testGetExceptionsByPage() {
        // 模拟鍒嗛〉查询
        try (var ignored = PageHelper.startPage(1, 10)) {
            when(processExceptionMapper.selectAll()).thenReturn(Arrays.asList(testException));
        }

        // 执行测试
        PageInfo<ProcessException> result = exceptionHandlingService.getExceptionsByPage(1, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getList().size());
        assertEquals(1L, result.getList().get(0).getProcessId());
    }

    @Test
    void testGetExceptionsByTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        // 模拟Mapper琛屼负
        when(processExceptionMapper.selectByTimeRange(startTime, endTime))
            .thenReturn(Arrays.asList(testException));

        // 执行测试
        List<ProcessException> result = exceptionHandlingService.getExceptionsByTimeRange(startTime, endTime);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProcessId());

        // 验证方法璋冪敤
        verify(processExceptionMapper).selectByTimeRange(startTime, endTime);
    }

    @Test
    void testGetExceptionsByStatus() {
        // 模拟Mapper琛屼负
        when(processExceptionMapper.selectByStatus(ExceptionStatus.OPEN))
            .thenReturn(Arrays.asList(testException));

        // 执行测试
        List<ProcessException> result = exceptionHandlingService.getExceptionsByStatus(ExceptionStatus.OPEN);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ExceptionStatus.OPEN, result.get(0).getExceptionStatus());

        // 验证方法璋冪敤
        verify(processExceptionMapper).selectByStatus(ExceptionStatus.OPEN);
    }

    @Test
    void testGetExceptionsByType() {
        // 模拟Mapper琛屼负
        when(processExceptionMapper.selectByType(ExceptionType.EQUIPMENT_FAILURE))
            .thenReturn(Arrays.asList(testException));

        // 执行测试
        List<ProcessException> result = exceptionHandlingService.getExceptionsByType(ExceptionType.EQUIPMENT_FAILURE);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ExceptionType.EQUIPMENT_FAILURE, result.get(0).getExceptionType());

        // 验证方法璋冪敤
        verify(processExceptionMapper).selectByType(ExceptionType.EQUIPMENT_FAILURE);
    }

    @Test
    void testResolveException() {
        // 准备解决异常的数据
        String resolution = "更换主驱动并重新校准";
        String resolvedBy = "TECH002";

        // 模拟Mapper琛屼负
        when(processExceptionMapper.updateById(any(ProcessException.class))).thenReturn(1);
        when(processExceptionMapper.selectById(1L)).thenReturn(testException);

        // 执行测试
        ProcessException result = exceptionHandlingService.resolveException(1L, resolution, resolvedBy);

        // 验证结果
        assertNotNull(result);
        assertEquals(resolution, result.getResolution());
        assertEquals(resolvedBy, result.getAssignedTo());
        assertEquals(ExceptionStatus.RESOLVED, result.getExceptionStatus());
        assertNotNull(result.getResolveTime());

        // 验证方法璋冪敤
        verify(processExceptionMapper).updateById(any(ProcessException.class));
    }

    @Test
    void testAssignException() {
        // 准备鍒嗛厤开傚父鐨勬暟鎹?
        String assignedTo = "TECH003";

        // 模拟Mapper琛屼负
        when(processExceptionMapper.updateById(any(ProcessException.class))).thenReturn(1);
        when(processExceptionMapper.selectById(1L)).thenReturn(testException);

        // 执行测试
        ProcessException result = exceptionHandlingService.assignException(1L, assignedTo);

        // 验证结果
        assertNotNull(result);
        assertEquals(assignedTo, result.getAssignedTo());
        assertEquals(ExceptionStatus.IN_PROGRESS, result.getExceptionStatus());

        // 验证方法璋冪敤
        verify(processExceptionMapper).updateById(any(ProcessException.class));
    }

    @Test
    void testGetExceptionStatistics() {
        // 准备缁熻数据
        Map<String, Object> expectedStats = Map.of(
            "total", 10,
            "open", 3,
            "inProgress", 4,
            "resolved", 3,
            "highLevel", 2,
            "mediumLevel", 5,
            "lowLevel", 3
        );

        // 模拟Mapper琛屼负
        when(processExceptionMapper.selectStatistics()).thenReturn(expectedStats);

        // 执行测试
        Map<String, Object> result = exceptionHandlingService.getExceptionStatistics();

        // 验证结果
        assertNotNull(result);
        assertEquals(10, result.get("total"));
        assertEquals(3, result.get("open"));
        assertEquals(4, result.get("inProgress"));
        assertEquals(3, result.get("resolved"));

        // 验证方法璋冪敤
        verify(processExceptionMapper).selectStatistics();
    }

    @Test
    void testGetExceptionsByLevel() {
        // 模拟Mapper琛屼负
        when(processExceptionMapper.selectByLevel(ExceptionLevel.HIGH))
            .thenReturn(Arrays.asList(testException));

        // 执行测试
        List<ProcessException> result = exceptionHandlingService.getExceptionsByLevel(ExceptionLevel.HIGH);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ExceptionLevel.HIGH, result.get(0).getExceptionLevel());

        // 验证方法璋冪敤
        verify(processExceptionMapper).selectByLevel(ExceptionLevel.HIGH);
    }

    @Test
    void testGetExceptionsByAssignee() {
        String assignee = "TECH001";

        // 模拟Mapper琛屼负
        when(processExceptionMapper.selectByAssignee(assignee))
            .thenReturn(Arrays.asList(testException));

        // 执行测试
        List<ProcessException> result = exceptionHandlingService.getExceptionsByAssignee(assignee);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(assignee, result.get(0).getAssignedTo());

        // 验证方法璋冪敤
        verify(processExceptionMapper).selectByAssignee(assignee);
    }
}
