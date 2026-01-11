package com.mold.digitalization.controller;

import com.mold.digitalization.entity.EquipmentMaintenanceRecord;
import com.mold.digitalization.service.EquipmentMaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentMaintenanceController extends BaseController {

    @Autowired
    private EquipmentMaintenanceRecordService recordService;

    @GetMapping("/{id}/maintenance-records")
    public ResponseEntity<List<EquipmentMaintenanceRecord>> list(@PathVariable Long id) {
        return success(recordService.listByEquipmentId(id));
    }

    @PostMapping("/{id}/maintenance-records")
    public ResponseEntity<EquipmentMaintenanceRecord> add(@PathVariable Long id, @RequestBody EquipmentMaintenanceRecord record) {
        return success(recordService.addRecord(id, record));
    }

    @GetMapping("/{id}/maintenance-cycle")
    public ResponseEntity<Map<String, Object>> getCycle(@PathVariable Long id) {
        Map<String, Object> r = new HashMap<>();
        r.put("equipmentId", id);
        r.put("cycleDays", recordService.getCycleDays(id));
        return success(r);
    }

    @PutMapping("/{id}/maintenance-cycle")
    public ResponseEntity<Void> setCycle(@PathVariable Long id, @RequestParam Integer days) {
        recordService.setCycleDays(id, days);
        return success();
    }

    @GetMapping("/maintenance-reminders")
    public ResponseEntity<List<Map<String, Object>>> reminders(@RequestParam(defaultValue = "30") Integer days) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : recordService.cyclesSnapshot().entrySet()) {
            Long equipmentId = e.getKey();
            Integer cycle = e.getValue();
            List<EquipmentMaintenanceRecord> list = recordService.listByEquipmentId(equipmentId);
            if (cycle != null && cycle > 0 && !list.isEmpty()) {
                LocalDateTime last = list.get(0).getMaintenanceTime();
                LocalDateTime next = last.plusDays(cycle);
                LocalDateTime now = LocalDateTime.now();
                long diff = java.time.Duration.between(now, next).toDays();
                if (diff <= days) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("equipmentId", equipmentId);
                    item.put("nextDueTime", next);
                    item.put("daysLeft", diff);
                    res.add(item);
                }
            }
        }
        return success(res);
    }

    @GetMapping("/{id}/maintenance-records/page")
    public ResponseEntity<Map<String,Object>> page(@PathVariable Long id,
                                                   @RequestParam(required = false) Integer pageNum,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) String startTime,
                                                   @RequestParam(required = false) String endTime,
                                                   @RequestParam(required = false) Long operatorUserId,
                                                   @RequestParam(required = false) String keyword) {
        var p = recordService.pageList(id, pageNum, pageSize, startTime, endTime, operatorUserId, keyword);
        Map<String,Object> r = new HashMap<>();
        r.put("total", p.getTotal());
        r.put("pages", p.getPages());
        r.put("current", p.getCurrent());
        r.put("size", p.getSize());
        r.put("records", p.getRecords());
        return success(r);
    }
}
