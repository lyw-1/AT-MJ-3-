package com.mold.digitalization.controller;

import com.mold.digitalization.entity.*;
import com.mold.digitalization.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mold-archive")
public class MoldArchiveController extends BaseController {

    @Autowired
    private MoldArchiveService archiveService;
    @Autowired
    private SlotWidthRecordService slotWidthRecordService;
    @Autowired
    private ChangeRecordService changeRecordService;
    @Autowired
    private TuningRecordService tuningRecordService;
    @Autowired
    private ProductionRecordService productionRecordService;
    @Autowired
    private MoldBorrowReturnRecordService borrowReturnService;
    @Autowired
    private MoldService moldService;

    @GetMapping("/{id}")
    public ResponseEntity<MoldArchive> getById(@PathVariable Long id) {
        MoldArchive a = archiveService.getById(id);
        if (a != null) return success(a);
        return notFoundGeneric("Not found");
    }

    @GetMapping("/code/{moldCode}")
    public ResponseEntity<MoldArchive> getByMoldCode(@PathVariable String moldCode) {
        MoldArchive a = archiveService.getByMoldCode(moldCode);
        if (a != null) return success(a);
        return notFoundGeneric("Not found");
    }

    @PostMapping
    public ResponseEntity<MoldArchive> create(@RequestBody MoldArchive archive) {
        boolean ok = archiveService.create(archive);
        if (ok) return success(archive);
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MoldArchive archive) {
        archive.setId(id);
        boolean ok = archiveService.updateArchive(archive);
        if (ok) return success();
        return notFoundGeneric("Update failed");
    }

    @GetMapping("/detail/{moldCode}")
    public ResponseEntity<Map<String, Object>> getArchiveDetail(@PathVariable String moldCode) {
        Map<String, Object> result = new HashMap<>();
        MoldArchive a = archiveService.getByMoldCode(moldCode);
        if (a == null) return notFoundGeneric("Not found");
        result.put("archive", a);
        Mold mold = moldService.getMoldByCode(moldCode);
        Long moldId = mold != null ? mold.getId() : null;
        List<SlotWidthRecord> slotList = moldId != null ? slotWidthRecordService.getByMoldId(moldId) : java.util.Collections.emptyList();
        result.put("slotWidthRecords", slotList);
        List<TuningRecord> tuningList = tuningRecordService.getByMoldCode(moldCode);
        result.put("tuningRecords", tuningList);
        List<ChangeRecord> changeList = changeRecordService.getByMoldCode(moldCode);
        result.put("changeRecords", changeList);
        List<ProductionRecord> prodList = moldId != null ? productionRecordService.getProductionRecordsByMoldId(moldId) : java.util.Collections.emptyList();
        result.put("productionRecords", prodList);
        List<MoldBorrowReturnRecord> borrowReturnList = moldId != null ? borrowReturnService.getByMoldId(moldId) : java.util.Collections.emptyList();
        result.put("borrowReturnRecords", borrowReturnList);
        return success(result);
    }
}