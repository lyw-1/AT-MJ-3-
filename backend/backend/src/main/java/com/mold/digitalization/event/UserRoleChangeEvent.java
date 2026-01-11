package com.mold.digitalization.event;

/**
 * 用户瑙掕壊鍙樻洿浜嬩欢
 * 褰撶敤鎴风殑瑙掕壊鍒嗛厤鍙戠敓鍙樺寲鏃惰Е鍙? */
public class UserRoleChangeEvent extends PermissionChangeEvent {

    private static final long serialVersionUID = 1L;
    
    /** 用户ID */
    private Long userId;
    
    /** 瑙掕壊ID */
    private Long roleId;
    
    /** 操作绫诲瀷锛欰SSIGN锛堝垎閰嶏級鎴朢EMOVE锛堢Щ闄わ級 */
    private String operationType;
    
    /**
     * 创建涓€涓柊鐨勭敤鎴疯鑹插彉鏇翠簨浠?     * @param source 浜嬩欢婧?     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @param operationType 操作绫诲瀷
     */
    public UserRoleChangeEvent(Object source, Long userId, Long roleId, String operationType) {
        super(source);
        this.userId = userId;
        this.roleId = roleId;
        this.operationType = operationType;
    }
    
    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 设置用户ID
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * 获取操作绫诲瀷
     * @return 操作绫诲瀷
     */
    public String getOperationType() {
        return operationType;
    }
    
    /**
     * 设置操作绫诲瀷
     * @param operationType 操作绫诲瀷
     */
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
    @Override
    public String toString() {
        return "UserRoleChangeEvent{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", operationType='" + operationType + '\'' +
                '}';
    }
}