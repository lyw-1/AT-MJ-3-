package com.mold.digitalization.controller;

import com.mold.digitalization.entity.EquipmentAccessoryReplacementRecord;
import com.mold.digitalization.service.EquipmentAccessoryReplacementRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentAccessoryReplacementController extends BaseController {

    @Autowired
    private EquipmentAccessoryReplacementRecordService recordService;

    @GetMapping("/{id}/replacement-records")
    public ResponseEntity<List<EquipmentAccessoryReplacementRecord>> list(@PathVariable Long id) {
        return success(recordService.listByEquipmentId(id));
    }

    @PostMapping("/{id}/replacement-records")
    public ResponseEntity<EquipmentAccessoryReplacementRecord> add(@PathVariable Long id, @RequestBody EquipmentAccessoryReplacementRecord record) {
        return success(recordService.addRecord(id, record));
    }

    @GetMapping("/{id}/replacement-records/page")
    public ResponseEntity<java.util.Map<String,Object>> page(@PathVariable Long id,
                                                   @RequestParam(required = false) Integer pageNum,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) String startTime,
                                                   @RequestParam(required = false) String endTime,
                                                   @RequestParam(required = false) String accessoryName,
                                                   @RequestParam(required = false) String accessoryCode,
                                                   @RequestParam(required = false) Long operatorUserId) {
        var p = recordService.pageList(id, pageNum, pageSize, startTime, endTime, accessoryName, accessoryCode, operatorUserId);
        java.util.Map<String,Object> r = new java.util.HashMap<>();
        r.put("total", p.getTotal());
        r.put("pages", p.getPages());
        r.put("current", p.getCurrent());
        r.put("size", p.getSize());
        r.put("records", p.getRecords());
        return success(r);
    }
}
