package com.mold.digitalization.dto;

import lombok.Data;

/**
 * 用户更新请求 DTO
 */
@Data
public class UserUpdateRequest {
    private String username;

    /** 真实姓名 */
    private String realName;

    /** 手机号 */
    private String phone;

    /** 状态（例如 1 启用，0 停用） */
    private Integer status;

    /** 部门名称 */
    private String departmentName;

    /** 邮箱 */
    private String email;

    public String getUsername() { return username; }
    public Integer getStatus() { return status; }
    public String getRealName() { return realName; }
    public String getPhone() { return phone; }
    public String getDepartmentName() { return departmentName; }
    public String getEmail() { return email; }
}
