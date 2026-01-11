package com.mold.digitalization.entity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 妯″叿淇℃伅鍝嶅簲DTO
 * 鐢ㄤ簬灏佽API返回鐨勬ā鍏蜂俊鎭暟鎹? */
@Data
public class MoldResponseDTO {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 妯″叿缂栧彿锛屽敮涓€
     */
    private String moldCode;

    /**
     * 妯″叿鍚嶇О
     */
    private String moldName;

    /**
     * 妯″叿绫诲瀷ID
     */
    private Long moldTypeId;

    /**
     * 妯″叿绫诲瀷鍚嶇О
     */
    private String moldTypeName;

    /**
     * 妯″叿鐘舵€両D
     */
    private Long moldStatusId;

    /**
     * 妯″叿鐘舵€佸悕绉?     */
    private String moldStatusName;

    /**
     * 璁捐瀵垮懡锛堜娇鐢ㄦ鏁帮級
     */
    private Integer designLife;

    /**
     * 宸蹭娇鐢ㄦ鏁?     */
    private Integer usedCount;

    /**
     * 璐熻矗浜篒D
     */
    private Long responsibleUserId;

    /**
     * 璐熻矗浜哄鍚?     */
    private String responsibleUserName;

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
    private String remark;

    /**
     * 妯″叿鏉愯川
     */
    private String material;

    /**
     * 妯″叿灏哄锛堥暱锛屽崟浣嶏細mm锛?     */
    private Double length;

    /**
     * 妯″叿灏哄锛堝锛屽崟浣嶏細mm锛?     */
    private Double width;

    /**
     * 妯″叿灏哄锛堥珮锛屽崟浣嶏細mm锛?     */
    private Double height;

    /**
     * 妯″叿閲嶉噺锛堝崟浣嶏細kg锛?     */
    private Double weight;

    /**
     * 瀛樻斁浣嶇疆
     */
    private String location;

    /**
     * 鐗堟湰鍙?     */
    private String version;

    /**
     * 璁捐閮ㄩ棬
     */
    private String designDepartment;

    /**
     * 鐢熶骇鍘傚晢
     */
    private String manufacturer;

    /**
     * 閲囪喘日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    /**
     * 创建鑰?     */
    private Long createdBy;

    /**
     * 创建鏃堕棿
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新鑰?     */
    private Long updatedBy;

    /**
     * 更新鏃堕棿
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
