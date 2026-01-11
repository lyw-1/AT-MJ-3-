package com.mold.digitalization.service.impl;

import com.mold.digitalization.dao.UserRoleMapper;
import com.mold.digitalization.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceImplTest {

    @Mock
    private UserRoleMapper userRoleMapper;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    @Test
    public void testGetUserRolesByUserId() {
        // 准备测试数据
        Long userId = 1L;
        List<UserRole> expectedUserRoles = Arrays.asList(
                new UserRole() {{ setUserId(userId); setRoleId(1L); }},
                new UserRole() {{ setUserId(userId); setRoleId(2L); }}
        );

        // 模拟行为
        when(userRoleMapper.selectByUserId(userId)).thenReturn(expectedUserRoles);

        // 执行测试
        List<UserRole> actualUserRoles = userRoleService.getUserRolesByUserId(userId);

        // 验证结果
        assertNotNull(actualUserRoles);
        assertEquals(expectedUserRoles.size(), actualUserRoles.size());
        verify(userRoleMapper, times(1)).selectByUserId(userId);
    }

    @Test
    public void testGetUserRolesByUserId_NullUserId() {
        // 执行测试 - 浼犲叆null用户ID
        List<UserRole> actualUserRoles = userRoleService.getUserRolesByUserId(null);

        // 验证结果
        assertNotNull(actualUserRoles);
        assertTrue(actualUserRoles.isEmpty());
        verify(userRoleMapper, times(0)).selectByUserId(anyLong());
    }

    @Test
    public void testGetUserRolesByRoleId() {
        // 准备测试数据
        Long roleId = 1L;
        List<UserRole> expectedUserRoles = Arrays.asList(
                new UserRole() {{ setUserId(1L); setRoleId(roleId); }},
                new UserRole() {{ setUserId(2L); setRoleId(roleId); }}
        );

        // 模拟行为
        when(userRoleMapper.selectByRoleId(roleId)).thenReturn(expectedUserRoles);

        // 执行测试
        List<UserRole> actualUserRoles = userRoleService.getUserRolesByRoleId(roleId);

        // 验证结果
        assertNotNull(actualUserRoles);
        assertEquals(expectedUserRoles.size(), actualUserRoles.size());
        verify(userRoleMapper, times(1)).selectByRoleId(roleId);
    }

    @Test
    public void testGetUserRolesByRoleId_NullRoleId() {
        // 执行测试 - 浼犲叆null瑙掕壊ID
        List<UserRole> actualUserRoles = userRoleService.getUserRolesByRoleId(null);

        // 验证结果
        assertNotNull(actualUserRoles);
        assertTrue(actualUserRoles.isEmpty());
        verify(userRoleMapper, times(0)).selectByRoleId(anyLong());
    }

    @Test
    public void testAssignRoleToUser_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long roleId = 2L;

        // 模拟行为 - 鍏宠仈涓嶅瓨鍦?        when(userRoleMapper.existsByUserIdAndRoleId(userId, roleId)).thenReturn(0);
        when(userRoleMapper.insert(any(UserRole.class))).thenReturn(1);

        // 执行测试
        boolean result = userRoleService.assignRoleToUser(userId, roleId);

        // 验证结果
        assertTrue(result);
        verify(userRoleMapper, times(1)).existsByUserIdAndRoleId(userId, roleId);
        verify(userRoleMapper, times(1)).insert(any(UserRole.class));
    }

    @Test
    public void testAssignRoleToUser_AlreadyExists() {
        // 准备测试数据
        Long userId = 1L;
        Long roleId = 2L;

        // 模拟行为 - 鍏宠仈宸插瓨鍦?        when(userRoleMapper.existsByUserIdAndRoleId(userId, roleId)).thenReturn(1);

        // 执行测试
        boolean result = userRoleService.assignRoleToUser(userId, roleId);

        // 验证结果
        assertTrue(result); // 宸插瓨鍦ㄤ篃绠楁垚鍔燂紝鍙槸涓嶉噸澶嶆彃鍏?        verify(userRoleMapper, times(1)).existsByUserIdAndRoleId(userId, roleId);
        verify(userRoleMapper, times(0)).insert(any(UserRole.class));
    }

    @Test
    public void testAssignRoleToUser_NullParams() {
        // 测试null用户ID
        boolean result1 = userRoleService.assignRoleToUser(null, 1L);
        assertFalse(result1);

        // 测试null瑙掕壊ID
        boolean result2 = userRoleService.assignRoleToUser(1L, null);
        assertFalse(result2);

        // 测试涓や釜閮戒负null
        boolean result3 = userRoleService.assignRoleToUser(null, null);
        assertFalse(result3);

        verify(userRoleMapper, times(0)).existsByUserIdAndRoleId(anyLong(), anyLong());
        verify(userRoleMapper, times(0)).insert(any(UserRole.class));
    }

    @Test
    public void testRemoveRoleFromUser_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long roleId = 2L;

        // 模拟行为
        when(userRoleMapper.deleteByUserIdAndRoleId(userId, roleId)).thenReturn(1);

        // 执行测试
        boolean result = userRoleService.removeRoleFromUser(userId, roleId);

        // 验证结果
        assertTrue(result);
        verify(userRoleMapper, times(1)).deleteByUserIdAndRoleId(userId, roleId);
    }

    @Test
    public void testRemoveRoleFromUser_NotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long roleId = 999L; // 涓嶅瓨鍦ㄧ殑瑙掕壊ID

        // 模拟行为
        when(userRoleMapper.deleteByUserIdAndRoleId(userId, roleId)).thenReturn(0);

        // 执行测试
        boolean result = userRoleService.removeRoleFromUser(userId, roleId);

        // 验证结果
        assertFalse(result);
        verify(userRoleMapper, times(1)).deleteByUserIdAndRoleId(userId, roleId);
    }

    @Test
    public void testRemoveRoleFromUser_NullParams() {
        // 测试null用户ID
        boolean result1 = userRoleService.removeRoleFromUser(null, 1L);
        assertFalse(result1);

        // 测试null瑙掕壊ID
        boolean result2 = userRoleService.removeRoleFromUser(1L, null);
        assertFalse(result2);

        // 测试涓や釜閮戒负null
        boolean result3 = userRoleService.removeRoleFromUser(null, null);
        assertFalse(result3);

        verify(userRoleMapper, times(0)).deleteByUserIdAndRoleId(anyLong(), anyLong());
    }

    @Test
    public void testBatchAssignRolesToUser_Success() {
        // 准备测试数据
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);

        // 模拟行为 - 鍏堝垹闄ゆ墍鏈夋棫鐨勫叧鑱?        when(userRoleMapper.deleteByUserId(userId)).thenReturn(3);
        // 模拟鎻掑叆琛屼负
        when(userRoleMapper.batchInsert(anyList())).thenReturn(3);

        // 执行测试
        boolean result = userRoleService.batchAssignRolesToUser(userId, roleIds);

        // 验证结果
        assertTrue(result);
        verify(userRoleMapper, times(1)).deleteByUserId(userId);
        verify(userRoleMapper, times(1)).batchInsert(anyList());
    }

    @Test
    public void testBatchAssignRolesToUser_EmptyRoleIds() {
        // 准备测试数据
        Long userId = 1L;
        List<Long> roleIds = Collections.emptyList();

        // 模拟行为 - 删除鎵€鏈夋棫鐨勫叧鑱?        when(userRoleMapper.deleteByUserId(userId)).thenReturn(2);

        // 执行测试
        boolean result = userRoleService.batchAssignRolesToUser(userId, roleIds);

        // 验证结果
        assertTrue(result);
        verify(userRoleMapper, times(1)).deleteByUserId(userId);
        verify(userRoleMapper, times(0)).batchInsert(anyList()); // 绌哄垪琛ㄤ笉鎻掑叆
    }

    @Test
    public void testBatchAssignRolesToUser_NullParams() {
        // 测试null用户ID
        boolean result1 = userRoleService.batchAssignRolesToUser(null, Arrays.asList(1L, 2L));
        assertFalse(result1);

        // 测试null瑙掕壊ID鍒楄〃
        boolean result2 = userRoleService.batchAssignRolesToUser(1L, null);
        assertFalse(result2);

        // 测试涓や釜閮戒负null
        boolean result3 = userRoleService.batchAssignRolesToUser(null, null);
        assertFalse(result3);

        verify(userRoleMapper, times(0)).deleteByUserId(anyLong());
        verify(userRoleMapper, times(0)).batchInsert(anyList());
    }

    @Test
    public void testBatchAssignRolesToUser_InsertFailed() {
        // 准备测试数据
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);

        // 模拟行为 - 删除成功浣嗘彃鍏ュけ璐?        when(userRoleMapper.deleteByUserId(userId)).thenReturn(2);
        when(userRoleMapper.batchInsert(anyList())).thenReturn(1); // 鍙彃鍏ヤ簡1鏉?
        // 执行测试
        boolean result = userRoleService.batchAssignRolesToUser(userId, roleIds);

        // 验证结果
        assertFalse(result);
        verify(userRoleMapper, times(1)).deleteByUserId(userId);
        verify(userRoleMapper, times(1)).batchInsert(anyList());
    }
}