package com.mold.digitalization.entity.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户淇℃伅鍝嶅簲DTO
 * 鐢ㄤ簬灏佽API返回鐨勭敤鎴蜂俊鎭暟锟? */
@Data
public class UserResponseDTO {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 用户名嶏紝鐧诲綍璐﹀彿
     */
    private String username;

    /**
     * 用户鐪熷疄濮撳悕
     */
    private String realName;

    /**
     * 用户绫诲瀷锟?-鏅€氱敤鎴凤紝1-绠＄悊锟?     */
    private Integer userType;

    /**
     * 用户绫诲瀷鏄剧ず鏂囨湰
     */
    private String userTypeText;

    /**
     * 用户鐘舵€侊細0-鍚敤锟?-绂佺敤
     */
    private Integer status;

    /**
     * 用户鐘舵€佹樉绀烘枃锟?     */
    private String statusText;

    /**
     * 鎵嬫満鍙风爜
     */
    private String phone;

    // email瀛楁宸茬Щ闄わ紝涓嶅啀需要
    // /**
    //  * 鐢靛瓙閭
    //  */
    // private String email;

    /**
     * 閮ㄩ棬ID
     */
    private Long departmentId;

    /**
     * 閮ㄩ棬鍚嶇О
     */
    private String departmentName;

    /**
     * 鑱屼綅
     */
    private String position;

    /**
     * 创建锟?     */
    private Long createdBy;

    /**
     * 创建鏃堕棿
     */
    private LocalDateTime createdAt;

    /**
     * 更新锟?     */
    private Long updatedBy;

    /**
     * 更新鏃堕棿
     */
    private LocalDateTime updatedAt;
}
