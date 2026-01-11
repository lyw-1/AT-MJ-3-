package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.Permission;
import com.mold.digitalization.entity.Role;
import java.util.List;
import java.util.Map;

/**
 * 瑙掕壊服务接口
 * 瀹氫箟瑙掕壊鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
public interface RoleService extends IService<Role> {
    
    /**
     * 鏍规嵁瑙掕壊ID获取瑙掕壊淇℃伅
     * @param id 瑙掕壊ID
     * @return 瑙掕壊淇℃伅
     */
    Role getRoleById(Long id);
    
    /**
     * 鏍规嵁瑙掕壊浠ｇ爜查询瑙掕壊淇℃伅
     * @param roleCode 瑙掕壊浠ｇ爜
     * @return 瑙掕壊淇℃伅
     */
    Role getRoleByCode(String roleCode);
    
    /**
     * 鏍规嵁用户ID查询用户鐨勮鑹插垪琛?     * @param userId 用户ID
     * @return 瑙掕壊鍒楄〃
     */
    List<Role> getRolesByUserId(Long userId);
    
    /**
     * 鍒嗛〉查询瑙掕壊鍒楄〃
     * @param page 褰撳墠椤电爜
     * @param pageSize 姣忛〉鏉℃暟
     * @param keyword 鎼滅储鍏抽敭瀛?     * @return 鍒嗛〉结果
     */
    Map<String, Object> getRoleList(int page, int pageSize, String keyword);
    
    /**
     * 创建鏂拌鑹?     * @param role 瑙掕壊淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createRole(Role role);
    
    /**
     * 更新瑙掕壊淇℃伅
     * @param role 瑙掕壊淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateRole(Role role);
    
    /**
     * 删除瑙掕壊
     * @param roleId 瑙掕壊ID
     * @return 鏄惁删除成功
     */
    boolean deleteRole(Long roleId);
    
    /**
     * 淇敼瑙掕壊状态
     * @param roleId 瑙掕壊ID
     * @param status 瑙掕壊状态
     * @return 鏄惁淇敼成功
     */
    boolean updateRoleStatus(Long roleId, Integer status);
    
    /**
     * 获取鎵€鏈夎鑹插垪琛?     * @return 瑙掕壊鍒楄〃
     */
    List<Role> getAllRoles();
    
    /**
     * 妫€鏌ヨ鑹蹭唬鐮佹槸鍚﹀凡瀛樺湪
     * @param roleCode 瑙掕壊浠ｇ爜
     * @param excludeId 鎺掗櫎鐨勮鑹睮D锛堢敤浜庣紪杈戞椂锛?     * @return 鏄惁瀛樺湪
     */
    boolean isRoleCodeExists(String roleCode, Long excludeId);
    
    /**
     * 获取角色的权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getRolePermissions(Long roleId);
    
    /**
     * 分配角色权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 是否分配成功
     */
    boolean assignRolePermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 移除角色权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 是否移除成功
     */
    boolean removeRolePermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 清空角色权限
     * @param roleId 角色ID
     * @return 是否清空成功
     */
    boolean clearRolePermissions(Long roleId);
    
    /**
     * 检查角色是否拥有指定权限
     * @param roleId 角色ID
     * @param permissionCode 权限编码
     * @return 是否拥有权限
     */
    boolean hasPermission(Long roleId, String permissionCode);
}
