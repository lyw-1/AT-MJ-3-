package com.mold.digitalization.controller;

import com.mold.digitalization.entity.EquipmentType;
import com.mold.digitalization.service.EquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 璁惧绫诲瀷控制鍣?
 * 澶勭悊璁惧绫诲瀷鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/equipment-types")
public class EquipmentTypeController extends BaseController {
    
    @Autowired
    private EquipmentTypeService equipmentTypeService;
    
    /**
     * 鏍规嵁璁惧绫诲瀷ID获取璁惧绫诲瀷淇℃伅
     * @param id 璁惧绫诲瀷ID
     * @return 璁惧绫诲瀷淇℃伅
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentType> getEquipmentTypeById(@PathVariable Long id) {
        EquipmentType equipmentType = equipmentTypeService.getById(id);
        if (equipmentType != null) {
            return success(equipmentType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 鏍规嵁璁惧绫诲瀷缂栫爜获取璁惧绫诲瀷淇℃伅
     * @param typeCode 璁惧绫诲瀷缂栫爜
     * @return 璁惧绫诲瀷淇℃伅
     */
    @GetMapping("/code/{typeCode}")
    public ResponseEntity<EquipmentType> getEquipmentTypeByCode(@PathVariable String typeCode) {
        EquipmentType equipmentType = equipmentTypeService.getEquipmentTypeByCode(typeCode);
        if (equipmentType != null) {
            return success(equipmentType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建鏂拌澶囩被鍨?
     * @param equipmentType 璁惧绫诲瀷淇℃伅
     * @return 创建鐨勮澶囩被鍨嬩俊鎭?
     */
    @PostMapping
    public ResponseEntity<EquipmentType> createEquipmentType(@RequestBody EquipmentType equipmentType) {
        boolean created = equipmentTypeService.createEquipmentType(equipmentType);
        if (created) {
            return created(equipmentType);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新璁惧绫诲瀷淇℃伅
     * @param id 璁惧绫诲瀷ID
     * @param equipmentType 璁惧绫诲瀷淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentType> updateEquipmentType(@PathVariable Long id, @RequestBody EquipmentType equipmentType) {
        equipmentType.setId(id);
        boolean updated = equipmentTypeService.updateEquipmentType(equipmentType);
        if (updated) {
            return success(equipmentType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除璁惧绫诲瀷
     * @param id 璁惧绫诲瀷ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<EquipmentType> deleteEquipmentType(@PathVariable Long id) {
        EquipmentType equipmentType = equipmentTypeService.getById(id);
        if (equipmentType == null) {
            return ResponseEntity.notFound().build();
        }
        
        boolean deleted = equipmentTypeService.deleteEquipmentType(id);
        if (deleted) {
            return success(equipmentType);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取鎵€鏈夎澶囩被鍨嬪垪琛?
     * @return 璁惧绫诲瀷鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<EquipmentType>> getAllEquipmentTypes() {
        List<EquipmentType> equipmentTypes = equipmentTypeService.getAllEquipmentTypes();
        return success(equipmentTypes);
    }
}
