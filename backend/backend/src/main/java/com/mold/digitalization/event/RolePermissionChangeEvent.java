package com.mold.digitalization.event;

import java.util.List;

/**
 * 瑙掕壊鏉冮檺鍙樻洿浜嬩欢
 * 褰撹鑹茬殑鏉冮檺鍒嗛厤鍙戠敓鍙樺寲鏃惰Е鍙?
 */
public class RolePermissionChangeEvent extends PermissionChangeEvent {

    private static final long serialVersionUID = 1L;
    
    /** 瑙掕壊ID */
    private Long roleId;
    
    /** 鏉冮檺ID鍒楄〃 */
    private List<Long> permissionIds;
    
    /**
     * 创建涓€涓柊鐨勮鑹叉潈闄愬彉鏇翠簨浠?
     * @param source 浜嬩欢婧?
     * @param roleId 瑙掕壊ID
     * @param permissionIds 鏉冮檺ID鍒楄〃
     */
    public RolePermissionChangeEvent(Object source, Long roleId, List<Long> permissionIds) {
        super(source);
        this.roleId = roleId;
        this.permissionIds = permissionIds;
    }
    
    /**
     * 获取瑙掕壊ID
     * @return 瑙掕壊ID
     */
    public Long getRoleId() {
        return roleId;
    }
    
    /**
     * 设置瑙掕壊ID
     * @param roleId 瑙掕壊ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    /**
     * 获取鏉冮檺ID鍒楄〃
     * @return 鏉冮檺ID鍒楄〃
     */
    public List<Long> getPermissionIds() {
        return permissionIds;
    }
    
    /**
     * 设置鏉冮檺ID鍒楄〃
     * @param permissionIds 鏉冮檺ID鍒楄〃
     */
    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
    
    @Override
    public String toString() {
        return "RolePermissionChangeEvent{" +
                "roleId=" + roleId +
                ", permissionIds=" + permissionIds +
                '}';
    }
}
