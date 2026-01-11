package com.mold.digitalization.service.impl;

import com.mold.digitalization.dao.MoldTypeMapper;
import com.mold.digitalization.entity.MoldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoldTypeServiceImplTest {

    @Mock
    private MoldTypeMapper moldTypeMapper;

    @InjectMocks
    private MoldTypeServiceImpl moldTypeService;

    private MoldType testMoldType;

    @BeforeEach
    void setUp() {
        // 鍒濆鍖栨祴璇曟暟鎹?        testMoldType = new MoldType();
        testMoldType.setId(1L);
        testMoldType.setTypeCode("TYPE001");
        testMoldType.setTypeName("娉ㄥ妯″叿");
        testMoldType.setDescription("娉ㄥ鎴愬瀷鐢ㄦā鍏?);"
        testMoldType.setCreateTime(LocalDateTime.now());
        testMoldType.setUpdateTime(LocalDateTime.now());

        // 设置鐖剁被Mapper
        try {
            Field mapperField = moldTypeService.getClass().getSuperclass().getDeclaredField("baseMapper");
            mapperField.setAccessible(true);
            mapperField.set(moldTypeService, moldTypeMapper);
        } catch (Exception e) {
            fail("Failed to set baseMapper field: " + e.getMessage());
        }
    }

    @Test
    void getMoldTypeByCode_ShouldReturnMoldType_WhenCodeExists() {
        // Arrange
        String typeCode = "TYPE001";
        when(moldTypeMapper.selectByTypeCode(typeCode)).thenReturn(testMoldType);

        // Act
        MoldType result = moldTypeService.getMoldTypeByCode(typeCode);

        // Assert
        assertNotNull(result);
        assertEquals(typeCode, result.getTypeCode());
        verify(moldTypeMapper, times(1)).selectByTypeCode(typeCode);
    }

    @Test
    void getMoldTypeByCode_ShouldReturnNull_WhenCodeNotExists() {
        // Arrange
        String typeCode = "NON_EXISTENT";
        when(moldTypeMapper.selectByTypeCode(typeCode)).thenReturn(null);

        // Act
        MoldType result = moldTypeService.getMoldTypeByCode(typeCode);

        // Assert
        assertNull(result);
        verify(moldTypeMapper, times(1)).selectByTypeCode(typeCode);
    }

    @Test
    void getAllMoldTypes_ShouldReturnMoldTypeList() {
        // Arrange
        List<MoldType> expectedTypes = Arrays.asList(testMoldType);
        when(moldTypeMapper.selectList(any())).thenReturn(expectedTypes);

        // Act
        List<MoldType> result = moldTypeService.getAllMoldTypes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(moldTypeMapper, times(1)).selectList(any());
    }

    @Test
    void createMoldType_ShouldSaveMoldType_WhenMoldTypeIsValid() {
        // Arrange
        when(moldTypeMapper.insert(any(MoldType.class))).thenReturn(1);

        // Act
        boolean result = moldTypeService.createMoldType(testMoldType);

        // Assert
        assertTrue(result);
        verify(moldTypeMapper, times(1)).insert(any(MoldType.class));
    }

    @Test
    void createMoldType_ShouldReturnFalse_WhenInsertFails() {
        // Arrange
        when(moldTypeMapper.insert(any(MoldType.class))).thenReturn(0);

        // Act
        boolean result = moldTypeService.createMoldType(testMoldType);

        // Assert
        assertFalse(result);
        verify(moldTypeMapper, times(1)).insert(any(MoldType.class));
    }

    @Test
    void updateMoldType_ShouldUpdateMoldType_WhenMoldTypeExists() {
        // Arrange
        when(moldTypeMapper.updateById(any(MoldType.class))).thenReturn(1);

        // Act
        boolean result = moldTypeService.updateMoldType(testMoldType);

        // Assert
        assertTrue(result);
        verify(moldTypeMapper, times(1)).updateById(any(MoldType.class));
    }

    @Test
    void updateMoldType_ShouldReturnFalse_WhenUpdateFails() {
        // Arrange
        when(moldTypeMapper.updateById(any(MoldType.class))).thenReturn(0);

        // Act
        boolean result = moldTypeService.updateMoldType(testMoldType);

        // Assert
        assertFalse(result);
        verify(moldTypeMapper, times(1)).updateById(any(MoldType.class));
    }

    @Test
    void deleteMoldType_ShouldDeleteMoldType_WhenMoldTypeExists() {
        // Arrange
        Long typeId = 1L;
        when(moldTypeMapper.selectById(typeId)).thenReturn(testMoldType);
        when(moldTypeMapper.deleteById(typeId)).thenReturn(1);

        // Act
        boolean result = moldTypeService.deleteMoldType(typeId);

        // Assert
        assertTrue(result);
        verify(moldTypeMapper, times(1)).selectById(typeId);
        verify(moldTypeMapper, times(1)).deleteById(typeId);
    }

    @Test
    void deleteMoldType_ShouldReturnFalse_WhenDeleteFails() {
        // Arrange
        Long typeId = 999L;
        when(moldTypeMapper.selectById(typeId)).thenReturn(null);

        // Act
        boolean result = moldTypeService.deleteMoldType(typeId);

        // Assert
        assertFalse(result);
        verify(moldTypeMapper, times(1)).selectById(typeId);
        verify(moldTypeMapper, never()).deleteById(typeId);
    }

    @Test
    void getById_ShouldReturnMoldType_WhenIdExists() {
        // Arrange
        Long typeId = 1L;
        when(moldTypeMapper.selectById(typeId)).thenReturn(testMoldType);

        // Act
        MoldType result = moldTypeService.getById(typeId);

        // Assert
        assertNotNull(result);
        assertEquals(typeId, result.getId());
        verify(moldTypeMapper, times(1)).selectById(typeId);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdNotExists() {
        // Arrange
        Long typeId = 999L;
        when(moldTypeMapper.selectById(typeId)).thenReturn(null);

        // Act
        MoldType result = moldTypeService.getById(typeId);

        // Assert
        assertNull(result);
        verify(moldTypeMapper, times(1)).selectById(typeId);
    }
}