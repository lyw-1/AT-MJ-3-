package com.mold.digitalization.entity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 妯″叿淇℃伅璇锋眰DTO
 * 鐢ㄤ簬妯″叿鐩稿叧操作鐨勮姹傚弬鏁?
 */
@Data
public class MoldRequestDTO {

    /**
     * 妯″叿缂栧彿锛屽敮涓€
     */
    @NotBlank(message = "moldCode must not be blank")
    @Size(max = 50, message = "moldCode max length is 50")
    private String moldCode;

    /**
     * 妯″叿鍚嶇О
     */
    @NotBlank(message = "moldName must not be blank")
    @Size(max = 100, message = "moldName max length is 100")
    private String moldName;

    /**
     * 妯″叿绫诲瀷ID锛屽閿叧鑱攎old_type琛?
     */
    @NotNull(message = "moldTypeId must not be null")
    private Long moldTypeId;

    /**
     * 妯″叿鐘舵€両D锛屽閿叧鑱攎old_status琛?
     */
    @NotNull(message = "moldStatusId must not be null")
    private Long moldStatusId;

    /**
     * 璁捐瀵垮懡锛堜娇鐢ㄦ鏁帮級
     */
    @Min(value = 0, message = "designLife must be >= 0")
    private Integer designLife;

    /**
     * 璐熻矗浜篒D锛屽閿叧鑱攗ser琛?
     */
    @NotNull(message = "responsibleUserId must not be null")
    private Long responsibleUserId;

    /**
     * 鍏ュ簱鏃堕棿
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime storageTime;

    /**
     * 棰勮鎶ュ簾鏃堕棿
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedScrapTime;

    /**
     * 澶囨敞淇℃伅
     */
    @Size(max = 500, message = "remark max length is 500")
    private String remark;

    /**
     * 妯″叿鏉愯川
     */
    @Size(max = 50, message = "material max length is 50")
    private String material;

    /**
     * 妯″叿灏哄锛堥暱锛屽崟浣嶏細mm锛?
     */
    @Min(value = 0, message = "length must be >= 0")
    private Double length;

    /**
     * 妯″叿灏哄锛堝锛屽崟浣嶏細mm锛?
     */
    @Min(value = 0, message = "width must be >= 0")
    private Double width;

    /**
     * 妯″叿灏哄锛堥珮锛屽崟浣嶏細mm锛?
     */
    @Min(value = 0, message = "height must be >= 0")
    private Double height;

    /**
     * 妯″叿閲嶉噺锛堝崟浣嶏細kg锛?
     */
    @Min(value = 0, message = "weight must be >= 0")
    private Double weight;

    /**
     * 瀛樻斁浣嶇疆
     */
    @Size(max = 100, message = "location max length is 100")
    private String location;

    /**
     * 鐗堟湰鍙?
     */
    @Size(max = 50, message = "version max length is 50")
    private String version;

    /**
     * 璁捐閮ㄩ棬
     */
    @Size(max = 100, message = "designDepartment max length is 100")
    private String designDepartment;

    /**
     * 鐢熶骇鍘傚晢
     */
    @Size(max = 100, message = "manufacturer max length is 100")
    private String manufacturer;

    /**
     * 閲囪喘日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;
}
