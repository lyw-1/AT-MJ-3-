package com.mold.digitalization.entity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 閰嶄欢淇℃伅璇锋眰DTO
 * 鐢ㄤ簬閰嶄欢鐩稿叧操作鐨勮姹傚弬鏁? */
@Data
public class AccessoryRequestDTO {

    /**
     * 閰嶄欢缂栧彿锛屽敮涓€
     */
    @NotBlank(message = "accessoryCode must not be blank")
    @Size(max = 50, message = "accessoryCode max length is 50")
    private String accessoryCode;

    /**
     * 閰嶄欢鍚嶇О
     */
    @NotBlank(message = "accessoryName must not be blank")
    @Size(max = 100, message = "accessoryName max length is 100")
    private String accessoryName;

    /**
     * 閰嶄欢绫诲瀷ID锛屽閿叧鑱攁ccessory_type琛?     */
    @NotNull(message = "accessoryTypeId must not be null")
    private Long accessoryTypeId;

    /**
     * 閰嶄欢鏉愯川
     */
    @Size(max = 50, message = "material max length is 50")
    private String material;

    /**
     * 閰嶄欢瑙勬牸
     */
    @Size(max = 100, message = "specification max length is 100")
    private String specification;

    /**
     * 璁￠噺鍗曚綅
     */
    @NotBlank(message = "unit must not be blank")
    @Size(max = 20, message = "unit max length is 20")
    private String unit;

    /**
     * 褰撳墠搴撳瓨鏁伴噺
     */
    @Min(value = 0, message = "stockQuantity must be >= 0")
    private Integer stockQuantity;

    /**
     * 鏈€浣庡簱瀛橀槇鍊?     */
    @Min(value = 0, message = "minimumStock must be >= 0")
    private Integer minimumStock;

    /**
     * 瀛樻斁浣嶇疆
     */
    @Size(max = 100, message = "location max length is 100")
    private String location;

    /**
     * 閰嶄欢鐘舵€侊細0-姝ｅ父锛?-鍋滅敤锛?-缂鸿揣
     */
    @Min(value = 0, message = "status must be >= 0")
    @Max(value = 2, message = "status must be <= 2")
    private Integer status;

    /**
     * 澶囨敞淇℃伅
     */
    @Size(max = 500, message = "remark max length is 500")
    private String remark;
}
