package com.mold.digitalization.entity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 妯″叿鍊熺敤记录璇锋眰DTO
 * 鐢ㄤ簬妯″叿鍊熺敤操作鐨勮姹傚弬鏁?
 */
@Data
public class MoldBorrowReturnRequestDTO {

    /**
     * 妯″叿ID锛屽閿叧鑱攎old琛?
     */
    @NotNull(message = "Mold ID must not be null")
    private Long moldId;

    /**
     * 妯″叿缂栧彿锛堝啑浣欏瓧娈碉紝鏂逛究查询锛?
     */
    @NotBlank(message = "Mold code must not be blank")
    @Size(max = 50, message = "Mold code must not exceed 50 characters")
    private String moldCode;

    /**
     * 鍊熺敤閮ㄩ棬ID锛屽閿叧鑱攄epartment琛紙濡傛灉瀛樺湪锛?
     */
    private Long departmentId;

    /**
     * 鍊熺敤浜篒D锛屽閿叧鑱攗ser琛?
     */
    @NotNull(message = "Borrower ID must not be null")
    private Long borrowerId;

    /**
     * 鍊熺敤浜哄鍚嶏紙鍐椾綑瀛楁锛?
     */
    @NotBlank(message = "Borrower name must not be blank")
    @Size(max = 50, message = "Borrower name must not exceed 50 characters")
    private String borrowerName;

    /**
     * 鍊熺敤日期
     */
    @NotNull(message = "Borrow date must not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime borrowDate;

    /**
     * 棰勮褰掕繕日期
     */
    @NotNull(message = "Expected return date must not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedReturnDate;

    /**
     * 鍊熺敤鐢ㄩ€?
     */
    @NotBlank(message = "Purpose must not be blank")
    @Size(max = 200, message = "Purpose must not exceed 200 characters")
    private String purpose;

    /**
     * 澶囨敞淇℃伅
     */
    @Size(max = 500, message = "Remark must not exceed 500 characters")
    private String remark;
}
