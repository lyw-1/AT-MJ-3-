package com.mold.digitalization.controller;

import com.mold.digitalization.entity.BareMoldInspection;
import com.mold.digitalization.service.BareMoldInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/process/bare-mold-inspection")
public class BareMoldInspectionController extends BaseController {

    @Autowired
    private BareMoldInspectionService service;

    @PostMapping
    public ResponseEntity<BareMoldInspection> create(@RequestBody Map<String, Object> payload) {
        String moldCode = payload.get("moldCode") != null ? payload.get("moldCode").toString() : null;
        Long inspectorId = payload.get("inspectorId") != null ? Long.valueOf(payload.get("inspectorId").toString()) : null;
        String inspectorName = payload.get("inspectorName") != null ? payload.get("inspectorName").toString() : null;
        String remark = payload.get("remark") != null ? payload.get("remark").toString() : null;
        LocalDateTime inspectionTime = payload.get("inspectionTime") != null ? LocalDateTime.parse(payload.get("inspectionTime").toString()) : null;
        BareMoldInspection r = service.create(moldCode, inspectorId, inspectorName, inspectionTime, remark);
        if (r != null) return success(r);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
