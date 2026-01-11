package com.mold.digitalization.dto;

import lombok.Data;

/**
 * 用户响应 DTO
 */
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private String departmentName;
    private Integer status;
    private Boolean locked; // 只读状态字段，如系统存在
    private java.util.List<String> roles;

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setRealName(String realName) { this.realName = realName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public void setStatus(Integer status) { this.status = status; }
    public void setLocked(Boolean locked) { this.locked = locked; }
    public void setRoles(java.util.List<String> roles) { this.roles = roles; }

    public Integer getStatus() { return status; }
}
