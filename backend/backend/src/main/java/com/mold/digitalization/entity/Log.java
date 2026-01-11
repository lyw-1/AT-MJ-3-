package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 鏃ュ織实体绫? * 瀵瑰簲数据搴撹〃锛歭og
 */
@Data
@TableName("log")
public class Log {

    /**
     * 鏃ュ織ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID锛屽閿叧鑱攗ser琛紝鍙负绌?     */
    private Long userId;

    /**
     * 操作绫诲瀷锛?-鐧诲綍锛?-鐧诲嚭锛?-查询锛?-鏂板锛?-淇敼锛?-删除锛?-瀵煎叆锛?-瀵煎嚭
     */
    private Integer operationType;

    /**
     * 操作妯″潡
     */
    private String module;

    /**
     * 操作鎻忚堪
     */
    private String description;

    /**
     * 璇锋眰鍙傛暟
     */
    private String requestParams;

    /**
     * 操作结果锛?-失败锛?-成功
     */
    private Integer result;

    /**
     * 错误淇℃伅
     */
    private String errorMessage;

    /**
     * 瀹㈡埛绔疘P鍦板潃
     */
    private String clientIp;

    /**
     * 操作鏃堕棿
     */
    private LocalDateTime operationTime;

    /**
     * 创建鏃堕棿
     */
    private LocalDateTime createTime;
}