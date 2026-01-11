package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.ProductionRecord;
import com.mold.digitalization.service.ProductionRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 鐢熶骇记录服务实现绫绘祴璇? */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class ProductionRecordServiceImplTest {

    @Autowired
    private ProductionRecordService productionRecordService;

    private ProductionRecord testProductionRecord;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        testProductionRecord = new ProductionRecord();
        testProductionRecord.setTaskId(1L);
        testProductionRecord.setMoldId(1L);
        testProductionRecord.setEquipmentId(1L);
        testProductionRecord.setOperatorId(1L);
        testProductionRecord.setProductionQuantity(100);
        testProductionRecord.setQualifiedQuantity(95);
        testProductionRecord.setDefectiveQuantity(5);
        testProductionRecord.setProductionDate(LocalDateTime.now());
        testProductionRecord.setMoldUsageCount(10);
        testProductionRecord.setEquipmentParameters("{\"speed\":100,\"temperature\":200}");
        testProductionRecord.setRemark("测试鐢熶骇记录");
        testProductionRecord.setCreateTime(LocalDateTime.now());
        testProductionRecord.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void createProductionRecord_ShouldCreateRecord_WhenValidData() {
        // 执行
        boolean result = productionRecordService.createProductionRecord(testProductionRecord);

        // 验证
        assertTrue(result);
        assertNotNull(testProductionRecord.getId());

        // 验证数据宸蹭繚瀛?        ProductionRecord savedRecord = productionRecordService.getById(testProductionRecord.getId());
        assertNotNull(savedRecord);
        assertEquals(1L, savedRecord.getTaskId());
        assertEquals(1L, savedRecord.getMoldId());
        assertEquals(100, savedRecord.getProductionQuantity());
        assertEquals(95, savedRecord.getQualifiedQuantity());
        assertEquals(5, savedRecord.getDefectiveQuantity());
    }

    @Test
    void createProductionRecord_ShouldReturnFalse_WhenMissingRequiredFields() {
        // 准备锛氱己灏戝繀瑕佸瓧娈电殑记录
        ProductionRecord invalidRecord = new ProductionRecord();
        invalidRecord.setProductionQuantity(50);
        invalidRecord.setQualifiedQuantity(48);
        invalidRecord.setDefectiveQuantity(2);
        // 缂哄皯taskId, moldId绛夊繀瑕佸瓧娈?
        // 执行
        boolean result = productionRecordService.createProductionRecord(invalidRecord);

        // 验证锛氱敱浜庢暟鎹簱绾︽潫锛屽簲璇ヨ繑鍥瀎alse
        assertFalse(result);
    }

    @Test
    void updateProductionRecord_ShouldUpdateRecord_WhenValidData() {
        // 准备
        productionRecordService.createProductionRecord(testProductionRecord);
        Long recordId = testProductionRecord.getId();

        // 准备更新数据
        ProductionRecord updateData = new ProductionRecord();
        updateData.setId(recordId);
        updateData.setProductionQuantity(200);
        updateData.setQualifiedQuantity(190);
        updateData.setDefectiveQuantity(10);
        updateData.setRemark("更新鍚庣殑鐢熶骇记录");
        updateData.setUpdateTime(LocalDateTime.now());

        // 执行
        boolean result = productionRecordService.updateProductionRecord(updateData);

        // 验证
        assertTrue(result);

        // 验证更新结果
        ProductionRecord updatedRecord = productionRecordService.getById(recordId);
        assertNotNull(updatedRecord);
        assertEquals(200, updatedRecord.getProductionQuantity());
        assertEquals(190, updatedRecord.getQualifiedQuantity());
        assertEquals(10, updatedRecord.getDefectiveQuantity());
        assertEquals("更新鍚庣殑鐢熶骇记录", updatedRecord.getRemark());
    }

    @Test
    void updateProductionRecord_ShouldReturnFalse_WhenRecordNotExists() {
        // 准备涓嶅瓨鍦ㄧ殑记录
        ProductionRecord nonExistentRecord = new ProductionRecord();
        nonExistentRecord.setId(9999L); // 涓嶅瓨鍦ㄧ殑ID
        nonExistentRecord.setProductionQuantity(100);

        // 执行
        boolean result = productionRecordService.updateProductionRecord(nonExistentRecord);

        // 验证
        assertFalse(result);
    }

    @Test
    void deleteProductionRecord_ShouldDeleteRecord_WhenRecordExists() {
        // 准备
        productionRecordService.createProductionRecord(testProductionRecord);
        Long recordId = testProductionRecord.getId();

        // 验证记录瀛樺湪
        assertNotNull(productionRecordService.getById(recordId));

        // 执行
        boolean result = productionRecordService.deleteProductionRecord(recordId);

        // 验证
        assertTrue(result);

        // 验证记录宸茶删除
        assertNull(productionRecordService.getById(recordId));
    }

    @Test
    void deleteProductionRecord_ShouldReturnFalse_WhenRecordNotExists() {
        // 执行
        boolean result = productionRecordService.deleteProductionRecord(9999L);

        // 验证
        assertFalse(result);
    }

    @Test
    void getAllProductionRecords_ShouldReturnAllRecords() {
        // 准备锛氬垱寤哄涓褰?        ProductionRecord record1 = new ProductionRecord();
        record1.setTaskId(1L);
        record1.setMoldId(1L);
        record1.setEquipmentId(1L);
        record1.setOperatorId(1L);
        record1.setProductionDate(LocalDateTime.now());
        record1.setProductionQuantity(100);
        record1.setQualifiedQuantity(95);
        record1.setDefectiveQuantity(5);
        record1.setMoldUsageCount(1);
        record1.setEquipmentParameters("{}");
        record1.setCreateTime(LocalDateTime.now());
        record1.setUpdateTime(LocalDateTime.now());

        ProductionRecord record2 = new ProductionRecord();
        record2.setTaskId(2L);
        record2.setMoldId(2L);
        record2.setEquipmentId(2L);
        record2.setOperatorId(2L);
        record2.setProductionDate(LocalDateTime.now());
        record2.setProductionQuantity(200);
        record2.setQualifiedQuantity(190);
        record2.setDefectiveQuantity(10);
        record2.setMoldUsageCount(1);
        record2.setEquipmentParameters("{}");
        record2.setCreateTime(LocalDateTime.now());
        record2.setUpdateTime(LocalDateTime.now());

        productionRecordService.createProductionRecord(record1);
        productionRecordService.createProductionRecord(record2);

        // 执行
        List<ProductionRecord> records = productionRecordService.getAllProductionRecords();

        // 验证
        assertNotNull(records);
        assertTrue(records.size() >= 2);

        // 验证鍖呭惈创建鐨勮褰?        boolean foundRecord1 = records.stream().anyMatch(r -> r.getTaskId() == 1L);
        boolean foundRecord2 = records.stream().anyMatch(r -> r.getTaskId() == 2L);
        assertTrue(foundRecord1);
        assertTrue(foundRecord2);
    }

    @Test
    void getProductionRecordsByTaskId_ShouldReturnRecords_WhenTaskHasRecords() {
        // 准备
        productionRecordService.createProductionRecord(testProductionRecord);

        // 执行
        List<ProductionRecord> records = productionRecordService.getProductionRecordsByTaskId(1L);

        // 验证
        assertNotNull(records);
        assertFalse(records.isEmpty());

        // 验证返回的记录属于指定任务
        boolean hasCorrectTask = records.stream()
                .allMatch(record -> 1L == record.getTaskId());
        assertTrue(hasCorrectTask);
    }

    @Test
    void getProductionRecordsByTaskId_ShouldReturnEmptyList_WhenNoRecordsForTask() {
        // 执行
        List<ProductionRecord> records = productionRecordService.getProductionRecordsByTaskId(9999L);

        // 验证
        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    void getProductionRecordsByMoldId_ShouldReturnRecords_WhenMoldHasRecords() {
        // 准备
        productionRecordService.createProductionRecord(testProductionRecord);

        // 执行
        List<ProductionRecord> records = productionRecordService.getProductionRecordsByMoldId(1L);

        // 验证
        assertNotNull(records);
        assertFalse(records.isEmpty());

        // 验证返回的记录属于指定模具
        boolean hasCorrectMold = records.stream()
                .allMatch(record -> 1L == record.getMoldId());
        assertTrue(hasCorrectMold);
    }

    @Test
    void getProductionRecordsByMoldId_ShouldReturnEmptyList_WhenNoRecordsForMold() {
        // 执行
        List<ProductionRecord> records = productionRecordService.getProductionRecordsByMoldId(9999L);

        // 验证
        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    void getProductionRecordsByEquipmentId_ShouldReturnRecords_WhenEquipmentHasRecords() {
        // 准备
        productionRecordService.createProductionRecord(testProductionRecord);

        // 执行
        List<ProductionRecord> records = productionRecordService.getProductionRecordsByEquipmentId(1L);

        // 验证
        assertNotNull(records);
        assertFalse(records.isEmpty());

        // 验证返回的记录都属于指定设备
        boolean hasCorrectEquipment = records.stream()
                .allMatch(record -> 1L == record.getEquipmentId());
        assertTrue(hasCorrectEquipment);
    }

    @Test
    void getProductionRecordsByEquipmentId_ShouldReturnEmptyList_WhenNoRecordsForEquipment() {
        // 执行
        List<ProductionRecord> records = productionRecordService.getProductionRecordsByEquipmentId(9999L);

        // 验证
        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    void createProductionRecord_ShouldHandleLargeQuantityValues() {
        // 准备：测试大数量值
        ProductionRecord largeQuantityRecord = new ProductionRecord();
        largeQuantityRecord.setTaskId(1L);
        largeQuantityRecord.setMoldId(1L);
        largeQuantityRecord.setEquipmentId(1L);
        largeQuantityRecord.setOperatorId(1L);
        largeQuantityRecord.setProductionDate(LocalDateTime.now());
        // 设置超大生产数量
        largeQuantityRecord.setProductionQuantity(1000000);
        largeQuantityRecord.setQualifiedQuantity(999000);
        largeQuantityRecord.setDefectiveQuantity(1000);
        largeQuantityRecord.setMoldUsageCount(1);
        largeQuantityRecord.setEquipmentParameters("{}");
        largeQuantityRecord.setCreateTime(LocalDateTime.now());
        largeQuantityRecord.setUpdateTime(LocalDateTime.now());

        // 执行
        boolean result = productionRecordService.createProductionRecord(largeQuantityRecord);

        // 验证
        assertTrue(result);
        assertNotNull(largeQuantityRecord.getId());

        // 验证数据姝ｇ‘淇濆瓨
        ProductionRecord savedRecord = productionRecordService.getById(largeQuantityRecord.getId());
        assertNotNull(savedRecord);
        assertEquals(1000000, savedRecord.getProductionQuantity());
        assertEquals(999000, savedRecord.getQualifiedQuantity());
        assertEquals(1000, savedRecord.getDefectiveQuantity());
    }

    @Test
    void updateProductionRecord_ShouldHandlePartialUpdates() {
        // 准备
        productionRecordService.createProductionRecord(testProductionRecord);
        Long recordId = testProductionRecord.getId();

        // 准备部分更新数据（只更新备注）
        ProductionRecord partialUpdate = new ProductionRecord();
        partialUpdate.setId(recordId);
        partialUpdate.setRemark("只更新备注");
        partialUpdate.setUpdateTime(LocalDateTime.now());

        // 执行
        boolean result = productionRecordService.updateProductionRecord(partialUpdate);

        // 验证
        assertTrue(result);

        // 验证鍙湁澶囨敞琚洿鏂帮紝鍏朵粬瀛楁淇濇寔涓嶅彉
        ProductionRecord updatedRecord = productionRecordService.getById(recordId);
        assertNotNull(updatedRecord);
        assertEquals("只更新备注", updatedRecord.getRemark());
        // 原值保持不变
        assertEquals(100, updatedRecord.getProductionQuantity());
        assertEquals(95, updatedRecord.getQualifiedQuantity());
        assertEquals(5, updatedRecord.getDefectiveQuantity());
    }

    @Test
    void deleteProductionRecord_ShouldHandleConsecutiveDeletes() {
        // 准备
        productionRecordService.createProductionRecord(testProductionRecord);
        Long recordId = testProductionRecord.getId();

        // 绗竴娆″垹闄?        boolean firstDelete = productionRecordService.deleteProductionRecord(recordId);
        assertTrue(firstDelete);

        // 验证记录宸茶删除
        assertNull(productionRecordService.getById(recordId));

        // 绗簩娆″垹闄わ紙应该返回false锛?        boolean secondDelete = productionRecordService.deleteProductionRecord(recordId);
        assertFalse(secondDelete);
    }

    @Test
    void getProductionRecordsByTaskId_ShouldHandleMultipleRecordsForSameTask() {
        // 准备锛氫负鍚屼竴涓换鍔″垱寤哄涓褰?        ProductionRecord record1 = new ProductionRecord();
        record1.setTaskId(100L);
        record1.setMoldId(1L);
        record1.setEquipmentId(1L);
        record1.setOperatorId(1L);
        record1.setProductionDate(LocalDateTime.now());
        record1.setProductionQuantity(100);
        record1.setQualifiedQuantity(95);
        record1.setDefectiveQuantity(5);
        record1.setMoldUsageCount(1);
        record1.setEquipmentParameters("{}");
        record1.setCreateTime(LocalDateTime.now());
        record1.setUpdateTime(LocalDateTime.now());

        ProductionRecord record2 = new ProductionRecord();
        record2.setTaskId(100L);
        record2.setMoldId(1L);
        record2.setEquipmentId(1L);
        record2.setOperatorId(1L);
        record2.setProductionDate(LocalDateTime.now());
        record2.setProductionQuantity(200);
        record2.setQualifiedQuantity(190);
        record2.setDefectiveQuantity(10);
        record2.setMoldUsageCount(1);
        record2.setEquipmentParameters("{}");
        record2.setCreateTime(LocalDateTime.now());
        record2.setUpdateTime(LocalDateTime.now());

        productionRecordService.createProductionRecord(record1);
        productionRecordService.createProductionRecord(record2);

        // 执行
        List<ProductionRecord> records = productionRecordService.getProductionRecordsByTaskId(100L);

        // 验证
        assertNotNull(records);
        assertEquals(2, records.size());

        // 验证所有记录都属于同一任务
        boolean allSameTask = records.stream()
                .allMatch(record -> 100L == record.getTaskId());
        assertTrue(allSameTask);

        // 验证包含两个不同的记录
        boolean hasRecord1 = records.stream().anyMatch(r -> r.getProductionQuantity() == 100);
        boolean hasRecord2 = records.stream().anyMatch(r -> r.getProductionQuantity() == 200);
        assertTrue(hasRecord1);
        assertTrue(hasRecord2);
    }
}
