package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 妲藉记录实体绫?
 * 瀵瑰簲数据搴撹〃锛歴lot_width_record
 * 记录妯″叿妲藉鐨勬祴閲忔暟鎹?
 */
@Data
@TableName("slot_width_record")
public class SlotWidthRecord {

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
     * 妲戒綅缂栧彿
     */
    @TableField("slot_number")
    private String slotNumber;

    /**
     * 璁捐妲藉锛堝崟浣嶏細mm锛?
     */
    @TableField("design_width")
    private Double designWidth;

    /**
     * 实际娴嬮噺妲藉锛堝崟浣嶏細mm锛?
     */
    @TableField("actual_width")
    private Double actualWidth;

    /**
     * 娴嬮噺鏃堕棿
     */
    @TableField("measurement_time")
    private LocalDateTime measurementTime;

    /**
     * 娴嬮噺浜哄憳ID锛屽閿叧鑱攗ser琛?
     */
    @TableField("measurer_id")
    private Long measurerId;

    /**
     * 娴嬮噺宸ュ叿
     */
    @TableField("measuring_tool")
    private String measuringTool;

    /**
     * 娴嬮噺环境娓╁害锛堝崟浣嶏細鈩冿級
     */
    @TableField("ambient_temperature")
    private Double ambientTemperature;

    /**
     * 鍋忓樊鑼冨洿锛堝崟浣嶏細mm锛?
     */
    @TableField("tolerance")
    private Double tolerance;

    /**
     * 鏄惁鍚堟牸锛?-涓嶅悎鏍硷紝1-鍚堟牸
     */
    @TableField("is_qualified")
    private Integer isQualified;
    
    /**
     * 鐘舵€侊細0-寰呭鐞嗭紝1-宸插鐞?
     */
    @TableField("status")
    private Integer status;

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

    public LocalDateTime getMeasurementTime() { return measurementTime; }
    public void setMeasurementTime(LocalDateTime measurementTime) { this.measurementTime = measurementTime; }
    public Double getTolerance() { return tolerance; }
    public Double getActualWidth() { return actualWidth; }
    public Double getDesignWidth() { return designWidth; }
    public void setIsQualified(Integer isQualified) { this.isQualified = isQualified; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public void setStatus(Integer status) { this.status = status; }
    public void setId(Long id) { this.id = id; }
}
