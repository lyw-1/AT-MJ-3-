package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.Accessory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 閰嶄欢Mapper接口
 * 缁ф壙BaseMapper骞舵彁渚涜嚜瀹氫箟查询方法
 */
public interface AccessoryMapper extends BaseMapper<Accessory> {

    /**
     * 鏍规嵁閰嶄欢缂栧彿查询閰嶄欢
     * @param accessoryCode 閰嶄欢缂栧彿
     * @return 閰嶄欢淇℃伅
     */
    Accessory selectByAccessoryCode(@Param("accessoryCode") String accessoryCode);

    /**
     * 鏍规嵁閰嶄欢绫诲瀷ID查询閰嶄欢鍒楄〃
     * @param accessoryTypeId 閰嶄欢绫诲瀷ID
     * @return 閰嶄欢鍒楄〃
     */
    List<Accessory> selectByTypeId(@Param("accessoryTypeId") Long accessoryTypeId);

    /**
     * 鏍规嵁閰嶄欢鐘舵€佹煡璇㈤厤浠跺垪琛?
     * @param status 閰嶄欢鐘舵€侊細0-姝ｅ父锛?-鍋滅敤锛?-缂鸿揣
     * @return 閰嶄欢鍒楄〃
     */
    List<Accessory> selectByStatus(@Param("status") Integer status);

    /**
     * 查询搴撳瓨浣庝簬闃堝€肩殑閰嶄欢
     * @param threshold 搴撳瓨闃堝€?
     * @return 搴撳瓨涓嶈冻鐨勯厤浠跺垪琛?
     */
    List<Accessory> selectLowStock(@Param("threshold") Integer threshold);

    /**
     * 鏍规嵁鏉愯川查询閰嶄欢鍒楄〃
     * @param material 閰嶄欢鏉愯川
     * @return 閰嶄欢鍒楄〃
     */
    List<Accessory> selectByMaterial(@Param("material") String material);

    /**
     * 更新閰嶄欢搴撳瓨鏁伴噺
     * @param id 閰嶄欢ID
     * @param quantity 璋冩暣鏁伴噺锛堟鏁板鍔狅紝璐熸暟鍑忓皯锛?
     * @return 更新结果
     */
    int updateStockQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);
}
