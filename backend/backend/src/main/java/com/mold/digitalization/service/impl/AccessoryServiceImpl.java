package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mold.digitalization.entity.Accessory;
import com.mold.digitalization.mapper.AccessoryMapper;
import com.mold.digitalization.service.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢服务实现绫?
 * 实现閰嶄欢鐩稿叧鐨勪笟鍔￠€昏緫操作
 */
@Service
@Transactional
public class AccessoryServiceImpl implements AccessoryService {

    @Autowired
    private AccessoryMapper accessoryMapper;

    /**
     * 鏍规嵁ID获取閰嶄欢淇℃伅
     * @param id 閰嶄欢ID
     * @return 閰嶄欢淇℃伅
     */
    @Override
    public Accessory getById(Long id) {
        return accessoryMapper.selectById(id);
    }

    /**
     * 鏍规嵁閰嶄欢缂栧彿获取閰嶄欢淇℃伅
     * @param accessoryCode 閰嶄欢缂栧彿
     * @return 閰嶄欢淇℃伅
     */
    @Override
    public Accessory getByAccessoryCode(String accessoryCode) {
        QueryWrapper<Accessory> wrapper = new QueryWrapper<>();
        wrapper.eq("accessory_code", accessoryCode);
        return accessoryMapper.selectOne(wrapper);
    }

    /**
     * 鏍规嵁閰嶄欢绫诲瀷ID获取閰嶄欢鍒楄〃
     * @param accessoryTypeId 閰嶄欢绫诲瀷ID
     * @return 閰嶄欢鍒楄〃
     */
    @Override
    public List<Accessory> getByAccessoryTypeId(Long accessoryTypeId) {
        QueryWrapper<Accessory> wrapper = new QueryWrapper<>();
        wrapper.eq("accessory_type_id", accessoryTypeId);
        return accessoryMapper.selectList(wrapper);
    }

    /**
     * 获取鎵€鏈夐厤浠朵俊鎭?
     * @return 閰嶄欢鍒楄〃
     */
    @Override
    public List<Accessory> getAllAccessories() {
        return accessoryMapper.selectList(null);
    }

    /**
     * 鏍规嵁鐘舵€佽幏鍙栭厤浠跺垪琛?
     * @param status 閰嶄欢状态
     * @return 閰嶄欢鍒楄〃
     */
    @Override
    public List<Accessory> getByStatus(Integer status) {
        QueryWrapper<Accessory> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return accessoryMapper.selectList(wrapper);
    }

    /**
     * 搴撳瓨棰勮閰嶄欢鍒楄〃锛堜綆浜庢渶浣庡簱瀛橀槇鍊硷級
     * @return 搴撳瓨棰勮閰嶄欢鍒楄〃
     */
    @Override
    public List<Accessory> getLowStockAccessories() {
        QueryWrapper<Accessory> wrapper = new QueryWrapper<>();
        wrapper.lt("stock_quantity", "minimum_stock");
        return accessoryMapper.selectList(wrapper);
    }

    /**
     * 创建鏂伴厤浠?
     * @param accessory 閰嶄欢淇℃伅
     * @return 鏄惁创建成功
     */
    @Override
    public boolean createAccessory(Accessory accessory) {
        accessory.setCreateTime(LocalDateTime.now());
        accessory.setUpdateTime(LocalDateTime.now());
        return accessoryMapper.insert(accessory) > 0;
    }

    /**
     * 更新閰嶄欢淇℃伅
     * @param accessory 閰嶄欢淇℃伅
     * @return 鏄惁更新成功
     */
    @Override
    public boolean updateAccessory(Accessory accessory) {
        accessory.setUpdateTime(LocalDateTime.now());
        return accessoryMapper.updateById(accessory) > 0;
    }

    /**
     * 更新閰嶄欢搴撳瓨鏁伴噺
     * @param id 閰嶄欢ID
     * @param quantity 璋冩暣鏁伴噺锛堟鏁板鍔狅紝璐熸暟鍑忓皯锛?
     * @return 鏄惁更新成功
     */
    @Override
    public boolean updateStockQuantity(Long id, Integer quantity) {
        Accessory accessory = accessoryMapper.selectById(id);
        if (accessory == null) {
            return false;
        }

        int newQuantity = accessory.getStockQuantity() + quantity;
        if (newQuantity < 0) {
            return false;
        }

        UpdateWrapper<Accessory> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id)
               .set("stock_quantity", newQuantity)
               .set("update_time", LocalDateTime.now());
        
        // 濡傛灉鏂版暟閲忎綆浜庢渶浣庡簱瀛橀槇鍊硷紝鑷姩更新鐘舵€佷负缂鸿揣(2)
        if (newQuantity < accessory.getMinimumStock()) {
            wrapper.set("status", 2);
        } else if (accessory.getStatus() == 2) {
            // 濡傛灉涔嬪墠鏄己璐х姸鎬侊紝鐜板湪鎭㈠姝ｅ父搴撳瓨锛屾洿鏂扮姸鎬佷负姝ｅ父(0)
            wrapper.set("status", 0);
        }
        
        return accessoryMapper.update(null, wrapper) > 0;
    }

    /**
     * 更新閰嶄欢状态
     * @param id 閰嶄欢ID
     * @param status 鏂扮姸鎬?
     * @return 鏄惁更新成功
     */
    @Override
    public boolean updateAccessoryStatus(Long id, Integer status) {
        UpdateWrapper<Accessory> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id)
               .set("status", status)
               .set("update_time", LocalDateTime.now());
        return accessoryMapper.update(null, wrapper) > 0;
    }

    /**
     * 删除閰嶄欢
     * @param id 閰嶄欢ID
     * @return 鏄惁删除成功
     */
    @Override
    public boolean deleteAccessory(Long id) {
        return accessoryMapper.deleteById(id) > 0;
    }

    /**
     * 澶氭潯浠舵煡璇㈤厤浠跺垪琛?
     * @param params 查询鍙傛暟
     * @return 閰嶄欢鍒楄〃
     */
    @Override
    public List<Accessory> queryAccessories(Map<String, Object> params) {
        QueryWrapper<Accessory> wrapper = new QueryWrapper<>();
        
        // 鍔ㄦ€佹瀯寤烘煡璇㈡潯浠?
        if (params != null) {
            if (params.containsKey("accessoryName")) {
                wrapper.like("accessory_name", params.get("accessoryName"));
            }
            if (params.containsKey("material")) {
                wrapper.like("material", params.get("material"));
            }
            if (params.containsKey("specification")) {
                wrapper.like("specification", params.get("specification"));
            }
            if (params.containsKey("location")) {
                wrapper.like("location", params.get("location"));
            }
            if (params.containsKey("minStock")) {
                wrapper.ge("stock_quantity", params.get("minStock"));
            }
            if (params.containsKey("maxStock")) {
                wrapper.le("stock_quantity", params.get("maxStock"));
            }
        }
        
        // 榛樿鎸夋洿鏂版椂闂撮檷搴忔帓搴?
        wrapper.orderByDesc("update_time");
        
        return accessoryMapper.selectList(wrapper);
    }
}
