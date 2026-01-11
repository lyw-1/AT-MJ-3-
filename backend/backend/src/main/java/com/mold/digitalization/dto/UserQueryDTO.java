package com.mold.digitalization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户查询数据浼犺緭瀵硅薄
 */
@Data
@ApiModel("用户查询鏉′欢DTO")
public class UserQueryDTO {
    
    @ApiModelProperty(value = "椤电爜", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "姣忛〉澶у皬", example = "10")
    private Integer pageSize = 10;
    
    @ApiModelProperty(value = "Username")
    private String username;
    
    @ApiModelProperty(value = "鐪熷疄濮撳悕")
    private String realName;
    
    @ApiModelProperty(value = "用户鐘舵€侊細0-绂佺敤锛?-鍚敤")
    private Integer status;
    
    @ApiModelProperty(value = "鎺掑簭瀛楁")
    private String orderBy;
    
    @ApiModelProperty(value = "鎺掑簭鏂瑰悜锛歛sc-鍗囧簭锛宒esc-闄嶅簭")
    private String orderDirection;
    
    // 鏄惧紡添加getter方法
    public Integer getPageNum() {
        return pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public Integer getStatus() {
        return status;
    }
}
