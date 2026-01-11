package com.mold.digitalization.controller;

import com.mold.digitalization.entity.SlottingLayerRecord;
import com.mold.digitalization.entity.SlottingProcess;
import com.mold.digitalization.service.SlottingProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/process/slotting")
public class SlottingProcessController extends BaseController {

    @Autowired
    private SlottingProcessService service;

    @PostMapping("/start")
    public ResponseEntity<SlottingProcess> start(@RequestBody Map<String, Object> payload) {
        String moldCode = payload.get("moldCode") != null ? payload.get("moldCode").toString() : null;
        Long equipmentId = payload.get("equipmentId") != null ? Long.valueOf(payload.get("equipmentId").toString()) : null;
        String equipmentName = payload.get("equipmentName") != null ? payload.get("equipmentName").toString() : null;
        LocalDateTime expectedFinishTime = payload.get("expectedFinishTime") != null ? LocalDateTime.parse(payload.get("expectedFinishTime").toString()) : null;
        SlottingProcess p = service.start(moldCode, equipmentId, equipmentName, expectedFinishTime);
        if (p != null) return success(p);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping("/{processId}/layers")
    public ResponseEntity<Void> addLayers(@PathVariable Long processId, @RequestBody List<SlottingLayerRecord> layers) {
        boolean ok = service.addLayers(processId, layers);
        if (ok) return success();
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping("/{processId}/axis/{axis}/complete")
    public ResponseEntity<Void> completeAxis(@PathVariable Long processId, @PathVariable String axis) {
        boolean ok = service.completeAxis(processId, axis);
        if (ok) return success();
        return notFoundGeneric("Not found or fail");
    }

    @PutMapping("/{processId}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long processId) {
        boolean ok = service.complete(processId);
        if (ok) return success();
        return notFoundGeneric("Not found or fail");
    }

    @GetMapping("/{processId}")
    public ResponseEntity<SlottingProcess> get(@PathVariable Long processId) {
        SlottingProcess p = service.getDetail(processId);
        if (p != null) return success(p);
        return notFoundGeneric("Not found");
    }
}
