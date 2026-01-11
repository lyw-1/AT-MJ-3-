package com.mold.digitalization.entity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 妯″叿褰掕繕璇锋眰DTO
 * 鐢ㄤ簬妯″叿褰掕繕操作鐨勮姹傚弬鏁?
 */
@Data
public class MoldReturnRequestDTO {

    /**
     * 记录ID锛屼富閿?
     */
    @NotNull(message = "记录ID涓嶈兘为空")
    private Long id;

    /**
     * 实际褰掕繕日期
     */
    @NotNull(message = "实际褰掕繕日期涓嶈兘为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualReturnDate;

    /**
     * 褰掕繕鏃剁姸鎬佹弿杩?
     */
    @Size(max = 500, message = "Return condition must not exceed 500 characters")
    private String returnCondition;

    /**
     * 澶囨敞淇℃伅
     */
    @Size(max = 500, message = "Remark must not exceed 500 characters")
    private String remark;
}
