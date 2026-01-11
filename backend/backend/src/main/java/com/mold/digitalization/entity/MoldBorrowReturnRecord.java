package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * 妯″叿棰嗚繕记录实体绫?
 * 瀵瑰簲数据搴撹〃锛歮old_borrow_return_record
 * 记录妯″叿鐨勫€熺敤鍜屽綊杩樻儏鍐?
 */
@Data
@TableName("mold_borrow_return_record")
public class MoldBorrowReturnRecord {

    /**
     * 记录ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 妯″叿ID锛屽閿叧鑱攎old琛?
     */
    @TableField("mold_id")
    @NotNull(message = "妯″叿ID涓嶈兘为空")
    private Long moldId;

    /**
     * 妯″叿缂栧彿锛堝啑浣欏瓧娈碉紝鏂逛究查询锛?
     */
    @TableField("mold_code")
    @NotBlank(message = "moldCode must not be blank")
    @Size(max = 50, message = "moldCode max length is 50")
    private String moldCode;

    /**
     * 鍊熺敤閮ㄩ棬ID锛屽閿叧鑱攄epartment琛紙濡傛灉瀛樺湪锛?
     */
    @TableField("department_id")
    private Long departmentId;

    /**
     * 鍊熺敤浜篒D锛屽閿叧鑱攗ser琛?
     */
    @TableField("borrower_id")
    @NotNull(message = "borrowerId must not be null")
    private Long borrowerId;

    /**
     * 鍊熺敤浜哄鍚嶏紙鍐椾綑瀛楁锛?
     */
    @TableField("borrower_name")
    @NotBlank(message = "borrowerName must not be blank")
    @Size(max = 50, message = "borrowerName max length is 50")
    private String borrowerName;

    /**
     * 鍊熺敤日期
     */
    @TableField("borrow_date")
    @NotNull(message = "borrowDate must not be null")
    private LocalDateTime borrowDate;

    /**
     * 棰勮褰掕繕日期
     */
    @TableField("expected_return_date")
    @NotNull(message = "expectedReturnDate must not be null")
    private LocalDateTime expectedReturnDate;

    /**
     * 实际褰掕繕日期
     */
    @TableField("actual_return_date")
    private LocalDateTime actualReturnDate;

    /**
     * 鍊熺敤鐢ㄩ€?     */
    @TableField("purpose")
    @NotBlank(message = "purpose must not be blank")
    @Size(max = 200, message = "purpose max length is 200")
    private String purpose;

    /**
     * 褰掕繕鐘舵€侊細0-鏈綊杩橈紝1-宸插綊杩橈紝2-閫炬湡
     */
    @TableField("return_status")
    @Min(value = 0, message = "returnStatus must be >= 0")
    @Max(value = 2, message = "returnStatus must be <= 2")
    private Integer returnStatus;

    /**
     * 褰掕繕鏃剁姸鎬佹弿杩?     */
    @TableField("return_condition")
    @Size(max = 500, message = "returnCondition max length is 500")
    private String returnCondition;

    /**
     * 澶囨敞淇℃伅
     */
    @TableField("remark")
    @Size(max = 500, message = "remark max length is 500")
    private String remark;

    /**
     * 创建鏃堕棿
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新鏃堕棿
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    public void setBorrowDate(LocalDateTime borrowDate) { this.borrowDate = borrowDate; }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { this.actualReturnDate = actualReturnDate; }
    public void setReturnStatus(Integer returnStatus) { this.returnStatus = returnStatus; }
    public void setId(Long id) { this.id = id; }
    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }
    public void setBorrowerId(Long borrowerId) { this.borrowerId = borrowerId; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setMoldId(Long moldId) { this.moldId = moldId; }
    public void setMoldCode(String moldCode) { this.moldCode = moldCode; }
}
