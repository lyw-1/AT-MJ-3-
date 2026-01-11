package com.mold.digitalization.controller;

import com.mold.digitalization.entity.TuningRecord;
import com.mold.digitalization.service.TuningRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/tuning-records")
public class TuningRecordController extends BaseController {

    @Autowired
    private TuningRecordService service;

    @GetMapping("/{id}")
    public ResponseEntity<TuningRecord> getById(@PathVariable Long id) {
        TuningRecord r = service.getById(id);
        if (r != null) return success(r);
        return notFoundGeneric("Not found");
    }

    @GetMapping("/mold/code/{moldCode}")
    public ResponseEntity<List<TuningRecord>> listByMoldCode(@PathVariable String moldCode) {
        return success(service.getByMoldCode(moldCode));
    }

    @GetMapping("/mold/{moldId}")
    public ResponseEntity<List<TuningRecord>> listByMoldId(@PathVariable Long moldId) {
        return success(service.getByMoldId(moldId));
    }

    @PostMapping
    public ResponseEntity<TuningRecord> create(@Valid @RequestBody TuningRecord record) {
        boolean ok = service.create(record);
        if (ok) return success(record);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody TuningRecord record) {
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
