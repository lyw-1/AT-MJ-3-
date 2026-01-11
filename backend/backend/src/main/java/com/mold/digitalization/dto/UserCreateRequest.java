package com.mold.digitalization.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户创建请求 DTO
 */
@Data
public class UserCreateRequest {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 64, message = "密码长度应在6-64之间")
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 手机号 */
    private String phone;

    /** 部门 */
    private String department;

    /** 邮箱 */
    private String email;

    /** 状态（例如 1 启用，0 停用） */
    private Integer status;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRealName() { return realName; }
    public String getPhone() { return phone; }
    public Integer getStatus() { return status; }
}
