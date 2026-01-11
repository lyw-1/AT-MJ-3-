package com.mold.digitalization.controller;

import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.service.MoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 模具控制器
 * 处理模具相关的HTTP请求
 */
@RestController
@RequestMapping("/api/molds")
public class MoldController extends BaseController {
    
    @Autowired
    private MoldService moldService;
    
    /**
     * 根据模具ID获取模具信息
     * @param id 模具ID
     * @return 模具信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mold> getMoldById(@PathVariable Long id) {
        Mold mold = moldService.getById(id);
        if (mold != null) {
            return success(mold);
        } else {
            return notFoundGeneric("Mold not found");
        }
    }
    
    /**
     * 根据模具编码获取模具信息
     * @param moldCode 模具编码
     * @return 模具信息
     */
    @GetMapping("/code/{moldCode}")
    public ResponseEntity<Mold> getMoldByCode(@PathVariable String moldCode) {
        Mold mold = moldService.getMoldByCode(moldCode);
        if (mold != null) {
            return success(mold);
        } else {
            return notFoundGeneric("Mold not found");
        }
    }
    
    /**
     * 根据模具类型ID获取模具列表
     * @param typeId 模具类型ID
     * @return 模具列表
     */
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<Mold>> getMoldsByTypeId(@PathVariable Long typeId) {
        List<Mold> molds = moldService.getMoldsByTypeId(typeId);
        return success(molds);
    }
    
    /**
     * 根据模具状态ID获取模具列表
     * @param statusId 模具状态ID
     * @return 模具列表
     */
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Mold>> getMoldsByStatusId(@PathVariable Long statusId) {
        List<Mold> molds = moldService.getMoldsByStatusId(statusId);
        return success(molds);
    }
    
    /**
     * 创建新模具
     * @param mold 模具信息
     * @return 创建的模具信息
     */
    @PostMapping
    public ResponseEntity<Mold> createMold(@RequestBody Mold mold) {
        boolean created = moldService.createMold(mold);
        if (created) {
            return success(mold);
        } else {
            return notFoundGeneric("创建模具失败");
        }
    }
    
    /**
     * 更新模具信息
     * @param id 模具ID
     * @param mold 模具信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMold(@PathVariable Long id, @RequestBody Mold mold) {
        mold.setId(id);
        boolean updated = moldService.updateMold(mold);
        if (updated) {
            return success();
        } else {
            return notFoundGeneric("模具不存在或更新失败");
        }
    }
    
    /**
     * 更新模具状态
     * @param id 模具ID
     * @param statusId 新状态ID
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateMoldStatus(@PathVariable Long id, @RequestParam Long statusId) {
        boolean updated = moldService.updateMoldStatus(id, statusId);
        if (updated) {
            return success();
        } else {
            return notFoundGeneric("模具不存在或更新失败");
        }
    }
    
    /**
     * 删除模具
     * @param id 模具ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMold(@PathVariable Long id) {
        boolean deleted = moldService.deleteMold(id);
        if (deleted) {
            return success();
        } else {
            return notFoundGeneric("模具不存在或删除失败");
        }
    }
    
    /**
     * 获取所有模具列表
     * @return 模具列表
     */
    @GetMapping
    public ResponseEntity<List<Mold>> getAllMolds() {
        List<Mold> molds = moldService.getAllMolds();
        return success(molds);
    }

    @GetMapping("/key")
    public ResponseEntity<List<Mold>> getKeyMolds() {
        List<Mold> list = moldService.lambdaQuery().eq(Mold::getIsKey, true).list();
        return success(list);
    }

    @PutMapping("/{id}/key")
    public ResponseEntity<Void> setKey(@PathVariable Long id, @RequestParam boolean key) {
        Mold m = moldService.getById(id);
        if (m == null) {
            return notFoundGeneric("not found");
        }
        m.setIsKey(key);
        return moldService.updateById(m) ? success() : notFoundGeneric("not found");
    }

    @GetMapping("/offline7days")
    public ResponseEntity<List<Mold>> getOffline7Days() {
        var now = java.time.LocalDateTime.now();
        var end = now.plusDays(7);
        List<Mold> list = moldService.lambdaQuery()
                .between(Mold::getEstimatedScrapTime, now, end)
                .list();
        return success(list);
    }
}
