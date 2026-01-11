package com.mold.digitalization.entity.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 閰嶄欢淇℃伅鍝嶅簲DTO
 * 鐢ㄤ簬灏佽API返回鐨勯厤浠朵俊鎭暟鎹? */
@Data
public class AccessoryResponseDTO {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 閰嶄欢缂栧彿锛屽敮涓€
     */
    private String accessoryCode;

    /**
     * 閰嶄欢鍚嶇О
     */
    private String accessoryName;

    /**
     * 閰嶄欢绫诲瀷ID
     */
    private Long accessoryTypeId;

    /**
     * 閰嶄欢绫诲瀷鍚嶇О
     */
    private String accessoryTypeName;

    /**
     * 閰嶄欢鏉愯川
     */
    private String material;

    /**
     * 閰嶄欢瑙勬牸
     */
    private String specification;

    /**
     * 璁￠噺鍗曚綅
     */
    private String unit;

    /**
     * 褰撳墠搴撳瓨鏁伴噺
     */
    private Integer stockQuantity;

    /**
     * 鏈€浣庡簱瀛橀槇鍊?     */
    private Integer minimumStock;

    /**
     * 瀛樻斁浣嶇疆
     */
    private String location;

    /**
     * 閰嶄欢鐘舵€侊細0-姝ｅ父锛?-鍋滅敤锛?-缂鸿揣
     */
    private Integer status;

    /**
     * 鐘舵€佹樉绀烘枃鏈?     */
    private String statusText;

    /**
     * 澶囨敞淇℃伅
     */
    private String remark;

    /**
     * 创建鑰?     */
    private Long createdBy;

    /**
     * 创建鏃堕棿
     */
    private LocalDateTime createdAt;

    /**
     * 更新鑰?     */
    private Long updatedBy;

    /**
     * 更新鏃堕棿
     */
    private LocalDateTime updatedAt;
}
