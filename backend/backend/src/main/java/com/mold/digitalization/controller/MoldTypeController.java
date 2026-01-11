package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldType;
import com.mold.digitalization.service.MoldTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 妯″叿绫诲瀷控制鍣? * 澶勭悊妯″叿绫诲瀷鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/mold-types")
public class MoldTypeController extends BaseController {
    
    @Autowired
    private MoldTypeService moldTypeService;
    
    /**
     * 鏍规嵁妯″叿绫诲瀷ID获取妯″叿绫诲瀷淇℃伅
     * @param id 妯″叿绫诲瀷ID
     * @return 妯″叿绫诲瀷淇℃伅
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoldType> getMoldTypeById(@PathVariable Long id) {
        MoldType moldType = moldTypeService.getById(id);
        if (moldType != null) {
            return success(moldType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 鏍规嵁妯″叿绫诲瀷缂栫爜获取妯″叿绫诲瀷淇℃伅
     * @param typeCode 妯″叿绫诲瀷缂栫爜
     * @return 妯″叿绫诲瀷淇℃伅
     */
    @GetMapping("/code/{typeCode}")
    public ResponseEntity<MoldType> getMoldTypeByCode(@PathVariable String typeCode) {
        MoldType moldType = moldTypeService.getMoldTypeByCode(typeCode);
        if (moldType != null) {
            return success(moldType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建鏂版ā鍏风被鍨?     * @param moldType 妯″叿绫诲瀷淇℃伅
     * @return 创建鐨勬ā鍏风被鍨嬩俊鎭?     */
    @PostMapping
    public ResponseEntity<MoldType> createMoldType(@RequestBody MoldType moldType) {
        boolean created = moldTypeService.createMoldType(moldType);
        if (created) {
            return created(moldType);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新妯″叿绫诲瀷淇℃伅
     * @param id 妯″叿绫诲瀷ID
     * @param moldType 妯″叿绫诲瀷淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMoldType(@PathVariable Long id, @RequestBody MoldType moldType) {
        moldType.setId(id);
        boolean updated = moldTypeService.updateMoldType(moldType);
        if (updated) {
            return success();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除妯″叿绫诲瀷
     * @param id 妯″叿绫诲瀷ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoldType(@PathVariable Long id) {
        boolean deleted = moldTypeService.deleteMoldType(id);
        if (deleted) {
            return success();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取鎵€鏈夋ā鍏风被鍨嬪垪琛?     * @return 妯″叿绫诲瀷鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<MoldType>> getAllMoldTypes() {
        try {
            List<MoldType> moldTypes = moldTypeService.getAllMoldTypes();
            return success(moldTypes);
        } catch (Exception e) {
            return success(java.util.Collections.emptyList());
        }
    }
}