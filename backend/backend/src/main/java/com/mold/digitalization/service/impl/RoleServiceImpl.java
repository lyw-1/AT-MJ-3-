package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.RoleMapper;
import com.mold.digitalization.mapper.UserRoleMapper;
import com.mold.digitalization.entity.Permission;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.event.RoleStatusChangeEvent;
import com.mold.digitalization.service.PermissionService;
import com.mold.digitalization.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 瑙掕壊服务实现绫?
 * 实现瑙掕壊鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private PermissionService permissionService;
    
    @Override
    @Cacheable(value = "role", key = "#roleCode")
    public Role getRoleByCode(String roleCode) {
        return roleMapper.selectByRoleCode(roleCode);
    }
    
    @Override
    @Cacheable(value = "role", key = "'userRoles:' + #userId")
    public List<Role> getRolesByUserId(Long userId) {
        return userRoleMapper.selectRolesByUserId(userId);
    }
    
    @Override
    @CacheEvict(value = "role", allEntries = true)
    public boolean createRole(Role role) {
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        return roleMapper.insertRole(role) == 1;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "role", allEntries = true)
    public boolean updateRole(Role role) {
        if (role.getId() == null) return false;
        UpdateWrapper<Role> uw = new UpdateWrapper<>();
        uw.eq("id", role.getId());
        if (role.getRoleName() != null) uw.set("role_name", role.getRoleName());
        if (role.getRoleCode() != null) uw.set("role_code", role.getRoleCode());
        if (role.getDescription() != null) uw.set("description", role.getDescription());
        uw.set("update_time", LocalDateTime.now());
        int rows = roleMapper.update(null, uw);
        return rows > 0;
    }
    
    @Override
    @CacheEvict(value = "role", allEntries = true)
    public boolean deleteRole(Long roleId) {
        return removeById(roleId);
    }
    
    @Override
    @Cacheable(value = "role", key = "'allRoles'")
    public List<Role> getAllRoles() {
        return list();
    }

    @Override
    @Cacheable(value = "role", key = "#id")
    public Role getRoleById(Long id) {
        return getById(id);
    }

    @Override
    public Map<String, Object> getRoleList(int page, int pageSize, String keyword) {
        Page<Role> pageParam = new Page<>(page, pageSize);
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        
        com.baomidou.mybatisplus.core.metadata.IPage<Role> rolePage = roleMapper.selectPage(pageParam, params);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", rolePage.getTotal());
        result.put("list", rolePage.getRecords());
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("pages", rolePage.getPages());
        
        return result;
    }

    @Transactional
    @Override
    @CacheEvict(value = "role", allEntries = true)
    public boolean updateRoleStatus(Long roleId, Integer status) {
    // 获取瑙掕壊褰撳墠状态
    Role currentRole = getById(roleId);
        if (currentRole == null) {
            return false;
        }
        
        Integer oldStatus = currentRole.getStatus();
        
    // 更新瑙掕壊状态
    currentRole.setStatus(status);
        boolean result = updateById(currentRole);
        
        // 鍙戝竷瑙掕壊鐘舵€佸彉鏇翠簨浠?
        if (result && !status.equals(oldStatus)) {
            eventPublisher.publishEvent(new RoleStatusChangeEvent(this, roleId, oldStatus, status));
        }
        
        return result;
    }

    @Override
    public boolean isRoleCodeExists(String roleCode, Long excludeId) {
        Role role = roleMapper.selectByRoleCode(roleCode);
        if (role == null) {
            return false;
        }
    // 濡傛灉鎺掗櫎ID涓嶄负绌轰笖涓庢煡璇㈠埌鐨勮鑹睮D鐩稿悓锛屽垯璁や负涓嶅瓨鍦ㄩ噸澶?
    return excludeId == null || !role.getId().equals(excludeId);
    }
    
    @Override
    @Cacheable(value = "role", key = "'permissions:' + #roleId")
    public List<Permission> getRolePermissions(Long roleId) {
        return permissionService.getPermissionsByRoleId(roleId);
    }
    
    @Override
    @CacheEvict(value = "role", allEntries = true)
    public boolean assignRolePermissions(Long roleId, List<Long> permissionIds) {
        return permissionService.assignPermissionsToRole(roleId, permissionIds);
    }
    
    @Override
    @CacheEvict(value = "role", allEntries = true)
    public boolean removeRolePermissions(Long roleId, List<Long> permissionIds) {
        // 先获取角色当前的权限
        List<Permission> currentPermissions = permissionService.getPermissionsByRoleId(roleId);
        if (currentPermissions.isEmpty()) {
            return true;
        }
        
        // 计算需要保留的权限ID
        List<Long> currentPermissionIds = currentPermissions.stream()
                .map(Permission::getId)
                .toList();
        
        // 从当前权限中移除要删除的权限
        List<Long> newPermissionIds = currentPermissionIds.stream()
                .filter(id -> !permissionIds.contains(id))
                .toList();
        
        // 重新分配权限
        return permissionService.assignPermissionsToRole(roleId, newPermissionIds);
    }
    
    @Override
    @CacheEvict(value = "role", allEntries = true)
    public boolean clearRolePermissions(Long roleId) {
        return permissionService.assignPermissionsToRole(roleId, List.of());
    }
    
    @Override
    @Cacheable(value = "role", key = "'hasPermission:' + #roleId + ':' + #permissionCode")
    public boolean hasPermission(Long roleId, String permissionCode) {
        // 获取角色的所有权限
        List<Permission> permissions = permissionService.getPermissionsByRoleId(roleId);
        // 检查是否包含指定权限
        return permissions.stream()
                .anyMatch(permission -> permission.getCode().equals(permissionCode));
    }
}
