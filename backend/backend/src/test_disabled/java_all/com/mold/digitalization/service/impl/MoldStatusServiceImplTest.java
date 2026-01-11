package com.mold.digitalization.service.impl;

import com.mold.digitalization.dao.MoldStatusMapper;
import com.mold.digitalization.entity.MoldStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoldStatusServiceImplTest {

    @Mock
    private MoldStatusMapper moldStatusMapper;

    @InjectMocks
    private MoldStatusServiceImpl moldStatusService;

    private MoldStatus testMoldStatus;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testMoldStatus = new MoldStatus();
        testMoldStatus.setId(1L);
        testMoldStatus.setStatusCode("STATUS001");
        testMoldStatus.setStatusName("正常");
        testMoldStatus.setDescription("模具状态为正常");
        testMoldStatus.setCreateTime(LocalDateTime.now());
        testMoldStatus.setUpdateTime(LocalDateTime.now());

        // 设置鐖剁被Mapper
        try {
            Field mapperField = moldStatusService.getClass().getSuperclass().getDeclaredField("baseMapper");
            mapperField.setAccessible(true);
            mapperField.set(moldStatusService, moldStatusMapper);
        } catch (Exception e) {
            fail("Failed to set baseMapper field: " + e.getMessage());
        }
    }

    @Test
    void getMoldStatusByCode_ShouldReturnMoldStatus_WhenCodeExists() {
        // Arrange
        String statusCode = "STATUS001";
        when(moldStatusMapper.selectByStatusCode(statusCode)).thenReturn(testMoldStatus);

        // Act
        MoldStatus result = moldStatusService.getMoldStatusByCode(statusCode);

        // Assert
        assertNotNull(result);
        assertEquals(statusCode, result.getStatusCode());
        verify(moldStatusMapper, times(1)).selectByStatusCode(statusCode);
    }

    @Test
    void getMoldStatusByCode_ShouldReturnNull_WhenCodeNotExists() {
        // Arrange
        String statusCode = "NON_EXISTENT";
        when(moldStatusMapper.selectByStatusCode(statusCode)).thenReturn(null);

        // Act
        MoldStatus result = moldStatusService.getMoldStatusByCode(statusCode);

        // Assert
        assertNull(result);
        verify(moldStatusMapper, times(1)).selectByStatusCode(statusCode);
    }

    @Test
    void getAllMoldStatuses_ShouldReturnMoldStatusList() {
        // Arrange
        List<MoldStatus> expectedStatuses = Arrays.asList(testMoldStatus);
        when(moldStatusMapper.selectList(any())).thenReturn(expectedStatuses);

        // Act
        List<MoldStatus> result = moldStatusService.getAllMoldStatuses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(moldStatusMapper, times(1)).selectList(any());
    }

    @Test
    void createMoldStatus_ShouldSaveMoldStatus_WhenMoldStatusIsValid() {
        // Arrange
        when(moldStatusMapper.insert(any(MoldStatus.class))).thenReturn(1);

        // Act
        boolean result = moldStatusService.createMoldStatus(testMoldStatus);

        // Assert
        assertTrue(result);
        verify(moldStatusMapper, times(1)).insert(any(MoldStatus.class));
    }

    @Test
    void createMoldStatus_ShouldReturnFalse_WhenInsertFails() {
        // Arrange
        when(moldStatusMapper.insert(any(MoldStatus.class))).thenReturn(0);

        // Act
        boolean result = moldStatusService.createMoldStatus(testMoldStatus);

        // Assert
        assertFalse(result);
        verify(moldStatusMapper, times(1)).insert(any(MoldStatus.class));
    }

    @Test
    void updateMoldStatus_ShouldUpdateMoldStatus_WhenMoldStatusExists() {
        // Arrange
        when(moldStatusMapper.updateById(any(MoldStatus.class))).thenReturn(1);

        // Act
        boolean result = moldStatusService.updateMoldStatus(testMoldStatus);

        // Assert
        assertTrue(result);
        verify(moldStatusMapper, times(1)).updateById(any(MoldStatus.class));
    }

    @Test
    void updateMoldStatus_ShouldReturnFalse_WhenUpdateFails() {
        // Arrange
        when(moldStatusMapper.updateById(any(MoldStatus.class))).thenReturn(0);

        // Act
        boolean result = moldStatusService.updateMoldStatus(testMoldStatus);

        // Assert
        assertFalse(result);
        verify(moldStatusMapper, times(1)).updateById(any(MoldStatus.class));
    }

    @Test
    void deleteMoldStatus_ShouldDeleteMoldStatus_WhenMoldStatusExists() {
        // Arrange
        Long statusId = 1L;
        when(moldStatusMapper.selectById(statusId)).thenReturn(testMoldStatus);
        when(moldStatusMapper.deleteById(statusId)).thenReturn(1);

        // Act
        boolean result = moldStatusService.deleteMoldStatus(statusId);

        // Assert
        assertTrue(result);
        verify(moldStatusMapper, times(1)).selectById(statusId);
        verify(moldStatusMapper, times(1)).deleteById(statusId);
    }

    @Test
    void deleteMoldStatus_ShouldReturnFalse_WhenDeleteFails() {
        // Arrange
        Long statusId = 999L;
        when(moldStatusMapper.selectById(statusId)).thenReturn(null);

        // Act
        boolean result = moldStatusService.deleteMoldStatus(statusId);

        // Assert
        assertFalse(result);
        verify(moldStatusMapper, times(1)).selectById(statusId);
        verify(moldStatusMapper, never()).deleteById(statusId);
    }

    @Test
    void getById_ShouldReturnMoldStatus_WhenIdExists() {
        // Arrange
        Long statusId = 1L;
        when(moldStatusMapper.selectById(statusId)).thenReturn(testMoldStatus);

        // Act
        MoldStatus result = moldStatusService.getById(statusId);

        // Assert
        assertNotNull(result);
        assertEquals(statusId, result.getId());
        verify(moldStatusMapper, times(1)).selectById(statusId);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdNotExists() {
        // Arrange
        Long statusId = 999L;
        when(moldStatusMapper.selectById(statusId)).thenReturn(null);

        // Act
        MoldStatus result = moldStatusService.getById(statusId);

        // Assert
        assertNull(result);
        verify(moldStatusMapper, times(1)).selectById(statusId);
    }
}
