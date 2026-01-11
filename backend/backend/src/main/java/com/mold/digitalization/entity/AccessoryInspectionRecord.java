package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 閰嶄欢璐ㄦ记录实体绫? * 瀵瑰簲数据搴撹〃锛歛ccessory_inspection_record
 * 记录閰嶄欢璐ㄩ噺妫€楠岀殑璇︾粏淇℃伅
 */
@Data
@TableName("accessory_inspection_record")
public class AccessoryInspectionRecord {

    /**
     * 记录ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 閰嶄欢ID锛屽閿叧鑱攎old_accessory琛?     */
    @TableField("accessory_id")
    private Long accessoryId;

    /**
     * 閰嶄欢缂栧彿锛堝啑浣欏瓧娈碉紝鏂逛究查询锛?     */
    @TableField("accessory_code")
    private String accessoryCode;

    /**
     * 妫€楠屾壒娆″彿
     */
    @TableField("batch_number")
    private String batchNumber;

    /**
     * 妫€楠岄」鐩悕绉?     */
    @TableField("inspection_item")
    private String inspectionItem;

    /**
     * 妫€楠屾爣鍑嗗€?     */
    @TableField("standard_value")
    private String standardValue;

    /**
     * 妫€楠屽疄闄呭€?     */
    @TableField("actual_value")
    private String actualValue;

    /**
     * 妫€楠岀粨鏋滐細0-寰呮楠岋紝1-鍚堟牸锛?-涓嶅悎鏍?     */
    @TableField("inspection_result")
    private Integer inspectionResult;

    /**
     * 妫€楠屾棩鏈?     */
    @TableField("inspection_date")
    private LocalDateTime inspectionDate;

    /**
     * 妫€楠屽憳ID锛屽閿叧鑱攗ser琛?     */
    @TableField("inspector_id")
    private Long inspectorId;

    /**
     * 妫€楠屽娉?     */
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

    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public LocalDateTime getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDateTime inspectionDate) { this.inspectionDate = inspectionDate; }
    public Long getId() { return id; }
    public Integer getInspectionResult() { return inspectionResult; }
    public void setInspectionResult(Integer inspectionResult) { this.inspectionResult = inspectionResult; }
    public void setId(Long id) { this.id = id; }
}
