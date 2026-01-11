package com.mold.digitalization.entity.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

/**
 * 用户淇℃伅璇锋眰DTO
 * 鐢ㄤ簬用户绠＄悊操作鐨勮姹傚弬鏁?
 */
@Data
public class UserRequestDTO {

    /**
     * Username / login name
     */
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username length must be between 3 and 50")
    private String username;

    /**
     * Password
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /**
     * User display name
     */
    @NotBlank(message = "Real name must not be blank")
    @Size(max = 50, message = "Real name must not exceed 50 characters")
    private String realName;

    /**
     * User type: 0 - normal user, 1 - admin
     */
    @Min(value = 0, message = "Invalid user type")
    @Max(value = 1, message = "Invalid user type")
    private Integer userType;

    /**
     * Account status: 0 - enabled, 1 - disabled
     */
    @Min(value = 0, message = "Invalid status")
    @Max(value = 1, message = "Invalid status")
    private Integer status;

    /**
     * Mobile phone number
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid phone number format")
    private String phone;

    // email瀛楁宸茬Щ闄わ紝涓嶅啀需要
    // /**
    //  * 鐢靛瓙閭
    //  */
    // @Email(message = "鐢靛瓙閭鏍煎紡涓嶆纭?)"
    // private String email;

    /**
     * 閮ㄩ棬ID锛屽閿叧鑱攄epartment琛紙濡傛灉瀛樺湪锛?
     */
    private Long departmentId;

    /**
     * Position / job title
     */
    @Size(max = 100, message = "Position must not exceed 100 characters")
    private String position;
}
