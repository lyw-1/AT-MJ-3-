package com.mold.digitalization.controller;

import com.mold.digitalization.entity.DeepHoleLayerRecord;
import com.mold.digitalization.entity.DeepHolePreDrillBatch;
import com.mold.digitalization.entity.DeepHoleProcess;
import com.mold.digitalization.service.DeepHoleProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/process/deep-hole")
public class DeepHoleProcessController extends BaseController {

    @Autowired
    private DeepHoleProcessService service;

    @PostMapping("/start")
    public ResponseEntity<DeepHoleProcess> start(@RequestBody Map<String, Object> payload) {
        String moldCode = payload.get("moldCode") != null ? payload.get("moldCode").toString() : null;
        Long equipmentId = payload.get("equipmentId") != null ? Long.valueOf(payload.get("equipmentId").toString()) : null;
        String equipmentName = payload.get("equipmentName") != null ? payload.get("equipmentName").toString() : null;
        Integer craftType = payload.get("craftType") != null ? Integer.valueOf(payload.get("craftType").toString()) : 1;
        Integer plannedLayerCount = payload.get("plannedLayerCount") != null ? Integer.valueOf(payload.get("plannedLayerCount").toString()) : null;
        LocalDateTime expectedFinishTime = payload.get("expectedFinishTime") != null ? LocalDateTime.parse(payload.get("expectedFinishTime").toString()) : null;
        DeepHoleProcess p = service.startProcess(moldCode, equipmentId, equipmentName, craftType, plannedLayerCount, expectedFinishTime);
        if (p != null) return success(p);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping("/{processId}/layers")
    public ResponseEntity<Void> addLayers(@PathVariable Long processId, @RequestBody List<DeepHoleLayerRecord> layers) {
        boolean ok = service.addLayerRecords(processId, layers);
        if (ok) return success();
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping("/{processId}/predrill")
    public ResponseEntity<Void> addPreDrill(@PathVariable Long processId, @RequestBody DeepHolePreDrillBatch batch) {
        boolean ok = service.addPreDrillBatch(processId, batch);
        if (ok) return success();
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping("/{processId}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long processId) {
        boolean ok = service.completeProcess(processId);
        if (ok) return success();
        return notFoundGeneric("Not found or fail");
    }

    @GetMapping("/{processId}")
    public ResponseEntity<DeepHoleProcess> get(@PathVariable Long processId) {
        DeepHoleProcess p = service.getDetail(processId);
        if (p != null) return success(p);
        return notFoundGeneric("Not found");
    }
}
