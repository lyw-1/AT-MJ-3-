package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 妯″叿鍏ュ簱记录实体绫?
 * 瀵瑰簲数据搴撹〃锛歮old_storage_record
 * 记录妯″叿鍏ュ簱鐨勮缁嗕俊鎭?
 */
@Data
@TableName("mold_storage_record")
public class MoldStorageRecord {

    /**
     * 记录ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 妯″叿ID锛屽閿叧鑱攎old琛?
     */
    @TableField("mold_id")
    private Long moldId;

    /**
     * 妯″叿缂栧彿锛堝啑浣欏瓧娈碉紝鏂逛究查询锛?
     */
    @TableField("mold_code")
    private String moldCode;

    /**
     * 鍏ュ簱日期
     */
    @TableField("storage_date")
    private LocalDateTime storageDate;

    /**
     * 鍏ュ簱鎵规鍙?
     */
    @TableField("batch_number")
    private String batchNumber;

    /**
     * 鍏ュ簱鏁伴噺
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 瀛樻斁浣嶇疆
     */
    @TableField("location")
    private String location;

    /**
     * 鍏ュ簱操作鍛業D锛屽閿叧鑱攗ser琛?
     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 璐ㄦ结果锛?-寰呰川妫€锛?-鍚堟牸锛?-涓嶅悎鏍?
     */
    @TableField("inspection_result")
    private Integer inspectionResult;

    /**
     * 澶囨敞淇℃伅
     */
    @TableField("remark")
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

    public void setId(Long id) { this.id = id; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public void setInspectionResult(Integer inspectionResult) { this.inspectionResult = inspectionResult; }
}
