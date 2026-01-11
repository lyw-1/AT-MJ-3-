package com.mold.digitalization.service.impl;

import com.mold.digitalization.dao.EquipmentTypeMapper;
import com.mold.digitalization.entity.EquipmentType;
import com.mold.digitalization.service.EquipmentTypeService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 璁惧绫诲瀷服务实现绫诲崟鍏冩祴璇?
 */
@ExtendWith(MockitoExtension.class)
class EquipmentTypeServiceImplTest {

    @Mock
    private EquipmentTypeMapper equipmentTypeMapper;

    @InjectMocks
    private EquipmentTypeServiceImpl equipmentTypeService;

    private EquipmentType testEquipmentType;

    @BeforeEach
    void setUp() throws Exception {
        // 设置baseMapper瀛楁
        Field baseMapperField = equipmentTypeService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(equipmentTypeService, equipmentTypeMapper);

        // 创建测试璁惧绫诲瀷
        testEquipmentType = new EquipmentType();
        testEquipmentType.setId(1L);
        testEquipmentType.setTypeCode("TYPE001");
        testEquipmentType.setTypeName("注塑机类别");
        testEquipmentType.setDescription("注塑机设备类别");
        testEquipmentType.setSortOrder(1);
        testEquipmentType.setCreateTime(LocalDateTime.now());
        testEquipmentType.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void getEquipmentTypeByCode_ShouldReturnEquipmentType_WhenEquipmentTypeExists() {
        // Arrange
        String typeCode = "TYPE001";
        when(equipmentTypeMapper.selectByTypeCode(typeCode)).thenReturn(testEquipmentType);

        // Act
        EquipmentType result = equipmentTypeService.getEquipmentTypeByCode(typeCode);

        // Assert
        assertNotNull(result);
        assertEquals(typeCode, result.getTypeCode());
        verify(equipmentTypeMapper, times(1)).selectByTypeCode(typeCode);
    }

    @Test
    void getEquipmentTypeByCode_ShouldReturnNull_WhenEquipmentTypeNotExists() {
        // Arrange
        String typeCode = "TYPE999";
        when(equipmentTypeMapper.selectByTypeCode(typeCode)).thenReturn(null);

        // Act
        EquipmentType result = equipmentTypeService.getEquipmentTypeByCode(typeCode);

        // Assert
        assertNull(result);
        verify(equipmentTypeMapper, times(1)).selectByTypeCode(typeCode);
    }

    @Test
    void createEquipmentType_ShouldCreateEquipmentType_WhenValidEquipmentType() {
        // Arrange
        when(equipmentTypeMapper.insert(any(EquipmentType.class))).thenReturn(1);

        // Act
        boolean result = equipmentTypeService.createEquipmentType(testEquipmentType);

        // Assert
        assertTrue(result);
        verify(equipmentTypeMapper, times(1)).insert(testEquipmentType);
    }

    @Test
    void createEquipmentType_ShouldReturnFalse_WhenInsertFails() {
        // Arrange
        when(equipmentTypeMapper.insert(any(EquipmentType.class))).thenReturn(0);

        // Act
        boolean result = equipmentTypeService.createEquipmentType(testEquipmentType);

        // Assert
        assertFalse(result);
        verify(equipmentTypeMapper, times(1)).insert(testEquipmentType);
    }

    @Test
    void updateEquipmentType_ShouldUpdateEquipmentType_WhenValidEquipmentType() {
        // Arrange
        when(equipmentTypeMapper.updateById(any(EquipmentType.class))).thenReturn(1);

        // Act
        boolean result = equipmentTypeService.updateEquipmentType(testEquipmentType);

        // Assert
        assertTrue(result);
        verify(equipmentTypeMapper, times(1)).updateById(testEquipmentType);
    }

    @Test
    void updateEquipmentType_ShouldReturnFalse_WhenUpdateFails() {
        // Arrange
        when(equipmentTypeMapper.updateById(any(EquipmentType.class))).thenReturn(0);

        // Act
        boolean result = equipmentTypeService.updateEquipmentType(testEquipmentType);

        // Assert
        assertFalse(result);
        verify(equipmentTypeMapper, times(1)).updateById(testEquipmentType);
    }

    @Test
    void deleteEquipmentType_ShouldDeleteEquipmentType_WhenEquipmentTypeExists() {
        // Arrange
        Long typeId = 1L;
        when(equipmentTypeMapper.selectById(typeId)).thenReturn(testEquipmentType);
        when(equipmentTypeMapper.deleteById(typeId)).thenReturn(1);

        // Act
        boolean result = equipmentTypeService.deleteEquipmentType(typeId);

        // Assert
        assertTrue(result);
        verify(equipmentTypeMapper, times(1)).selectById(typeId);
        verify(equipmentTypeMapper, times(1)).deleteById(typeId);
    }

    @Test
    void deleteEquipmentType_ShouldReturnFalse_WhenEquipmentTypeNotExists() {
        // Arrange
        Long typeId = 999L;
        when(equipmentTypeMapper.selectById(typeId)).thenReturn(null);

        // Act
        boolean result = equipmentTypeService.deleteEquipmentType(typeId);

        // Assert
        assertFalse(result);
        verify(equipmentTypeMapper, times(1)).selectById(typeId);
        verify(equipmentTypeMapper, never()).deleteById(typeId);
    }

    @Test
    void deleteEquipmentType_ShouldReturnFalse_WhenDeleteFails() {
        // Arrange
        Long typeId = 1L;
        when(equipmentTypeMapper.selectById(typeId)).thenReturn(testEquipmentType);
        when(equipmentTypeMapper.deleteById(typeId)).thenReturn(0);

        // Act
        boolean result = equipmentTypeService.deleteEquipmentType(typeId);

        // Assert
        assertFalse(result);
        verify(equipmentTypeMapper, times(1)).selectById(typeId);
        verify(equipmentTypeMapper, times(1)).deleteById(typeId);
    }

    @Test
    void getAllEquipmentTypes_ShouldReturnAllEquipmentTypes() {
        // Arrange
        List<EquipmentType> equipmentTypeList = Arrays.asList(testEquipmentType);
        when(equipmentTypeMapper.selectList(any())).thenReturn(equipmentTypeList);

        // Act
        List<EquipmentType> result = equipmentTypeService.getAllEquipmentTypes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(equipmentTypeMapper, times(1)).selectList(any());
    }

    @Test
    void getById_ShouldReturnEquipmentType_WhenEquipmentTypeExists() {
        // Arrange
        Long typeId = 1L;
        when(equipmentTypeMapper.selectById(typeId)).thenReturn(testEquipmentType);

        // Act
        EquipmentType result = equipmentTypeService.getById(typeId);

        // Assert
        assertNotNull(result);
        assertEquals(typeId, result.getId());
        verify(equipmentTypeMapper, times(1)).selectById(typeId);
    }

    @Test
    void getById_ShouldReturnNull_WhenEquipmentTypeNotExists() {
        // Arrange
        Long typeId = 999L;
        when(equipmentTypeMapper.selectById(typeId)).thenReturn(null);

        // Act
        EquipmentType result = equipmentTypeService.getById(typeId);

        // Assert
        assertNull(result);
        verify(equipmentTypeMapper, times(1)).selectById(typeId);
    }

}
