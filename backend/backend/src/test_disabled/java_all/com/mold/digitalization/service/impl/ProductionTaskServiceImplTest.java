package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.ProductionTask;
import com.mold.digitalization.service.ProductionTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 鐢熶骇浠诲姟服务实现绫绘祴璇? */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class ProductionTaskServiceImplTest {

    @Autowired
    private ProductionTaskService productionTaskService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductionTask testProductionTask;

    @BeforeEach
    void setUp() {
        // 鍏堟彃鍏ヤ緷璧栨暟鎹紝閬垮厤澶栭敭绾︽潫
        insertDependencyData();
        
        // 创建测试数据
        testProductionTask = new ProductionTask();
        testProductionTask.setTaskCode("TEST001");
        testProductionTask.setTaskName("测试鐢熶骇浠诲姟");
        testProductionTask.setMoldId(1L); // 浣跨敤宸叉彃鍏ョ殑妯″叿ID
        testProductionTask.setEquipmentId(1L); // 浣跨敤宸叉彃鍏ョ殑璁惧ID
        testProductionTask.setOperatorId(1L); // 浣跨敤宸叉彃鍏ョ殑用户ID
        testProductionTask.setPlannedQuantity(100);
        testProductionTask.setActualQuantity(0);
        testProductionTask.setQualifiedQuantity(0);
        testProductionTask.setDefectiveQuantity(0);
        testProductionTask.setStatus(0);
        testProductionTask.setStartTime(LocalDateTime.now());
        testProductionTask.setEndTime(LocalDateTime.now().plusHours(2));
        testProductionTask.setDescription("测试浠诲姟鎻忚堪");
        testProductionTask.setCreateTime(LocalDateTime.now());
        testProductionTask.setUpdateTime(LocalDateTime.now());
    }
    
    private void insertDependencyData() {
        // 鎻掑叆测试用户
        // 修正表名为 `user`（测试环境使用 schema.sql 创建的用户表），并使用反引号避免与保留字冲突
        String insertUserSql = "INSERT INTO `user` (id, username, password, status, create_time, update_time) " +
                "VALUES (1, 'testuser', 'password', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        // 鎻掑叆测试妯″叿
        // 修正乱码与缺失引号，使用可解析的英文占位数据
        String insertMoldSql = "INSERT INTO mold (id, mold_code, mold_name, mold_type, specification, status, create_time, update_time) " +
                "VALUES (1, 'MOLD001', 'Test Mold', 'Type-A', 'Standard Spec', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        // 鎻掑叆测试璁惧
        String insertEquipmentSql = "INSERT INTO equipment (id, equipment_code, equipment_name, equipment_type, model, status, create_time, update_time) " +
                "VALUES (1, 'EQ001', 'Test Equipment', 'Type-A', 'Model-X', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        
        try {
            jdbcTemplate.execute(insertUserSql);
            jdbcTemplate.execute(insertMoldSql);
            jdbcTemplate.execute(insertEquipmentSql);
        } catch (Exception e) {
            // 濡傛灉数据宸插瓨鍦紝蹇界暐閲嶅鎻掑叆错误
            System.out.println("渚濊禆数据鎻掑叆开傚父锛堝彲鑳藉凡瀛樺湪锛? " + e.getMessage());
        }
    }

    @Test
    void createProductionTask_ShouldCreateTask_WhenValidData() {
        // 执行
        boolean result = productionTaskService.createProductionTask(testProductionTask);
        
        // 验证
        assertTrue(result);
        assertNotNull(testProductionTask.getId());
        
        // 验证数据宸蹭繚瀛?
        ProductionTask savedTask = productionTaskService.getById(testProductionTask.getId());
        assertNotNull(savedTask);
        assertEquals("TEST001", savedTask.getTaskCode());
        assertEquals("测试鐢熶骇浠诲姟", savedTask.getTaskName());
        assertEquals(0, savedTask.getStatus());
    }

    @Test
    void createProductionTask_ShouldThrowException_WhenDuplicateTaskCode() {
        // 准备锛氬厛创建涓€涓换鍔?
        productionTaskService.createProductionTask(testProductionTask);
        
        // 准备锛氬垱寤虹浜屼釜浠诲姟锛堜娇鐢ㄤ笉鍚岀殑浠诲姟缂栧彿锛?
        ProductionTask secondTask = new ProductionTask();
        // 使用时间戳和线程ID确保唯一性
        secondTask.setTaskCode("TEST_DUPLICATE_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId());
        secondTask.setTaskName("第二个任务");
        secondTask.setMoldId(1L);
        secondTask.setEquipmentId(1L);
        secondTask.setOperatorId(1L);
        secondTask.setPlannedQuantity(50);
        secondTask.setStatus(0);
        secondTask.setCreateTime(LocalDateTime.now());
        secondTask.setUpdateTime(LocalDateTime.now());
        
        // 鍏堝垱寤虹浜屼釜浠诲姟
        productionTaskService.createProductionTask(secondTask);
        
        // 准备锛氬垱寤虹涓変釜浠诲姟锛屼娇鐢ㄤ笌绗簩涓换鍔＄浉鍚岀殑缂栧彿
        ProductionTask thirdTask = new ProductionTask();
        // 复用第二个任务的编码以模拟重复
        thirdTask.setTaskCode(secondTask.getTaskCode());
        thirdTask.setTaskName("第三个任务");
        thirdTask.setMoldId(1L);
        thirdTask.setEquipmentId(1L);
        thirdTask.setOperatorId(1L);
        thirdTask.setPlannedQuantity(75);
        thirdTask.setStatus(0);
        thirdTask.setCreateTime(LocalDateTime.now());
        thirdTask.setUpdateTime(LocalDateTime.now());
        
        // 执行鍜岄獙璇侊細灏濊瘯创建閲嶅缂栧彿鐨勪换鍔★紝应该鎶涘嚭开傚父
        assertThrows(DataIntegrityViolationException.class, () -> {
            productionTaskService.createProductionTask(thirdTask);
        });
    }

    @Test
    void getProductionTaskByCode_ShouldReturnTask_WhenTaskExists() {
        // 准备
        productionTaskService.createProductionTask(testProductionTask);
        
        // 执行
        ProductionTask foundTask = productionTaskService.getProductionTaskByCode("TEST001");
        
        // 验证
        assertNotNull(foundTask);
        assertEquals("TEST001", foundTask.getTaskCode());
        assertEquals("测试鐢熶骇浠诲姟", foundTask.getTaskName());
    }

    @Test
    void getProductionTaskByCode_ShouldReturnNull_WhenTaskNotExists() {
        // 执行
        ProductionTask foundTask = productionTaskService.getProductionTaskByCode("NON_EXISTENT_CODE");
        
        // 验证
        assertNull(foundTask);
    }

    @Test
    void updateProductionTask_ShouldUpdateTask_WhenValidData() {
        // 准备
        productionTaskService.createProductionTask(testProductionTask);
        Long taskId = testProductionTask.getId();
        
        // 准备更新数据
        ProductionTask updateData = new ProductionTask();
        updateData.setId(taskId);
        updateData.setTaskName("更新鍚庣殑浠诲姟鍚嶇О");
        updateData.setPlannedQuantity(200);
        updateData.setStatus(1); // 杩涜涓?
        updateData.setDescription("更新鍚庣殑鎻忚堪");
        updateData.setUpdateTime(LocalDateTime.now());
        
        // 执行
        boolean result = productionTaskService.updateProductionTask(updateData);
        
        // 验证
        assertTrue(result);
        
        // 验证更新结果
        ProductionTask updatedTask = productionTaskService.getById(taskId);
        assertNotNull(updatedTask);
        assertEquals("更新鍚庣殑浠诲姟鍚嶇О", updatedTask.getTaskName());
        assertEquals(200, updatedTask.getPlannedQuantity());
        assertEquals(1, updatedTask.getStatus());
        assertEquals("更新鍚庣殑鎻忚堪", updatedTask.getDescription());
    }

    @Test
    void updateProductionTask_ShouldReturnFalse_WhenTaskNotExists() {
        // 准备涓嶅瓨鍦ㄧ殑浠诲姟
        ProductionTask nonExistentTask = new ProductionTask();
        nonExistentTask.setId(9999L); // 涓嶅瓨鍦ㄧ殑ID
        nonExistentTask.setTaskName("涓嶅瓨鍦ㄧ殑浠诲姟");
        
        // 执行
        boolean result = productionTaskService.updateProductionTask(nonExistentTask);
        
        // 验证
        assertFalse(result);
    }

    @Test
    void deleteProductionTask_ShouldDeleteTask_WhenTaskExists() {
        // 准备
        productionTaskService.createProductionTask(testProductionTask);
        Long taskId = testProductionTask.getId();
        
        // 验证浠诲姟瀛樺湪
        assertNotNull(productionTaskService.getById(taskId));
        
        // 执行
        boolean result = productionTaskService.deleteProductionTask(taskId);
        
        // 验证
        assertTrue(result);
        
        // 验证浠诲姟宸茶删除
        assertNull(productionTaskService.getById(taskId));
    }

    @Test
    void deleteProductionTask_ShouldReturnFalse_WhenTaskNotExists() {
        // 执行
        boolean result = productionTaskService.deleteProductionTask(9999L);
        
        // 验证
        assertFalse(result);
    }

    @Test
    void getAllProductionTasks_ShouldReturnAllTasks() {
        // 准备锛氬垱寤哄涓换鍔?
        ProductionTask task1 = new ProductionTask();
        task1.setTaskCode("TASK_001");
        task1.setTaskName("浠诲姟1");
        task1.setMoldId(1L);
        task1.setEquipmentId(1L);
        task1.setOperatorId(1L);
        task1.setPlannedQuantity(100);
        task1.setStatus(0);
        task1.setCreateTime(LocalDateTime.now());
        task1.setUpdateTime(LocalDateTime.now());
        
        ProductionTask task2 = new ProductionTask();
        task2.setTaskCode("TASK_002");
        task2.setTaskName("浠诲姟2");
        task2.setMoldId(1L);
        task2.setEquipmentId(1L);
        task2.setOperatorId(1L);
        task2.setPlannedQuantity(200);
        task2.setStatus(1);
        task2.setCreateTime(LocalDateTime.now());
        task2.setUpdateTime(LocalDateTime.now());
        
        productionTaskService.createProductionTask(task1);
        productionTaskService.createProductionTask(task2);
        
        // 执行
        List<ProductionTask> tasks = productionTaskService.getAllProductionTasks();
        
        // 验证
        assertNotNull(tasks);
        assertTrue(tasks.size() >= 2);
        
        // 验证包含创建的两个任务
        boolean foundTask1 = tasks.stream().anyMatch(t -> "TASK_001".equals(t.getTaskCode()));
        boolean foundTask2 = tasks.stream().anyMatch(t -> "TASK_002".equals(t.getTaskCode()));
        assertTrue(foundTask1);
        assertTrue(foundTask2);
    }

    @Test
    void getProductionTasksByEquipmentId_ShouldReturnTasks_WhenEquipmentHasTasks() {
        // 准备
        productionTaskService.createProductionTask(testProductionTask);
        
        // 执行
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByEquipmentId(1L);
        
        // 验证
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        
        // 验证返回的任务属于指定设备
        boolean hasCorrectEquipment = tasks.stream()
                .allMatch(task -> 1L == task.getEquipmentId());
        assertTrue(hasCorrectEquipment);
    }

    @Test
    void getProductionTasksByEquipmentId_ShouldReturnEmptyList_WhenNoTasksForEquipment() {
        // 执行
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByEquipmentId(9999L);
        
        // 验证
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void getProductionTasksByStatus_ShouldReturnTasks_WhenStatusExists() {
        // 准备
        productionTaskService.createProductionTask(testProductionTask);
        
        // 执行：查询状态为0（未开始）的任务
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByStatus(0);
        
        // 验证
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        
        // 验证返回的任务状态正确
        boolean hasCorrectStatus = tasks.stream()
                .allMatch(task -> 0 == task.getStatus());
        assertTrue(hasCorrectStatus);
    }

    @Test
    void getProductionTasksByStatus_ShouldReturnEmptyList_WhenNoTasksWithStatus() {
        // 执行：查询状态为3（已取消）的任务
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByStatus(3);
        
        // 验证
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void getProductionTasksByMoldId_ShouldReturnTasks_WhenMoldHasTasks() {
        // 准备
        productionTaskService.createProductionTask(testProductionTask);
        
        // 执行
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByMoldId(1L);
        
        // 验证
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        
        // 验证返回的任务属于指定模具
        boolean hasCorrectMold = tasks.stream()
                .allMatch(task -> 1L == task.getMoldId());
        assertTrue(hasCorrectMold);
    }

    @Test
    void getProductionTasksByMoldId_ShouldReturnEmptyList_WhenNoTasksForMold() {
        // 执行
        List<ProductionTask> tasks = productionTaskService.getProductionTasksByMoldId(9999L);
        
        // 验证
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void updateProductionTaskStatus_ShouldUpdateStatus_WhenValidParameters() {
        // 准备
        productionTaskService.createProductionTask(testProductionTask);
        Long taskId = testProductionTask.getId();
        
        // 执行锛氭洿鏂扮姸鎬佷负1锛堣繘琛屼腑锛?
        boolean result = productionTaskService.updateProductionTaskStatus(taskId, 1);
        
        // 验证
        assertTrue(result);
        
        // 验证鐘舵€佸凡更新
        ProductionTask updatedTask = productionTaskService.getById(taskId);
        assertNotNull(updatedTask);
        assertEquals(1, updatedTask.getStatus());
    }

    @Test
    void updateProductionTaskStatus_ShouldReturnFalse_WhenTaskNotExists() {
        // 执行
        boolean result = productionTaskService.updateProductionTaskStatus(9999L, 1);
        
        // 验证
        assertFalse(result);
    }

    @Test
    void updateProductionTaskStatus_ShouldThrowException_WhenInvalidStatus() {
        // 准备
        productionTaskService.createProductionTask(testProductionTask);
        Long taskId = testProductionTask.getId();
        
        // 执行鍜岄獙璇侊細更新鐘舵€佷负999锛堣秴鍑篢INYINT鑼冨洿锛屾暟鎹簱浼氭姏鍑哄紓甯革級
        assertThrows(DataIntegrityViolationException.class, () -> {
            productionTaskService.updateProductionTaskStatus(taskId, 999);
        });
    }
}
