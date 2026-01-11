package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldAccessoryInfo;
import com.mold.digitalization.service.MoldAccessoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mold-accessory-info")
public class MoldAccessoryInfoController extends BaseController {

    @Autowired
    private MoldAccessoryInfoService service;

    @GetMapping("/mold/{moldCode}")
    public ResponseEntity<List<MoldAccessoryInfo>> listByMoldCode(final @PathVariable String moldCode) {
        return success(service.getByMoldCode(moldCode));
    }

    @PostMapping
    public ResponseEntity<MoldAccessoryInfo> create(final @RequestBody MoldAccessoryInfo info) {
        boolean ok = service.save(info);
        if (ok) {
            return success(info);
        }
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(final @PathVariable Long id, final @RequestBody MoldAccessoryInfo info) {
        info.setId(id);
        boolean ok = service.updateById(info);
        if (ok) {
            return success();
        }
        return notFoundGeneric("Update failed");
    }

    @PostMapping("/status")
    public ResponseEntity<Void> updateStatus(final @RequestBody Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        List<String> seqCodes = (List<String>) payload.get("seqCodes");
        String status = payload.get("status") != null ? payload.get("status").toString() : null;
        String handler = payload.get("handler") != null ? payload.get("handler").toString() : null;
        boolean ok = service.updateStatusAndHandler(seqCodes, status, handler);
        if (ok) {
            return success();
        }
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}