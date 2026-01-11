package com.mold.digitalization.controller;

import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.service.ProgressTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/progress")
public class ProgressTrackingController extends BaseController {

    @Autowired
    private ProgressTrackingService progressTrackingService;

    @GetMapping("/{processId}")
    public ResponseEntity<Map<String, Object>> getProgress(@PathVariable Long processId) {
        Integer p = progressTrackingService.getCurrentProgress(processId);
        return success(Map.of("processId", processId, "progress", p));
    }

    @PutMapping("/{processId}")
    public ResponseEntity<?> update(@PathVariable Long processId, @RequestBody Map<String, Object> body) {
        Integer progress = null;
        Long operatorId = null;
        try { progress = body.get("progress") != null ? Integer.valueOf(String.valueOf(body.get("progress"))) : null; } catch (Exception ignored) {}
        try { operatorId = body.get("operatorId") != null ? Long.valueOf(String.valueOf(body.get("operatorId"))) : null; } catch (Exception ignored) {}
        if (progress == null) return badRequest("progress is required");
        boolean ok = progressTrackingService.updateProgress(processId, progress, operatorId);
        if (!ok) return notFoundGeneric("Update progress failed");
        return success(Map.of("processId", processId, "progress", progress));
    }

    @GetMapping("/{processId}/history")
    public ResponseEntity<java.util.List<ProcessStatusHistory>> history(@PathVariable Long processId) {
        return success(progressTrackingService.getProgressHistory(processId));
    }
}
