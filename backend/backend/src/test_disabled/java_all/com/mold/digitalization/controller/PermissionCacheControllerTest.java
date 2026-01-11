package com.mold.digitalization.controller;

import com.mold.digitalization.service.PermissionCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PermissionCacheControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PermissionCacheService permissionCacheService;

    @InjectMocks
    private PermissionCacheController permissionCacheController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(permissionCacheController)
                .setControllerAdvice(new com.mold.digitalization.handler.GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .setMessageConverters(
                        new StringHttpMessageConverter(StandardCharsets.UTF_8),
                        new MappingJackson2HttpMessageConverter()
                )
                .build();
    }

    @Test
    void testRefreshUserPermissions_Success() throws Exception {
        mockMvc.perform(post("/api/v1/admin/permissions/cache/refresh/user/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User permission cache refreshed"));

        verify(permissionCacheService, times(1)).refreshUserPermissions(1L);
    }

    @Test
    void testRefreshUserPermissions_InvalidUserId() throws Exception {
        mockMvc.perform(post("/api/v1/admin/permissions/cache/refresh/user/{userId}", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(permissionCacheService, never()).refreshUserPermissions(anyLong());
    }

    @Test
    void testRefreshAllUsersPermissions_Success() throws Exception {
        mockMvc.perform(post("/api/v1/admin/permissions/cache/refresh/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("All users' permission caches refreshed; background job started"));

        verify(permissionCacheService, times(1)).refreshAllUsersPermissions();
    }

    @Test
    void testRefreshUsersByRoleId_Success() throws Exception {
        mockMvc.perform(post("/api/v1/admin/permissions/cache/refresh/role/{roleId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("瑙掕壊鍏宠仈用户鏉冮檺缂撳瓨鍒锋柊成功"));

        verify(permissionCacheService, times(1)).refreshUsersByRoleId(1L);
    }

    @Test
    void testRefreshUsersByRoleId_InvalidRoleId() throws Exception {
        mockMvc.perform(post("/api/v1/admin/permissions/cache/refresh/role/{roleId}", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(permissionCacheService, never()).refreshUsersByRoleId(anyLong());
    }

    @Test
    void testClearUserPermissions_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/permissions/cache/clear/user/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User permission cache cleared"));

        verify(permissionCacheService, times(1)).clearUserPermissions(1L);
    }

    @Test
    void testClearUserPermissions_InvalidUserId() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/permissions/cache/clear/user/{userId}", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(permissionCacheService, never()).clearUserPermissions(anyLong());
    }

    @Test
    void testClearAllUsersPermissions_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/permissions/cache/clear/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("鎵€鏈夌敤鎴锋潈闄愮紦瀛樺凡娓呴櫎"));

        verify(permissionCacheService, times(1)).clearAllUsersPermissions();
    }
}
