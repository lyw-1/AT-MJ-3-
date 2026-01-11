package com.mold.digitalization.service.impl;

import com.mold.digitalization.dao.EquipmentMapper;
import com.mold.digitalization.entity.Equipment;
import com.mold.digitalization.service.EquipmentService;
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
 * 璁惧服务实现绫诲崟鍏冩祴璇?
 */
@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplTest {

    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    private Equipment testEquipment;

    @BeforeEach
    void setUp() throws Exception {
        // 设置baseMapper瀛楁
        Field baseMapperField = equipmentService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(equipmentService, equipmentMapper);

        // 创建测试璁惧
        testEquipment = new Equipment();
        testEquipment.setId(1L);
        testEquipment.setEquipmentCode("EQ001");
        testEquipment.setEquipmentName("注塑机");
        testEquipment.setEquipmentTypeId(1L);
        testEquipment.setStatus(1);
        testEquipment.setLocation("鐢熶骇杞﹂棿A");
        testEquipment.setResponsibleUserId(1L);
        testEquipment.setPurchaseTime(LocalDateTime.now());
        testEquipment.setRemark("测试璁惧");
        testEquipment.setCreateTime(LocalDateTime.now());
        testEquipment.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void getEquipmentByCode_ShouldReturnEquipment_WhenEquipmentExists() {
        // Arrange
        String equipmentCode = "EQ001";
        when(equipmentMapper.selectByEquipmentCode(equipmentCode)).thenReturn(testEquipment);

        // Act
        Equipment result = equipmentService.getEquipmentByCode(equipmentCode);

        // Assert
        assertNotNull(result);
        assertEquals(equipmentCode, result.getEquipmentCode());
        verify(equipmentMapper, times(1)).selectByEquipmentCode(equipmentCode);
    }

    @Test
    void getEquipmentByCode_ShouldReturnNull_WhenEquipmentNotExists() {
        // Arrange
        String equipmentCode = "EQ999";
        when(equipmentMapper.selectByEquipmentCode(equipmentCode)).thenReturn(null);

        // Act
        Equipment result = equipmentService.getEquipmentByCode(equipmentCode);

        // Assert
        assertNull(result);
        verify(equipmentMapper, times(1)).selectByEquipmentCode(equipmentCode);
    }

    @Test
    void getEquipmentByTypeId_ShouldReturnEquipmentList_WhenEquipmentExists() {
        // Arrange
        Long typeId = 1L;
        List<Equipment> equipmentList = Arrays.asList(testEquipment);
        when(equipmentMapper.selectByTypeId(typeId)).thenReturn(equipmentList);

        // Act
        List<Equipment> result = equipmentService.getEquipmentByTypeId(typeId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(equipmentMapper, times(1)).selectByTypeId(typeId);
    }

    @Test
    void createEquipment_ShouldCreateEquipment_WhenValidEquipment() {
        // Arrange
        when(equipmentMapper.insert(any(Equipment.class))).thenReturn(1);

        // Act
        boolean result = equipmentService.createEquipment(testEquipment);

        // Assert
        assertTrue(result);
        verify(equipmentMapper, times(1)).insert(testEquipment);
    }

    @Test
    void createEquipment_ShouldReturnFalse_WhenInsertFails() {
        // Arrange
        when(equipmentMapper.insert(any(Equipment.class))).thenReturn(0);

        // Act
        boolean result = equipmentService.createEquipment(testEquipment);

        // Assert
        assertFalse(result);
        verify(equipmentMapper, times(1)).insert(testEquipment);
    }

    @Test
    void updateEquipment_ShouldUpdateEquipment_WhenValidEquipment() {
        // Arrange
        when(equipmentMapper.updateById(any(Equipment.class))).thenReturn(1);

        // Act
        boolean result = equipmentService.updateEquipment(testEquipment);

        // Assert
        assertTrue(result);
        verify(equipmentMapper, times(1)).updateById(testEquipment);
    }

    @Test
    void updateEquipment_ShouldReturnFalse_WhenUpdateFails() {
        // Arrange
        when(equipmentMapper.updateById(any(Equipment.class))).thenReturn(0);

        // Act
        boolean result = equipmentService.updateEquipment(testEquipment);

        // Assert
        assertFalse(result);
        verify(equipmentMapper, times(1)).updateById(testEquipment);
    }

    @Test
    void deleteEquipment_ShouldDeleteEquipment_WhenEquipmentExists() {
        // Arrange
        Long equipmentId = 1L;
        when(equipmentMapper.selectById(equipmentId)).thenReturn(testEquipment);
        when(equipmentMapper.deleteById(equipmentId)).thenReturn(1);

        // Act
        boolean result = equipmentService.deleteEquipment(equipmentId);

        // Assert
        assertTrue(result);
        verify(equipmentMapper, times(1)).selectById(equipmentId);
        verify(equipmentMapper, times(1)).deleteById(equipmentId);
    }

    @Test
    void deleteEquipment_ShouldReturnFalse_WhenEquipmentNotExists() {
        // Arrange
        Long equipmentId = 999L;
        when(equipmentMapper.selectById(equipmentId)).thenReturn(null);

        // Act
        boolean result = equipmentService.deleteEquipment(equipmentId);

        // Assert
        assertFalse(result);
        verify(equipmentMapper, times(1)).selectById(equipmentId);
        verify(equipmentMapper, never()).deleteById(equipmentId);
    }

    @Test
    void deleteEquipment_ShouldReturnFalse_WhenDeleteFails() {
        // Arrange
        Long equipmentId = 1L;
        when(equipmentMapper.selectById(equipmentId)).thenReturn(testEquipment);
        when(equipmentMapper.deleteById(equipmentId)).thenReturn(0);

        // Act
        boolean result = equipmentService.deleteEquipment(equipmentId);

        // Assert
        assertFalse(result);
        verify(equipmentMapper, times(1)).selectById(equipmentId);
        verify(equipmentMapper, times(1)).deleteById(equipmentId);
    }

    @Test
    void getAllEquipment_ShouldReturnAllEquipment() {
        // Arrange
        List<Equipment> equipmentList = Arrays.asList(testEquipment);
        when(equipmentMapper.selectList(any())).thenReturn(equipmentList);

        // Act
        List<Equipment> result = equipmentService.getAllEquipment();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(equipmentMapper, times(1)).selectList(any());
    }

    @Test
    void updateEquipmentStatus_ShouldUpdateStatus_WhenValidParameters() {
        // Arrange
        Long equipmentId = 1L;
        Integer status = 2;
        when(equipmentMapper.update(any(), any())).thenReturn(1);

        // Act
        boolean result = equipmentService.updateEquipmentStatus(equipmentId, status);

        // Assert
        assertTrue(result);
        verify(equipmentMapper, times(1)).update(any(), any());
    }

    @Test
    void updateEquipmentStatus_ShouldReturnFalse_WhenUpdateFails() {
        // Arrange
        Long equipmentId = 1L;
        Integer status = 2;
        when(equipmentMapper.update(any(), any())).thenReturn(0);

        // Act
        boolean result = equipmentService.updateEquipmentStatus(equipmentId, status);

        // Assert
        assertFalse(result);
        verify(equipmentMapper, times(1)).update(any(), any());
    }

    @Test
    void getById_ShouldReturnEquipment_WhenEquipmentExists() {
        // Arrange
        Long equipmentId = 1L;
        when(equipmentMapper.selectById(equipmentId)).thenReturn(testEquipment);

        // Act
        Equipment result = equipmentService.getById(equipmentId);

        // Assert
        assertNotNull(result);
        assertEquals(equipmentId, result.getId());
        verify(equipmentMapper, times(1)).selectById(equipmentId);
    }

    @Test
    void getById_ShouldReturnNull_WhenEquipmentNotExists() {
        // Arrange
        Long equipmentId = 999L;
        when(equipmentMapper.selectById(equipmentId)).thenReturn(null);

        // Act
        Equipment result = equipmentService.getById(equipmentId);

        // Assert
        assertNull(result);
        verify(equipmentMapper, times(1)).selectById(equipmentId);
    }
}
