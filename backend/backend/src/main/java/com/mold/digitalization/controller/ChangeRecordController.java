package com.mold.digitalization.controller;

import com.mold.digitalization.entity.ChangeRecord;
import com.mold.digitalization.service.ChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/change-records")
public class ChangeRecordController extends BaseController {

    @Autowired
    private ChangeRecordService service;

    @GetMapping("/{id}")
    public ResponseEntity<ChangeRecord> getById(@PathVariable Long id) {
        ChangeRecord r = service.getById(id);
        if (r != null) return success(r);
        return notFoundGeneric("Not found");
    }

    @GetMapping("/mold/code/{moldCode}")
    public ResponseEntity<List<ChangeRecord>> listByMoldCode(@PathVariable String moldCode) {
        return success(service.getByMoldCode(moldCode));
    }

    @GetMapping("/mold/{moldId}")
    public ResponseEntity<List<ChangeRecord>> listByMoldId(@PathVariable Long moldId) {
        return success(service.getByMoldId(moldId));
    }

    @PostMapping
    public ResponseEntity<ChangeRecord> create(@RequestBody ChangeRecord record) {
        boolean ok = service.create(record);
        if (ok) return success(record);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ChangeRecord record) {
        record.setId(id);
        boolean ok = service.updateRecord(record);
        if (ok) return success();
        return notFoundGeneric("Update failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean ok = service.deleteRecord(id);
        if (ok) return success();
        return notFoundGeneric("Delete failed");
    }
}