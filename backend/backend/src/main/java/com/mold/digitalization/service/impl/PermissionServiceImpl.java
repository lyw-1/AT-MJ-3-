package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.Permission;
import com.mold.digitalization.event.RolePermissionChangeEvent;
import com.mold.digitalization.mapper.PermissionMapper;
import com.mold.digitalization.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 鏉冮檺服务实现绫?*/
@Service
@Transactional
@Slf4j
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<Permission> getPermissionsByUserId(Long userId) {
        return permissionMapper.selectByUserId(userId);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectByRoleId(roleId);
    }

    @Override
    public List<Permission> getPermissionTree() {
        return permissionMapper.selectPermissionTree();
    }

    @Override
    public boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        try {
            // remove existing role-permission relationships first
            permissionMapper.deleteRolePermissions(roleId);

            // if permissionIds is not empty then insert new relationships
            if (!CollectionUtils.isEmpty(permissionIds)) {
                permissionMapper.insertRolePermissions(roleId, permissionIds);
                logger.info("Successfully assigned {} permissions to role {}", permissionIds.size(), roleId);
            } else {
                logger.info("No permissions provided for role {}", roleId);
            }

            // publish role-permission change event
            eventPublisher.publishEvent(new RolePermissionChangeEvent(this, roleId, permissionIds));

            return true;
        } catch (Exception e) {
            logger.error("Failed to assign permissions to role {}", roleId, e);
            throw e;
        }
    }

    @Override
    public Permission getPermissionByCode(String code) {
        return permissionMapper.selectByCode(code);
    }

    @Override
    public List<Permission> getAllEnabledPermissions() {
        return permissionMapper.selectAllEnabled();
    }
}
