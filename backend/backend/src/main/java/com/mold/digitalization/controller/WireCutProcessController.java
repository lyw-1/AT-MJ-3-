package com.mold.digitalization.controller;

import com.mold.digitalization.entity.WireCutProcess;
import com.mold.digitalization.service.WireCutProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/process/wire-cut")
public class WireCutProcessController extends BaseController {

    @Autowired
    private WireCutProcessService service;

    @PostMapping("/start")
    public ResponseEntity<WireCutProcess> start(@RequestBody Map<String, Object> payload) {
        String moldCode = payload.get("moldCode") != null ? payload.get("moldCode").toString() : null;
        Long equipmentId = payload.get("equipmentId") != null ? Long.valueOf(payload.get("equipmentId").toString()) : null;
        String equipmentName = payload.get("equipmentName") != null ? payload.get("equipmentName").toString() : null;
        LocalDateTime expectedFinishTime = payload.get("expectedFinishTime") != null ? LocalDateTime.parse(payload.get("expectedFinishTime").toString()) : null;
        WireCutProcess p = service.start(moldCode, equipmentId, equipmentName, expectedFinishTime);
        if (p != null) return success(p);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long id) {
        boolean ok = service.complete(id);
        if (ok) return success();
        return notFoundGeneric("Not found or fail");
    }
}
