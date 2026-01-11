package com.mold.digitalization.controller;

import com.mold.digitalization.entity.Equipment;
import com.mold.digitalization.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 璁惧控制鍣?
 * 澶勭悊璁惧鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController extends BaseController {
    
    @Autowired
    private EquipmentService equipmentService;
    
    /**
     * 鏍规嵁璁惧ID获取璁惧淇℃伅
     * @param id 璁惧ID
     * @return 璁惧淇℃伅
     */
    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        Equipment equipment = equipmentService.getById(id);
        if (equipment != null) {
            return success(equipment);
        } else {
            return notFoundGeneric("Equipment not found");
        }
    }
    
    /**
     * 鏍规嵁璁惧缂栫爜获取璁惧淇℃伅
     * @param equipmentCode 璁惧缂栫爜
     * @return 璁惧淇℃伅
     */
    @GetMapping("/code/{equipmentCode}")
    public ResponseEntity<Equipment> getEquipmentByCode(@PathVariable String equipmentCode) {
        Equipment equipment = equipmentService.getEquipmentByCode(equipmentCode);
        if (equipment != null) {
            return success(equipment);
        } else {
            return notFoundGeneric("Equipment not found");
        }
    }
    
    /**
     * 鏍规嵁璁惧绫诲瀷ID获取璁惧鍒楄〃
     * @param typeId 璁惧绫诲瀷ID
     * @return 璁惧鍒楄〃
     */
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<Equipment>> getEquipmentByTypeId(@PathVariable Long typeId) {
        List<Equipment> equipment = equipmentService.getEquipmentByTypeId(typeId);
        return success(equipment);
    }
    
    /**
     * 创建鏂拌澶?
     * @param equipment 璁惧淇℃伅
     * @return 创建鐨勮澶囦俊鎭?
     */
    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        boolean created = equipmentService.createEquipment(equipment);
        if (created) {
            return created(equipment);
        } else {
            return notFoundGeneric("创建璁惧失败");
        }
    }
    
    /**
     * 更新璁惧淇℃伅
     * @param id 璁惧ID
     * @param equipment 璁惧淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEquipment(@PathVariable Long id, @RequestBody Equipment equipment) {
        equipment.setId(id);
        boolean updated = equipmentService.updateEquipment(equipment);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 更新璁惧状态
     * @param id 璁惧ID
     * @param status 鏂扮姸鎬?
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateEquipmentStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean updated = equipmentService.updateEquipmentStatus(id, status);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除璁惧
     * @param id 璁惧ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        boolean deleted = equipmentService.deleteEquipment(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取鎵€鏈夎澶囧垪琛?
     * @return 璁惧鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> equipment = equipmentService.getAllEquipment();
        return success(equipment);
    }

    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @GetMapping("/usage-stats")
    public ResponseEntity<?> usageStats() {
        String sql = "select e.id as equipment_id, e.equipment_name as name, count(t.id) as in_progress, " +
                "avg(timestampdiff(day, now(), t.planned_end_time)) as days_to_offline " +
                "from equipment e left join production_task t on t.equipment_id = e.id and t.status = 1 " +
                "group by e.id, e.equipment_name";
        java.util.List<java.util.Map<String,Object>> list = new java.util.ArrayList<>();
        jdbcTemplate.query(sql, rs -> {
            var m = new java.util.HashMap<String,Object>();
            m.put("equipmentId", rs.getLong("equipment_id"));
            m.put("name", rs.getString("name"));
            m.put("inProgress", rs.getLong("in_progress"));
            m.put("daysToOffline", rs.getObject("days_to_offline"));
            list.add(m);
        });
        return success(list);
    }
}
