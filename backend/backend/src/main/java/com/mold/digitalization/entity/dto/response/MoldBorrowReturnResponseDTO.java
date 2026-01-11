package com.mold.digitalization.entity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 妯″叿鍊熺敤褰掕繕记录鍝嶅簲DTO
 * 鐢ㄤ簬灏佽API返回鐨勬ā鍏峰€熺敤褰掕繕记录淇℃伅数据
 */
@Data
public class MoldBorrowReturnResponseDTO {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 妯″叿ID
     */
    private Long moldId;

    /**
     * 妯″叿缂栧彿
     */
    private String moldCode;

    /**
     * 妯″叿鍚嶇О
     */
    private String moldName;

    /**
     * 鍊熺敤閮ㄩ棬ID
     */
    private Long departmentId;

    /**
     * 鍊熺敤閮ㄩ棬鍚嶇О
     */
    private String departmentName;

    /**
     * 鍊熺敤浜篒D
     */
    private Long borrowerId;

    /**
     * 鍊熺敤浜哄鍚?     */
    private String borrowerName;

    /**
     * 鍊熺敤日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowDate;

    /**
     * 棰勮褰掕繕日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedReturnDate;

    /**
     * 实际褰掕繕日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualReturnDate;

    /**
     * 鍊熺敤鐢ㄩ€?     */
    private String purpose;

    /**
     * 褰掕繕鏃剁姸鎬佹弿杩?     */
    private String returnCondition;

    /**
     * 鍊熺敤鐘舵€侊細0-鍊熺敤涓紝1-宸插綊杩?     */
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
