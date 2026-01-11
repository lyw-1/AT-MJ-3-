package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 閰嶄欢鍔犲伐记录实体绫? * 瀵瑰簲数据搴撹〃锛歛ccessory_process_record
 * 记录妯″叿閰嶄欢鐨勫姞宸ヨ繃绋嬪拰结果
 */
@Data
@TableName("accessory_process_record")
public class AccessoryProcessRecord {

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
     * 鍏宠仈妯″叿ID锛屽閿叧鑱攎old琛?     */
    @TableField("mold_id")
    private Long moldId;

    /**
     * 鍔犲伐浠诲姟ID锛屽閿叧鑱攑roduction_task琛?     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 鍔犲伐宸ュ簭鍚嶇О
     */
    @TableField("process_name")
    private String processName;

    /**
     * 鍔犲伐璁惧ID锛屽閿叧鑱攅quipment琛?     */
    @TableField("equipment_id")
    private Long equipmentId;

    /**
     * 鍔犲伐操作鍛業D锛屽閿叧鑱攗ser琛?     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 鍔犲伐开€濮嬫椂闂?     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 鍔犲伐缁撴潫鏃堕棿
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 鍔犲伐鍙傛暟锛圝SON鏍煎紡瀛樺偍璇︾粏鍙傛暟锛?     */
    @TableField("process_parameters")
    private String processParameters;

    /**
     * 鍔犲伐结果锛?-鏈畬鎴愶紝1-鍚堟牸锛?-涓嶅悎鏍?     */
    @TableField("process_result")
    private Integer processResult;

    /**
     * 妫€楠屽憳ID
     */
    @TableField("inspector_id")
    private Long inspectorId;

    /**
     * 妫€楠屾椂闂?     */
    @TableField("inspection_time")
    private LocalDateTime inspectionTime;

    /**
     * 闂鎻忚堪锛堝鏋滄湁锛?     */
    @TableField("issue_description")
    private String issueDescription;

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
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setProcessResult(Integer processResult) { this.processResult = processResult; }
    public Integer getProcessResult() { return processResult; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
