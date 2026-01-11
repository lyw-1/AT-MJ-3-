package com.mold.digitalization.controller;

import com.mold.digitalization.entity.EdmFormingProcess;
import com.mold.digitalization.service.EdmFormingProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/process/edm-forming")
public class EdmFormingProcessController extends BaseController {

    @Autowired
    private EdmFormingProcessService service;

    @PostMapping("/start")
    public ResponseEntity<EdmFormingProcess> start(@RequestBody Map<String, Object> payload) {
        String moldCode = payload.get("moldCode") != null ? payload.get("moldCode").toString() : null;
        Long equipmentId = payload.get("equipmentId") != null ? Long.valueOf(payload.get("equipmentId").toString()) : null;
        String equipmentName = payload.get("equipmentName") != null ? payload.get("equipmentName").toString() : null;
        String stage = payload.get("stage") != null ? payload.get("stage").toString() : null;
        LocalDateTime expectedFinishTime = payload.get("expectedFinishTime") != null ? LocalDateTime.parse(payload.get("expectedFinishTime").toString()) : null;
        EdmFormingProcess p = service.start(moldCode, equipmentId, equipmentName, stage, expectedFinishTime);
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