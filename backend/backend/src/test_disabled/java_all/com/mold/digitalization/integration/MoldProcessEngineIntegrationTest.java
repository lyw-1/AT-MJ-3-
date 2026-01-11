package com.mold.digitalization.integration;

import com.mold.digitalization.entity.*;
import com.mold.digitalization.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 妯″叿鍔犲伐娴佺▼开曟搸闆嗘垚测试
 * 测试妯″叿鍔犲伐娴佺▼开曟搸鐨勬牳蹇冨姛鑳斤紝鍖呮嫭娴佺▼绠＄悊銆佺姸鎬佽浆鎹€佽川閲忔楠岀瓑
 */
@SpringBootTest
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper.class,
                springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider.class
        })
})
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional // 浣跨敤浜嬪姟鍥炴粴锛岄伩鍏嶆祴璇曟暟鎹薄鏌?
public class MoldProcessEngineIntegrationTest {

    @Autowired
    private MoldProcessService moldProcessService;

    @Autowired
    private MoldService moldService;

    @Autowired
    private InspectionResultService inspectionResultService;

    @Autowired
    private ProductionTaskService productionTaskService;

    @Autowired
    private ProductionRecordService productionRecordService;

    /**
     * 测试妯″叿鍔犲伐娴佺▼开曟搸核心服务鏄惁姝ｇ‘娉ㄥ叆
     */
    @Test
    void testServiceInjection() {
        assertNotNull(moldProcessService, "MoldProcessService 应当被正确注入");
        assertNotNull(moldService, "MoldService 应当被正确注入");
        assertNotNull(inspectionResultService, "InspectionResultService 应当被正确注入");
        assertNotNull(productionTaskService, "ProductionTaskService 应当被正确注入");
        assertNotNull(productionRecordService, "ProductionRecordService 应当被正确注入");
    }

    /**
     * 测试妯″叿鍔犲伐娴佺▼瀹屾暣鐢熷懡鍛ㄦ湡锛氬垱寤?-> 鐘舵€佽浆鎹?-> 璐ㄩ噺妫€楠?-> 瀹屾垚
     */
    @Test
    void testMoldProcessFullLifecycle() {
        // 1. 创建测试妯″叿
        Mold testMold = createTestMold();
        boolean moldSaved = moldService.save(testMold);
        assertTrue(moldSaved, "测试妯″叿应该淇濆瓨成功");
        assertNotNull(testMold.getId(), "淇濆瓨鍚庣殑妯″叿应该鏈塈D");

        // 2. 创建测试鐢熶骇浠诲姟
        ProductionTask testTask = createTestProductionTask(testMold.getId());
        boolean taskSaved = productionTaskService.save(testTask);
        assertTrue(taskSaved, "测试鐢熶骇浠诲姟应该淇濆瓨成功");
        assertNotNull(testTask.getId(), "淇濆瓨鍚庣殑鐢熶骇浠诲姟应该鏈塈D");

        // 3. 创建妯″叿鍔犲伐娴佺▼
        MoldProcess testProcess = createTestMoldProcess(testMold.getId(), testTask.getId());
        boolean processSaved = moldProcessService.save(testProcess);
        assertTrue(processSaved, "测试娴佺▼应该淇濆瓨成功");
        assertNotNull(testProcess.getId(), "淇濆瓨鍚庣殑娴佺▼应该鏈塈D");

        // 4. 测试娴佺▼鐘舵€佽浆鎹細寰呭紑濮?-> 准备涓?
        boolean startResult = moldProcessService.startMoldProcess(testProcess.getId());
        assertTrue(startResult, "娴佺▼鍚姩应该成功");

        MoldProcess startedProcess = moldProcessService.getById(testProcess.getId());
        assertEquals(1, startedProcess.getCurrentStatus(), "娴佺▼鐘舵€佸簲璇ュ彉涓哄噯澶囦腑");

        // 5. 测试娴佺▼鐘舵€佽浆鎹細准备涓?-> 鍔犲伐涓?
        boolean processResult = moldProcessService.updateMoldProcessStatus(testProcess.getId(), 2);
        assertTrue(processResult, "流程状态更新为加工中应当成功");

        // 6. 创建璐ㄩ噺妫€楠岀粨鏋?
        InspectionResult testInspection = createTestInspectionResult(testProcess.getId());
        boolean inspectionSaved = inspectionResultService.save(testInspection);
        assertTrue(inspectionSaved, "测试检验结果应当保存成功");

        // 7. 测试娴佺▼鐘舵€佽浆鎹細鍔犲伐涓?-> 宸插畬鎴?
        boolean completeResult = moldProcessService.completeMoldProcess(testProcess.getId());
        assertTrue(completeResult, "娴佺▼瀹屾垚应该成功");

        MoldProcess completedProcess = moldProcessService.getById(testProcess.getId());
        assertEquals(4, completedProcess.getCurrentStatus(), "娴佺▼鐘舵€佸簲璇ュ彉涓哄凡瀹屾垚");

        // 8. 创建鐢熶骇记录
        ProductionRecord testRecord = createTestProductionRecord(testTask.getId(), testMold.getId());
        boolean recordSaved = productionRecordService.save(testRecord);
        assertTrue(recordSaved, "测试鐢熶骇记录应该淇濆瓨成功");

        System.out.println("妯″叿鍔犲伐娴佺▼瀹屾暣鐢熷懡鍛ㄦ湡测试閫氳繃锛氬垱寤?-> 鐘舵€佽浆鎹?-> 璐ㄩ噺妫€楠?-> 瀹屾垚");
    }

