package com.mold.digitalization.event;

/**
 * 瑙掕壊鐘舵€佸彉鏇翠簨浠?
 * 褰撹鑹茬姸鎬佸彂鐢熷彉鍖栨椂瑙﹀彂
 */
public class RoleStatusChangeEvent extends PermissionChangeEvent {

    private static final long serialVersionUID = 1L;
    
    /** 瑙掕壊ID */
    private Long roleId;
    
    /** 鍘熺姸鎬?*/
    private Integer oldStatus;
    
    /** 鏂扮姸鎬?*/
    private Integer newStatus;
    
    /**
     * 创建涓€涓柊鐨勮鑹茬姸鎬佸彉鏇翠簨浠?
     * @param source 浜嬩欢婧?
     * @param roleId 瑙掕壊ID
     * @param oldStatus 鍘熺姸鎬?
     * @param newStatus 鏂扮姸鎬?
     */
    public RoleStatusChangeEvent(Object source, Long roleId, Integer oldStatus, Integer newStatus) {
        super(source);
        this.roleId = roleId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
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
     * 获取鍘熺姸鎬?
     * @return 鍘熺姸鎬?
     */
    public Integer getOldStatus() {
        return oldStatus;
    }
    
    /**
     * 设置鍘熺姸鎬?
     * @param oldStatus 鍘熺姸鎬?
     */
    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }
    
    /**
     * 获取鏂扮姸鎬?
     * @return 鏂扮姸鎬?
     */
    public Integer getNewStatus() {
        return newStatus;
    }
    
    /**
     * 设置鏂扮姸鎬?
     * @param newStatus 鏂扮姸鎬?
     */
    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }
    
    @Override
    public String toString() {
        return "RoleStatusChangeEvent{" +
                "roleId=" + roleId +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                '}';
    }
}
