package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 璁惧实体绫?
 * 瀵瑰簲数据搴撹〃锛歟quipment
 */
@Getter
@Setter
@TableName("equipment")
public class Equipment {

    /**
     * 璁惧ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 璁惧缂栧彿锛屽敮涓€
     */
    private String equipmentCode;

    /**
     * 璁惧鍚嶇О
     */
    private String equipmentName;

    /**
     * 璁惧绫诲瀷ID锛屽閿叧鑱攅quipment_type琛?
     */
    private Long equipmentTypeId;

    /**
     * 璁惧鐘舵€侊細0-鍋滅敤锛?-杩愯涓紝2-缁存姢涓?
     */
    private Integer status;

    /**
     * 鎵€鍦ㄤ綅缃?
     */
    private String location;

    /**
     * 璐熻矗浜篒D锛屽閿叧鑱攗ser琛?
     */
    private Long responsibleUserId;

    /**
     * 璐叆鏃堕棿
     */
    private LocalDateTime purchaseTime;

    /**
     * 澶囨敞淇℃伅
     */
    private String remark;

    /**
     * 创建鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 更新鏃堕棿
     */
    private LocalDateTime updateTime;
    
    // 鎵嬪姩添加setter方法浠ョ‘淇濈紪璇戦€氳繃
    public void setId(Long id) {
        this.id = id;
    }
}
