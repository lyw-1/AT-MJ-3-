package com.mold.digitalization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 妯″叿鍒濆鍙傛暟实体绫?
 * 瀵瑰簲数据搴撹〃锛歮old_parameter
 * 瀛樺偍妯″叿鐨勮缁嗘妧鏈弬鏁?
 */
@Data
@TableName("mold_parameter")
public class MoldParameter {

    /**
     * 鍙傛暟ID锛屼富閿紝鑷
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 妯″叿ID锛屽閿叧鑱攎old琛?
     */
    @TableField("mold_id")
    private Long moldId;

    /**
     * 妯″叿鏉愯川
     */
    @TableField("material")
    private String material;

    /**
     * 妯″叿纭害
     */
    @TableField("hardness")
    private String hardness;

    /**
     * 妯″叿灏哄锛堥暱锛屽崟浣嶏細mm锛?
     */
    @TableField("length")
    private Double length;

    /**
     * 妯″叿灏哄锛堝锛屽崟浣嶏細mm锛?
     */
    @TableField("width")
    private Double width;

    /**
     * 妯″叿灏哄锛堥珮锛屽崟浣嶏細mm锛?
     */
    @TableField("height")
    private Double height;

    /**
     * 妯″叿閲嶉噺锛堝崟浣嶏細kg锛?
     */
    @TableField("weight")
    private Double weight;

    /**
     * 鍨嬭厰鏁伴噺
     */
    @TableField("cavity_count")
    private Integer cavityCount;

    /**
     * 娉ㄥ鍘嬪姏锛堝崟浣嶏細bar锛?
     */
    @TableField("injection_pressure")
    private Double injectionPressure;

    /**
     * 璁捐娓╁害锛堝崟浣嶏細鈩冿級
     */
    @TableField("design_temperature")
    private Double designTemperature;

    /**
     * 閫傜敤鏈哄瀷
     */
    @TableField("applicable_machine")
    private String applicableMachine;

    /**
     * 鍐峰嵈鏂瑰紡
     */
    @TableField("cooling_method")
    private String coolingMethod;

    /**
     * 椤跺嚭鏂瑰紡
     */
    @TableField("ejection_method")
    private String ejectionMethod;

    /**
     * CAD鍥剧焊缂栧彿
     */
    @TableField("cad_drawing_number")
    private String cadDrawingNumber;

    /**
     * 鍙傛暟璇存槑
     */
    @TableField("description")
    private String description;

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

    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
