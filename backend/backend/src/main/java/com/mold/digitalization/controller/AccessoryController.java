package com.mold.digitalization.controller;

import com.mold.digitalization.entity.Accessory;
import com.mold.digitalization.service.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 閰嶄欢控制鍣?
 * 澶勭悊閰嶄欢鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/accessories")
public class AccessoryController extends BaseController {

    @Autowired
    private AccessoryService accessoryService;

    /**
     * 鏍规嵁閰嶄欢ID获取閰嶄欢淇℃伅
     * @param id 閰嶄欢ID
     * @return 閰嶄欢淇℃伅
     */
    @GetMapping("/{id}")
    public ResponseEntity<Accessory> getAccessoryById(@PathVariable Long id) {
        Accessory accessory = accessoryService.getById(id);
        if (accessory != null) {
            return success(accessory);
        } else {
            return notFoundGeneric("Accessory not found");
        }
    }

    /**
     * 鏍规嵁閰嶄欢缂栧彿获取閰嶄欢淇℃伅
     * @param accessoryCode 閰嶄欢缂栧彿
     * @return 閰嶄欢淇℃伅
     */
    @GetMapping("/code/{accessoryCode}")
    public ResponseEntity<Accessory> getAccessoryByCode(@PathVariable String accessoryCode) {
        Accessory accessory = accessoryService.getByAccessoryCode(accessoryCode);
        if (accessory != null) {
            return success(accessory);
        } else {
            return notFoundGeneric("Accessory not found");
        }
    }

    /**
     * 鏍规嵁閰嶄欢绫诲瀷ID获取閰嶄欢鍒楄〃
     * @param typeId 閰嶄欢绫诲瀷ID
     * @return 閰嶄欢鍒楄〃
     */
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<Accessory>> getAccessoriesByTypeId(@PathVariable Long typeId) {
        List<Accessory> accessories = accessoryService.getByAccessoryTypeId(typeId);
        return success(accessories);
    }

    /**
     * 鏍规嵁閰嶄欢鐘舵€佽幏鍙栭厤浠跺垪琛?
     * @param status 閰嶄欢状态
     * @return 閰嶄欢鍒楄〃
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Accessory>> getAccessoriesByStatus(@PathVariable Integer status) {
        List<Accessory> accessories = accessoryService.getByStatus(status);
        return success(accessories);
    }

    /**
     * 获取搴撳瓨涓嶈冻鐨勯厤浠跺垪琛?
     * @return 搴撳瓨涓嶈冻鐨勯厤浠跺垪琛?
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<Accessory>> getLowStockAccessories() {
        List<Accessory> accessories = accessoryService.getLowStockAccessories();
        return success(accessories);
    }

    /**
     * 创建鏂伴厤浠?
     * @param accessory 閰嶄欢淇℃伅
     * @return 创建鐨勯厤浠朵俊鎭?
     */
    @PostMapping
    public ResponseEntity<Accessory> createAccessory(@RequestBody Accessory accessory) {
        boolean created = accessoryService.createAccessory(accessory);
        if (created) {
            return success(accessory);
        } else {
            // 返回500鐘舵€佺爜鍜宯ull锛屼繚鎸佽繑鍥炵被鍨嬩竴鑷?
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 更新閰嶄欢淇℃伅
     * @param id 閰嶄欢ID
     * @param accessory 閰嶄欢淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAccessory(@PathVariable Long id, @RequestBody Accessory accessory) {
        accessory.setId(id);
        boolean updated = accessoryService.updateAccessory(accessory);
        if (updated) {
            return success();
        } else {
            // 返回404鐘舵€佺爜鍜宯ull锛屼繚鎸佽繑鍥炵被鍨嬩竴鑷?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 更新閰嶄欢搴撳瓨
     * @param id 閰嶄欢ID
     * @param quantity 璋冩暣鏁伴噺锛堟鏁板鍔狅紝璐熸暟鍑忓皯锛?
     * @return 更新结果
     */
    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> updateAccessoryStock(@PathVariable Long id, @RequestParam Integer quantity) {
        boolean updated = accessoryService.updateStockQuantity(id, quantity);
        if (updated) {
            return success();
        } else {
            // 返回404鐘舵€佺爜鍜宯ull锛屼繚鎸佽繑鍥炵被鍨嬩竴鑷?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 更新閰嶄欢状态
     * @param id 閰嶄欢ID
     * @param status 鏂扮姸鎬?
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateAccessoryStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean updated = accessoryService.updateAccessoryStatus(id, status);
        if (updated) {
            return success();
        } else {
            // 返回404鐘舵€佺爜鍜宯ull锛屼繚鎸佽繑鍥炵被鍨嬩竴鑷?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 删除閰嶄欢
     * @param id 閰嶄欢ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        boolean deleted = accessoryService.deleteAccessory(id);
        if (deleted) {
            return success();
        } else {
            // 返回404鐘舵€佺爜鍜宯ull锛屼繚鎸佽繑鍥炵被鍨嬩竴鑷?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 获取鎵€鏈夐厤浠跺垪琛?
     * @return 閰嶄欢鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<Accessory>> getAllAccessories() {
        List<Accessory> accessories = accessoryService.getAllAccessories();
        return success(accessories);
    }

    /**
     * 获取閰嶄欢缁熻淇℃伅
     * @return 缁熻淇℃伅
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAccessoryStats() {
        // 涓存椂返回绌簃ap锛屽洜涓篈ccessoryService涓病鏈塯etAccessoryStats方法
        return success(java.util.Collections.emptyMap());
    }
}
