package com.mold.digitalization.test;

import com.mold.digitalization.event.UserRoleChangeEvent;
import com.mold.digitalization.event.RolePermissionChangeEvent;
import com.mold.digitalization.event.RoleStatusChangeEvent;
import com.mold.digitalization.service.PermissionCacheService;
import com.mold.digitalization.listener.PermissionChangeListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * 鏉冮檺缂撳瓨鍒锋柊鏈哄埗鍗曞厓测试
 */
@ExtendWith(MockitoExtension.class)
public class PermissionCacheRefreshTest {

    @Mock
    private PermissionCacheService permissionCacheService;

    @InjectMocks
    private PermissionChangeListener permissionChangeListener;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    /**
     * 测试用户瑙掕壊鍙樻洿浜嬩欢鐩戝惉
     */
    @Test
    public void testUserRoleChangeEvent() {
        // 准备测试数据
        Long userId = 1L;
        Long roleId = 2L;
        String operationType = "ASSIGN";
        Object source = new Object();
        
        // 创建浜嬩欢
        UserRoleChangeEvent event = new UserRoleChangeEvent(source, userId, roleId, operationType);
        
        // 璋冪敤鐩戝惉鍣ㄦ柟娉?
        permissionChangeListener.handleUserRoleChangeEvent(event);
        
        // 验证缂撳瓨服务方法琚皟鐢?
        verify(permissionCacheService, times(1)).refreshUserPermissions(userId);
    }

    /**
     * 测试瑙掕壊鏉冮檺鍙樻洿浜嬩欢鐩戝惉
     */
    @Test
    public void testRolePermissionChangeEvent() {
        // 准备测试数据
        Long roleId = 2L;
        Object source = new Object();
        
        // 创建浜嬩欢
        RolePermissionChangeEvent event = new RolePermissionChangeEvent(source, roleId, Arrays.asList(1L, 2L, 3L));
        
        // 璋冪敤鐩戝惉鍣ㄦ柟娉?
        permissionChangeListener.handleRolePermissionChangeEvent(event);
        
        // 验证缂撳瓨服务方法琚皟鐢?
        verify(permissionCacheService, times(1)).refreshUsersByRoleId(roleId);
    }

    /**
     * 测试瑙掕壊鐘舵€佸彉鏇翠簨浠剁洃鍚?
     */
    @Test
    public void testRoleStatusChangeEvent() {
        // 准备测试数据
        Long roleId = 2L;
        Integer oldStatus = 1;
        Integer newStatus = 0;
        Object source = new Object();
        
        // 创建浜嬩欢
        RoleStatusChangeEvent event = new RoleStatusChangeEvent(source, roleId, oldStatus, newStatus);
        
        // 璋冪敤鐩戝惉鍣ㄦ柟娉?
        permissionChangeListener.handleRoleStatusChangeEvent(event);
        
        // 验证缂撳瓨服务方法琚皟鐢?
        verify(permissionCacheService, times(1)).refreshUsersByRoleId(roleId);
    }

    /**
     * 测试绌烘潈闄愬垪琛ㄧ殑瑙掕壊鏉冮檺鍙樻洿浜嬩欢
     */
    @Test
    public void testRolePermissionChangeEventWithEmptyList() {
        // 准备测试数据
        Long roleId = 2L;
        Object source = new Object();
        
        // 创建浜嬩欢
        RolePermissionChangeEvent event = new RolePermissionChangeEvent(source, roleId, Collections.emptyList());
        
        // 璋冪敤鐩戝惉鍣ㄦ柟娉?
        permissionChangeListener.handleRolePermissionChangeEvent(event);
        
        // 验证缂撳瓨服务方法琚皟鐢?
        verify(permissionCacheService, times(1)).refreshUsersByRoleId(roleId);
    }
}