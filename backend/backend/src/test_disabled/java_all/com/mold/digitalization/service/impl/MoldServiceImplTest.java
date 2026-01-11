package com.mold.digitalization.service.impl;

import com.mold.digitalization.dao.MoldMapper;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.entity.MoldType;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoldServiceImplTest {

    @Mock
    private MoldMapper moldMapper;

    @InjectMocks
    private MoldServiceImpl moldService;

    private Mold testMold;
    private MoldType testMoldType;
    private MoldStatus testMoldStatus;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曟暟鎹?        testMoldType = new MoldType();
        testMoldType.setId(1L);
        testMoldType.setTypeCode("TYPE001");
        testMoldType.setTypeName("娉ㄥ妯″叿");

        testMoldStatus = new MoldStatus();
        testMoldStatus.setId(1L);
        testMoldStatus.setStatusCode("STATUS001");
        testMoldStatus.setStatusName("姝ｅ父");

        testMold = new Mold();
        testMold.setId(1L);
        testMold.setMoldCode("MOLD001");
        testMold.setMoldName("娉ㄥ妯″叿A");
        testMold.setMoldTypeId(1L);
        testMold.setMoldStatusId(1L);
        testMold.setCreateTime(LocalDateTime.now());
        testMold.setUpdateTime(LocalDateTime.now());

        // 设置鐖剁被Mapper
        try {
            Field mapperField = moldService.getClass().getSuperclass().getDeclaredField("baseMapper");
            mapperField.setAccessible(true);
            mapperField.set(moldService, moldMapper);
        } catch (Exception e) {
            fail("Failed to set baseMapper field: " + e.getMessage());
        }
    }

    @Test
    void getMoldByCode_ShouldReturnMold_WhenCodeExists() {
        // Arrange
        String moldCode = "MOLD001";
        when(moldMapper.selectByMoldCode(moldCode)).thenReturn(testMold);

        // Act
        Mold result = moldService.getMoldByCode(moldCode);

        // Assert
        assertNotNull(result);
        assertEquals(moldCode, result.getMoldCode());
        verify(moldMapper, times(1)).selectByMoldCode(moldCode);
    }

    @Test
    void getMoldByCode_ShouldReturnNull_WhenCodeNotExists() {
        // Arrange
        String moldCode = "NON_EXISTENT";
        when(moldMapper.selectByMoldCode(moldCode)).thenReturn(null);

        // Act
        Mold result = moldService.getMoldByCode(moldCode);

        // Assert
        assertNull(result);
        verify(moldMapper, times(1)).selectByMoldCode(moldCode);
    }

    @Test
    void getMoldsByTypeId_ShouldReturnMoldList_WhenTypeIdExists() {
        // Arrange
        Long typeId = 1L;
        List<Mold> expectedMolds = Arrays.asList(testMold);
        when(moldMapper.selectByTypeId(typeId)).thenReturn(expectedMolds);

        // Act
        List<Mold> result = moldService.getMoldsByTypeId(typeId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(typeId, result.get(0).getMoldTypeId());
        verify(moldMapper, times(1)).selectByTypeId(typeId);
    }

    @Test
    void getMoldsByTypeId_ShouldReturnEmptyList_WhenTypeIdNotExists() {
        // Arrange
        Long typeId = 999L;
        when(moldMapper.selectByTypeId(typeId)).thenReturn(Arrays.asList());

        // Act
        List<Mold> result = moldService.getMoldsByTypeId(typeId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(moldMapper, times(1)).selectByTypeId(typeId);
    }

    @Test
    void getMoldsByStatusId_ShouldReturnMoldList_WhenStatusIdExists() {
        // Arrange
        Long statusId = 1L;
        List<Mold> expectedMolds = Arrays.asList(testMold);
        when(moldMapper.selectByStatusId(statusId)).thenReturn(expectedMolds);

        // Act
        List<Mold> result = moldService.getMoldsByStatusId(statusId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statusId, result.get(0).getMoldStatusId());
        verify(moldMapper, times(1)).selectByStatusId(statusId);
    }

    @Test
    void updateMoldStatus_ShouldUpdateStatus_WhenMoldExists() {
        // Arrange
        Long moldId = 1L;
        Long newStatusId = 2L;
        when(moldMapper.selectById(moldId)).thenReturn(testMold);
        when(moldMapper.updateById(any(Mold.class))).thenReturn(1);

        // Act
        boolean result = moldService.updateMoldStatus(moldId, newStatusId);

        // Assert
        assertTrue(result);
        verify(moldMapper, times(1)).selectById(moldId);
        verify(moldMapper, times(1)).updateById(any(Mold.class));
    }

    @Test
    void updateMoldStatus_ShouldReturnFalse_WhenMoldNotExists() {
        // Arrange
        Long moldId = 999L;
        Long newStatusId = 2L;
        when(moldMapper.selectById(moldId)).thenReturn(null);

        // Act
        boolean result = moldService.updateMoldStatus(moldId, newStatusId);

        // Assert
        assertFalse(result);
        verify(moldMapper, times(1)).selectById(moldId);
        verify(moldMapper, never()).updateById(any(Mold.class));
    }

    @Test
    void createMold_ShouldSaveMold_WhenMoldIsValid() {
        // Arrange
        when(moldMapper.insert(any(Mold.class))).thenReturn(1);

        // Act
        boolean result = moldService.createMold(testMold);

        // Assert
        assertTrue(result);
        verify(moldMapper, times(1)).insert(any(Mold.class));
    }

    @Test
    void createMold_ShouldReturnFalse_WhenInsertFails() {
        // Arrange
        when(moldMapper.insert(any(Mold.class))).thenReturn(0);

        // Act
        boolean result = moldService.createMold(testMold);

        // Assert
        assertFalse(result);
        verify(moldMapper, times(1)).insert(any(Mold.class));
    }

    @Test
    void updateMold_ShouldUpdateMold_WhenMoldExists() {
        // Arrange
        when(moldMapper.updateById(any(Mold.class))).thenReturn(1);

        // Act
        boolean result = moldService.updateMold(testMold);

        // Assert
        assertTrue(result);
        verify(moldMapper, times(1)).updateById(any(Mold.class));
    }

    @Test
    void updateMold_ShouldReturnFalse_WhenUpdateFails() {
        // Arrange
        when(moldMapper.updateById(any(Mold.class))).thenReturn(0);

        // Act
        boolean result = moldService.updateMold(testMold);

        // Assert
        assertFalse(result);
        verify(moldMapper, times(1)).updateById(any(Mold.class));
    }

    @Test
    void deleteMold_ShouldDeleteMold_WhenMoldExists() {
        // Arrange
        Long moldId = 1L;
        when(moldMapper.selectById(moldId)).thenReturn(testMold);
        when(moldMapper.deleteById(moldId)).thenReturn(1);

        // Act
        boolean result = moldService.deleteMold(moldId);

        // Assert
        assertTrue(result);
        verify(moldMapper, times(1)).selectById(moldId);
        verify(moldMapper, times(1)).deleteById(moldId);
    }

    @Test
    void deleteMold_ShouldReturnFalse_WhenDeleteFails() {
        // Arrange
        Long moldId = 999L;
        when(moldMapper.selectById(moldId)).thenReturn(null);

        // Act
        boolean result = moldService.deleteMold(moldId);

        // Assert
        assertFalse(result);
        verify(moldMapper, times(1)).selectById(moldId);
        verify(moldMapper, never()).deleteById(moldId);
    }

    @Test
    void getById_ShouldReturnMold_WhenIdExists() {
        // Arrange
        Long moldId = 1L;
        when(moldMapper.selectById(moldId)).thenReturn(testMold);

        // Act
        Mold result = moldService.getById(moldId);

        // Assert
        assertNotNull(result);
        assertEquals(moldId, result.getId());
        verify(moldMapper, times(1)).selectById(moldId);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdNotExists() {
        // Arrange
        Long moldId = 999L;
        when(moldMapper.selectById(moldId)).thenReturn(null);

        // Act
        Mold result = moldService.getById(moldId);

        // Assert
        assertNull(result);
        verify(moldMapper, times(1)).selectById(moldId);
    }
}