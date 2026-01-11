package com.mold.digitalization.controller;

import com.mold.digitalization.entity.OutsourcedHeatTreatment;
import com.mold.digitalization.service.OutsourcedHeatTreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/process/heat-treatment")
public class OutsourcedHeatTreatmentController extends BaseController {

    @Autowired
    private OutsourcedHeatTreatmentService service;

    @PostMapping
    public ResponseEntity<OutsourcedHeatTreatment> create(@RequestBody Map<String, Object> payload) {
        String moldCode = payload.get("moldCode") != null ? payload.get("moldCode").toString() : null;
        Integer type = payload.get("type") != null ? Integer.valueOf(payload.get("type").toString()) : null;
        String supplier = payload.get("supplier") != null ? payload.get("supplier").toString() : null;
        String targetHrc = payload.get("targetHrc") != null ? payload.get("targetHrc").toString() : null;
        LocalDateTime expectedFinishTime = payload.get("expectedFinishTime") != null ? LocalDateTime.parse(payload.get("expectedFinishTime").toString()) : null;
        OutsourcedHeatTreatment r = service.create(moldCode, type, supplier, targetHrc, expectedFinishTime);
        if (r != null) return success(r);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<Void> ship(@PathVariable Long id) {
        boolean ok = service.ship(id);
        if (ok) return success();
        return notFoundGeneric("Not found or fail");
    }

    @PutMapping("/{id}/back")
    public ResponseEntity<Void> back(@PathVariable Long id) {
        boolean ok = service.back(id);
        if (ok) return success();
        return notFoundGeneric("Not found or fail");
    }
}
