package com.mold.digitalization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 用户数据浼犺緭瀵硅薄
 */
@Data
@ApiModel("用户淇℃伅DTO")
public class UserDTO {
    
    @ApiModelProperty(value = "用户ID")
    private Long id;
    
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters long")
    @ApiModelProperty(value = "Username", required = true)
    private String username;
    
    @ApiModelProperty(value = "密码")
    private String password;
    
    @ApiModelProperty(value = "鐪熷疄濮撳悕")
    private String realName;
    
    @ApiModelProperty(value = "鑱旂郴鐢佃瘽")
    private String phone;
    
    // email瀛楁宸茬Щ闄わ紝涓嶅啀需要
    // @ApiModelProperty(value = "鐢靛瓙閭")
    // private String email;
    
    @ApiModelProperty(value = "用户鐘舵€侊細0-绂佺敤锛?-鍚敤")
    private Integer status;
    
    @ApiModelProperty(value = "澶囨敞淇℃伅")
    private String remark;
    
    @ApiModelProperty(value = "瑙掕壊ID鍒楄〃")
    private List<Long> roleIds;
    
    // 鏄惧紡瀹氫箟getter方法锛岀‘淇滾ombok姝ｇ‘鐢熸垚
    public Long getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public List<Long> getRoleIds() {
        return roleIds;
    }
}