    /**
     * 测试娴佺▼缁熻功能
     */
    @Test
    void testProcessStatistics() {
        // 创建澶氫釜测试娴佺▼
        MoldProcess process1 = createTestMoldProcess(1L, 1L);
        process1.setCurrentStatus(0); // 寰呭紑濮?
        moldProcessService.save(process1);

        MoldProcess process2 = createTestMoldProcess(2L, 2L);
        process2.setCurrentStatus(1); // 准备涓?
        moldProcessService.save(process2);

        MoldProcess process3 = createTestMoldProcess(3L, 3L);
        process3.setCurrentStatus(2); // 鍔犲伐涓?
        moldProcessService.save(process3);

        MoldProcess process4 = createTestMoldProcess(4L, 4L);
        process4.setCurrentStatus(4); // 宸插畬鎴?
        moldProcessService.save(process4);

        // 获取娴佺▼缁熻淇℃伅
        MoldProcessService.MoldProcessStatistics statistics = moldProcessService.getMoldProcessStatistics();

        assertNotNull(statistics, "娴佺▼缁熻淇℃伅涓嶅簲璇ヤ负绌?);"
        assertTrue(statistics.getTotalProcesses() >= 4, "鎬绘祦绋嬫暟閲忓簲璇ヨ嚦灏戜负4");
        assertTrue(statistics.getPendingProcesses() >= 1, "寰呭紑濮嬫祦绋嬫暟閲忓簲璇ヨ嚦灏戜负1");
        assertTrue(statistics.getInProgressProcesses() >= 2, "杩涜涓祦绋嬫暟閲忓簲璇ヨ嚦灏戜负2");
        assertTrue(statistics.getCompletedProcesses() >= 1, "宸插畬鎴愭祦绋嬫暟閲忓簲璇ヨ嚦灏戜负1");

        System.out.println("娴佺▼缁熻功能测试閫氳繃");
    }

    /**
     * 测试璐ㄩ噺妫€楠岀粺璁″姛鑳?
     */
    @Test
    void testInspectionStatistics() {
        // 创建澶氫釜测试妫€楠岀粨鏋?
        InspectionResult inspection1 = createTestInspectionResult(1L);
        inspection1.setResult(1); // 鍚堟牸
        inspectionResultService.save(inspection1);

        InspectionResult inspection2 = createTestInspectionResult(2L);
        inspection2.setResult(0); // 涓嶅悎鏍?
        inspectionResultService.save(inspection2);

        InspectionResult inspection3 = createTestInspectionResult(3L);
        inspection3.setResult(1); // 鍚堟牸
        inspectionResultService.save(inspection3);

        // 获取妫€楠岀粺璁′俊鎭?
        InspectionResultService.InspectionStatistics statistics = inspectionResultService.getInspectionStatistics();

        assertNotNull(statistics, "妫€楠岀粺璁′俊鎭笉应该为空");
        assertTrue(statistics.getTotalInspections() >= 3, "鎬绘楠屾暟閲忓簲璇ヨ嚦灏戜负3");
        assertTrue(statistics.getPassedInspections() >= 2, "鍚堟牸妫€楠屾暟閲忓簲璇ヨ嚦灏戜负2");
        assertTrue(statistics.getFailedInspections() >= 1, "涓嶅悎鏍兼楠屾暟閲忓簲璇ヨ嚦灏戜负1");

        System.out.println("璐ㄩ噺妫€楠岀粺璁″姛鑳芥祴璇曢€氳繃");
    }

    /**
     * 测试娴佺▼开傚父澶勭悊
     */
    @Test
    void testProcessExceptionHandling() {
        // 创建测试娴佺▼
        MoldProcess testProcess = createTestMoldProcess(1L, 1L);
        testProcess.setCurrentStatus(2); // 鍔犲伐涓?
        moldProcessService.save(testProcess);

        // 测试娴佺▼鏆傚仠
        boolean pauseResult = moldProcessService.pauseMoldProcess(testProcess.getId());
        assertTrue(pauseResult, "娴佺▼鏆傚仠应该成功");

        MoldProcess pausedProcess = moldProcessService.getById(testProcess.getId());
        assertEquals(3, pausedProcess.getCurrentStatus(), "娴佺▼鐘舵€佸簲璇ュ彉涓烘殏鍋滀腑");

        // 测试娴佺▼鎭㈠
        boolean resumeResult = moldProcessService.resumeMoldProcess(testProcess.getId());
        assertTrue(resumeResult, "娴佺▼鎭㈠应该成功");

        MoldProcess resumedProcess = moldProcessService.getById(testProcess.getId());
        assertEquals(2, resumedProcess.getCurrentStatus(), "娴佺▼鐘舵€佸簲璇ユ仮澶嶄负鍔犲伐涓?);"

        // 测试娴佺▼鍙栨秷
        boolean cancelResult = moldProcessService.cancelMoldProcess(testProcess.getId());
        assertTrue(cancelResult, "娴佺▼鍙栨秷应该成功");

        MoldProcess cancelledProcess = moldProcessService.getById(testProcess.getId());
        assertEquals(5, cancelledProcess.getCurrentStatus(), "娴佺▼鐘舵€佸簲璇ュ彉涓哄凡鍙栨秷");

        System.out.println("娴佺▼开傚父澶勭悊测试閫氳繃锛氭殏鍋?-> 鎭㈠ -> 鍙栨秷");
    }

    /**
     * 创建测试妯″叿
     */
    private Mold createTestMold() {
        Mold mold = new Mold();
        mold.setMoldCode("TEST-MOLD-" + System.currentTimeMillis());
        mold.setMoldName("测试妯″叿");
        mold.setMoldTypeId(1L);
        mold.setMoldStatusId(1L);
        mold.setDesignLife(1000);
        mold.setCurrentUsageCount(0);
        mold.setResponsibleUserId(1L);
        mold.setStorageTime(LocalDateTime.now());
        mold.setEstimatedScrapTime(LocalDateTime.now().plusYears(1));
        mold.setRemark("测试妯″叿");
        return mold;
    }

    /**
     * 创建测试鐢熶骇浠诲姟
     */
    private ProductionTask createTestProductionTask(Long moldId) {
        ProductionTask task = new ProductionTask();
        task.setTaskCode("TEST-TASK-" + System.currentTimeMillis());
        task.setTaskName("测试鐢熶骇浠诲姟");
        task.setMoldId(moldId);
        task.setEquipmentId(1L);
        task.setOperatorId(1L);
        task.setPlannedQuantity(100);
        task.setActualQuantity(0);
        task.setQualifiedQuantity(0);
        task.setDefectiveQuantity(0);
        task.setStatus(0); // 鏈紑濮?
        task.setStartTime(LocalDateTime.now());
        return task;
    }

    /**
     * 创建测试妯″叿鍔犲伐娴佺▼
     */
    private MoldProcess createTestMoldProcess(Long moldId, Long productionTaskId) {
        MoldProcess process = new MoldProcess();
        process.setProcessCode("TEST-PROCESS-" + System.currentTimeMillis());
        process.setProcessName("测试鍔犲伐娴佺▼");
        process.setMoldId(moldId);
        process.setProductionTaskId(productionTaskId);
        process.setCurrentStatus(0); // 寰呭紑濮?
        process.setCurrentProgress(0);
        process.setPlannedStartTime(LocalDateTime.now());
        process.setPlannedEndTime(LocalDateTime.now().plusHours(2));
        process.setPriority(2); // 涓紭鍏堢骇
        process.setEstimatedDuration(120); // 2灏忔椂
        process.setResponsibleUserId(1L);
        process.setOperatorId(1L);
        process.setEquipmentId(1L);
        process.setDescription("测试鍔犲伐娴佺▼");
        process.setRemark("测试娴佺▼");
        return process;
    }

    /**
     * 创建测试璐ㄩ噺妫€楠岀粨鏋?
     */
    private InspectionResult createTestInspectionResult(Long processId) {
        InspectionResult inspection = new InspectionResult();
        inspection.setProcessId(processId);
        inspection.setInspectionItem("尺寸检验");
        inspection.setInspectionStandard("灏哄鍏樊卤0.1mm");
        inspection.setInspectionValue("10.05mm");
        inspection.setResult(1); // 鍚堟牸
        inspection.setInspectorId(1L);
        inspection.setInspectionTime(LocalDateTime.now());
        inspection.setInspectionRemark("测试检验");
        return inspection;
    }

    /**
     * 创建测试鐢熶骇记录
     */
    private ProductionRecord createTestProductionRecord(Long taskId, Long moldId) {
        ProductionRecord record = new ProductionRecord();
        record.setTaskId(taskId);
        record.setMoldId(moldId);
        record.setEquipmentId(1L);
        record.setOperatorId(1L);
        record.setProductionDate(LocalDateTime.now());
        record.setProductionQuantity(100);
        record.setQualifiedQuantity(95);
        record.setDefectiveQuantity(5);
        record.setMoldUsageCount(1);
        record.setEquipmentParameters("{}");
        record.setRemark("测试鐢熶骇记录");
        return record;
    }
}
