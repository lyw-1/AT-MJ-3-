package com.mold.digitalization.service.state.impl;

import com.mold.digitalization.entity.MoldProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 鐘舵€佽浆鎹㈠疄鐜扮被鍗曞厓测试
 *
 * @author System
 * @since 2024-01-01
 */
class StateTransitionImplTest {

    private StateTransitionImpl transition;
    private MoldProcess testProcess;

    @BeforeEach
    void setUp() {
        // 创建用于测试的状态转换
        transition = new StateTransitionImpl("PENDING", "PREPARING", "准备中");

        // 创建测试鐢ㄧ殑娴佺▼瀵硅薄
        testProcess = new MoldProcess();
        testProcess.setId(1L);
        testProcess.setMoldId("MOLD001");
        testProcess.setCurrentStatus("PENDING");
    }

    @Test
    void testGetFromStatus() {
        // 执行测试
        String fromStatus = transition.getFromStatus();

        // 验证结果
        assertEquals("PENDING", fromStatus);
    }

    @Test
    void testGetToStatus() {
        // 执行测试
        String toStatus = transition.getToStatus();

        // 验证结果
        assertEquals("PREPARING", toStatus);
    }

    @Test
    void testGetDescription() {
        // 执行测试
        String description = transition.getDescription();

        // 断言
        assertEquals("准备中", description);
    }

    @Test
    void testCanTransition() {
        // 执行测试
        boolean canTransition = transition.canTransition(testProcess);

        // 验证结果
        assertTrue(canTransition);
    }

    @Test
    void testCanTransitionWrongStatus() {
        // 设置流程状态为不匹配的状态
        testProcess.setCurrentStatus("COMPLETED");

        // 执行测试
        boolean canTransition = transition.canTransition(testProcess);

        // 验证结果
        assertFalse(canTransition);
    }

    @Test
    void testCanTransitionNullProcess() {
        // 执行测试
        boolean canTransition = transition.canTransition(null);

        // 验证结果
        assertFalse(canTransition);
    }

    @Test
    void testCanTransitionNullCurrentStatus() {
        // 设置娴佺▼鐘舵€佷负null
        testProcess.setCurrentStatus(null);

        // 执行测试
        boolean canTransition = transition.canTransition(testProcess);

        // 验证结果
        assertFalse(canTransition);
    }

    @Test
    void testToString() {
        // 执行测试
        String result = transition.toString();

        // 验证结果
        assertEquals("StateTransition{from='PENDING', to='PREPARING', description='准备中'}", result);
    }

    @Test
    void testEquals() {
        // 创建相同的状态转换
        StateTransitionImpl sameTransition = new StateTransitionImpl("PENDING", "PREPARING", "准备中");

        // 执行测试
        boolean result = transition.equals(sameTransition);

        // 验证结果
        assertTrue(result);
    }

    @Test
    void testEqualsDifferent() {
        // 创建不同的状态转换
        StateTransitionImpl differentTransition = new StateTransitionImpl("PREPARING", "IN_PROGRESS", "进行中");

        // 执行测试
        boolean result = transition.equals(differentTransition);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testEqualsNull() {
        // 执行测试
        boolean result = transition.equals(null);

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testHashCode() {
        // 创建相同的状态转换
        StateTransitionImpl sameTransition = new StateTransitionImpl("PENDING", "PREPARING", "准备中");

        // 执行测试
        int hashCode1 = transition.hashCode();
        int hashCode2 = sameTransition.hashCode();

        // 验证结果
        assertEquals(hashCode1, hashCode2);
    }
}
