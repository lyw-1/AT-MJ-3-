package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 璐ㄩ噺妫€楠岀粨鏋滃疄浣撶被
 * 瀵瑰簲数据搴撹〃锛歩nspection_result
 */
@Data
@TableName("inspection_result")
public class InspectionResult {

    /**
     * 妫€楠岀粨鏋淚D锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 娴佺▼ID锛屽閿叧鑱攎old_process琛?
     */
    @TableField("process_id")
    private Long processId;

    /**
     * 妫€楠岄」鐩悕绉?
     */
    @TableField("inspection_item")
    private String inspectionItem;

    /**
     * 妫€楠屾爣鍑?
     */
    @TableField("inspection_standard")
    private String inspectionStandard;

    /**
     * 妫€楠屽€?
     */
    @TableField("inspection_value")
    private String inspectionValue;

    /**
     * 妫€楠岀粨鏋滐細0-涓嶅悎鏍硷紝1-鍚堟牸
     */
    @TableField("inspection_result")
    private Integer result;

    /**
     * 妫€楠屼汉鍛業D锛屽閿叧鑱攗ser琛?
     */
    @TableField("inspector_id")
    private Long inspectorId;

    /**
     * 妫€楠屾椂闂?
     */
    @TableField("inspection_time")
    private LocalDateTime inspectionTime;

    /**
     * 妫€楠屽娉?
     */
    @TableField("inspection_remark")
    private String inspectionRemark;

    /**
     * 涓嶅悎鏍煎師鍥狅紙濡傛灉涓嶅悎鏍硷級
     */
    @TableField("defect_reason")
    private String defectReason;

    /**
     * 澶勭悊寤鸿
     */
    @TableField("handling_suggestion")
    private String handlingSuggestion;

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

    /**
     * 设置妫€楠岀粨鏋淚D
     * @param id 妫€楠岀粨鏋淚D
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 设置娴佺▼ID
     * @param processId 娴佺▼ID
     */
    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    /**
     * 设置妫€楠岀粨鏋?
     * @param result 妫€楠岀粨鏋?
     */
    public void setResult(Integer result) {
        this.result = result;
    }
    public void setInspectorId(Long inspectorId) { this.inspectorId = inspectorId; }
    public void setInspectionTime(LocalDateTime inspectionTime) { this.inspectionTime = inspectionTime; }
}
