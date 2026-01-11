package com.mold.digitalization.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mold.digitalization.entity.ProcessPreset;
import com.mold.digitalization.mapper.ProcessPresetMapper;
import com.mold.digitalization.service.impl.ProcessPresetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 工序预设置服务单元测试
 */
@ExtendWith(MockitoExtension.class)
public class ProcessPresetServiceTest {

    @Mock
    private ProcessPresetMapper processPresetMapper;

    @InjectMocks
    private ProcessPresetServiceImpl processPresetService;

    private ProcessPreset preset;
    private List<ProcessPreset> presetList;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        preset = new ProcessPreset();
        preset.setId(1L);
        preset.setMoldId(1L);
        preset.setProcessCode("PREP");
        preset.setPresetKey("material");
        preset.setPresetValue("H13");
        preset.setDescription("备料材质");
        preset.setCreateTime(LocalDateTime.now());
        preset.setUpdateTime(LocalDateTime.now());

        presetList = new ArrayList<>();
        presetList.add(preset);
    }

    @Test
    void testGetByMoldIdAndProcessCode() {
        // 模拟Mapper返回数据
        when(processPresetMapper.getByMoldIdAndProcessCode(anyLong(), anyString())).thenReturn(presetList);

        // 调用服务方法
        List<ProcessPreset> result = processPresetService.getByMoldIdAndProcessCode(1L, "PREP");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("H13", result.get(0).getPresetValue());
        verify(processPresetMapper, times(1)).getByMoldIdAndProcessCode(1L, "PREP");
    }

    @Test
    void testGetByMoldId() {
        // 模拟Mapper返回数据
        when(processPresetMapper.getByMoldId(anyLong())).thenReturn(presetList);

        // 调用服务方法
        List<ProcessPreset> result = processPresetService.getByMoldId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(processPresetMapper, times(1)).getByMoldId(1L);
    }

    @Test
    void testGetByProcessCode() {
        // 模拟Mapper返回数据
        when(processPresetMapper.getByProcessCode(anyString())).thenReturn(presetList);

        // 调用服务方法
        List<ProcessPreset> result = processPresetService.getByProcessCode("PREP");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(processPresetMapper, times(1)).getByProcessCode("PREP");
    }

    @Test
    void testSaveOrUpdatePreset() {
        // 新增场景
        ProcessPreset newPreset = new ProcessPreset();
        newPreset.setMoldId(1L);
        newPreset.setProcessCode("PREP");
        newPreset.setPresetKey("material");
        newPreset.setPresetValue("H13");

        when(processPresetService.save(any(ProcessPreset.class))).thenReturn(true);

        boolean result = processPresetService.saveOrUpdatePreset(newPreset);
        assertTrue(result);
        verify(processPresetService, times(1)).save(any(ProcessPreset.class));

        // 更新场景
        when(processPresetService.updateById(any(ProcessPreset.class))).thenReturn(true);

        boolean updateResult = processPresetService.saveOrUpdatePreset(preset);
        assertTrue(updateResult);
        verify(processPresetService, times(1)).updateById(any(ProcessPreset.class));
    }

    @Test
    void testGetPresetValue() {
        // 模拟Mapper返回数据
        when(processPresetMapper.getByMoldIdAndProcessCode(anyLong(), anyString())).thenReturn(presetList);

        // 调用服务方法
        String result = processPresetService.getPresetValue(1L, "PREP", "material");

        // 验证结果
        assertNotNull(result);
        assertEquals("H13", result);
        verify(processPresetMapper, times(1)).getByMoldIdAndProcessCode(1L, "PREP");
    }

    @Test
    void testGetPresetMap() {
        // 模拟Mapper返回数据
        when(processPresetMapper.getByMoldIdAndProcessCode(anyLong(), anyString())).thenReturn(presetList);

        // 调用服务方法
        Map<String, String> result = processPresetService.getPresetMap(1L, "PREP");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("H13", result.get("material"));
        verify(processPresetMapper, times(1)).getByMoldIdAndProcessCode(1L, "PREP");
    }

    @Test
    void testDeleteByMoldIdAndProcessCode() {
        // 模拟Mapper返回删除数量
        when(processPresetMapper.deleteByMoldIdAndProcessCode(anyLong(), anyString())).thenReturn(1);

        // 调用服务方法
        boolean result = processPresetService.deleteByMoldIdAndProcessCode(1L, "PREP");

        // 验证结果
        assertTrue(result);
        verify(processPresetMapper, times(1)).deleteByMoldIdAndProcessCode(1L, "PREP");
    }

    @Test
    void testDeleteByMoldId() {
        // 模拟Mapper返回删除数量
        when(processPresetMapper.deleteByMoldId(anyLong())).thenReturn(1);

        // 调用服务方法
        boolean result = processPresetService.deleteByMoldId(1L);

        // 验证结果
        assertTrue(result);
        verify(processPresetMapper, times(1)).deleteByMoldId(1L);
    }
}
