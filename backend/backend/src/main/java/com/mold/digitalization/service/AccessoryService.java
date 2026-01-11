package com.mold.digitalization.service;

import com.mold.digitalization.entity.Accessory;
import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢服务接口
 * 鎻愪緵閰嶄欢鐩稿叧鐨勪笟鍔￠€昏緫操作
 */
public interface AccessoryService {

    /**
     * 鏍规嵁ID获取閰嶄欢淇℃伅
     * @param id 閰嶄欢ID
     * @return 閰嶄欢淇℃伅
     */
    Accessory getById(Long id);

    /**
     * 鏍规嵁閰嶄欢缂栧彿获取閰嶄欢淇℃伅
     * @param accessoryCode 閰嶄欢缂栧彿
     * @return 閰嶄欢淇℃伅
     */
    Accessory getByAccessoryCode(String accessoryCode);

    /**
     * 鏍规嵁閰嶄欢绫诲瀷ID获取閰嶄欢鍒楄〃
     * @param accessoryTypeId 閰嶄欢绫诲瀷ID
     * @return 閰嶄欢鍒楄〃
     */
    List<Accessory> getByAccessoryTypeId(Long accessoryTypeId);

    /**
     * 获取鎵€鏈夐厤浠朵俊鎭?     * @return 閰嶄欢鍒楄〃
     */
    List<Accessory> getAllAccessories();

    /**
     * 鏍规嵁鐘舵€佽幏鍙栭厤浠跺垪琛?     * @param status 閰嶄欢状态     * @return 閰嶄欢鍒楄〃
     */
    List<Accessory> getByStatus(Integer status);

    /**
     * 搴撳瓨棰勮閰嶄欢鍒楄〃锛堜綆浜庢渶浣庡簱瀛橀槇鍊硷級
     * @return 搴撳瓨棰勮閰嶄欢鍒楄〃
     */
    List<Accessory> getLowStockAccessories();

    /**
     * 创建鏂伴厤浠?
     * @param accessory 閰嶄欢淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createAccessory(Accessory accessory);

    /**
     * 更新閰嶄欢淇℃伅
     * @param accessory 閰嶄欢淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateAccessory(Accessory accessory);

    /**
     * 更新閰嶄欢搴撳瓨鏁伴噺
     * @param id 閰嶄欢ID
     * @param quantity 璋冩暣鏁伴噺锛堟鏁板鍔狅紝璐熸暟鍑忓皯锛?     * @return 鏄惁更新成功
     */
    boolean updateStockQuantity(Long id, Integer quantity);

    /**
     * 更新閰嶄欢状态
     * @param id 閰嶄欢ID
     * @param status 鏂扮姸鎬?
     * @return 鏄惁更新成功
     */
    boolean updateAccessoryStatus(Long id, Integer status);

    /**
     * 删除閰嶄欢
     * @param id 閰嶄欢ID
     * @return 鏄惁删除成功
     */
    boolean deleteAccessory(Long id);

    /**
     * 澶氭潯浠舵煡璇㈤厤浠跺垪琛?
     * @param params 查询鍙傛暟
     * @return 閰嶄欢鍒楄〃
     */
    List<Accessory> queryAccessories(Map<String, Object> params);
}
