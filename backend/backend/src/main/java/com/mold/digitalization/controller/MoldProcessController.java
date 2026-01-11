package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.enums.ProcessStatusEnum;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.service.MoldService;
import com.mold.digitalization.service.MoldProcessService;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.service.MoldInitialParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/mold-process")
public class MoldProcessController extends BaseController {

    @Autowired
    private MoldProcessService moldProcessService;
    @Autowired
    private MoldService moldService;
    @Autowired
    private MoldProcessMapper moldProcessMapper;
    @Autowired
    private MoldInitialParameterService moldInitialParameterService;
    @Autowired
    private com.mold.digitalization.engine.ProcessStateMachine processStateMachine;

    @GetMapping("/{id}")
    public ResponseEntity<MoldProcess> getById(@PathVariable Long id) {
        MoldProcess p = moldProcessService.getById(id);
        if (p == null) return notFoundGeneric("Not Found");
        return success(p);
    }

    @GetMapping
    public ResponseEntity<java.util.List<MoldProcess>> listAll() {
        try {
            return success(moldProcessService.getAllMoldProcesses());
        } catch (Exception e) {
            return success(java.util.List.of());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody java.util.Map<String, Object> payload) {
        MoldProcess exist = moldProcessService.getById(id);
        if (exist == null) return notFoundGeneric("Not Found");
        try {
            if (payload.containsKey("processName")) exist.setProcessName(String.valueOf(payload.get("processName")));
            if (payload.containsKey("description")) exist.setDescription(String.valueOf(payload.get("description")));
            if (payload.containsKey("remark")) exist.setRemark(String.valueOf(payload.get("remark")));
            // 先更新基础字段
            moldProcessService.updateMoldProcess(exist);
            // 独立更新状态与进度（避免部分环境字段限制）
            if (payload.containsKey("currentStatus")) {
                Integer st = null; try { st = Integer.valueOf(String.valueOf(payload.get("currentStatus"))); } catch (Exception ignored) {}
                if (st != null) moldProcessMapper.updateProcessStatus(id, st);
            }
            if (payload.containsKey("currentProgress")) {
                Integer pg = null; try { pg = Integer.valueOf(String.valueOf(payload.get("currentProgress"))); } catch (Exception ignored) {}
                if (pg != null) moldProcessMapper.updateProcessProgress(id, pg);
            }
            return success();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean ok = moldProcessService.deleteMoldProcess(id);
        if (ok) return success();
        return notFoundGeneric("Not Found");
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> start(@PathVariable Long id, @RequestParam(required = false) String operator, @RequestParam(required = false) String remark) {
        boolean ok = processStateMachine.triggerTransition(id, com.mold.digitalization.engine.ProcessStateMachine.ProcessEvent.START, operator, remark);
        if (ok) return success();
        return ResponseEntity.status(422).build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long id, @RequestParam(required = false) String operator, @RequestParam(required = false) String remark) {
        boolean ok = processStateMachine.triggerTransition(id, com.mold.digitalization.engine.ProcessStateMachine.ProcessEvent.COMPLETE, operator, remark);
        if (ok) return success();
        return ResponseEntity.status(422).build();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> payload) {
        try {
            String moldName = String.valueOf(payload.getOrDefault("moldName", "流程"));
            String moldNumber = String.valueOf(payload.getOrDefault("moldNumber", "AUTO"));
            String moldCategory = String.valueOf(payload.getOrDefault("moldCategory", "UNKNOWN"));
            String material = String.valueOf(payload.getOrDefault("material", ""));
            Integer cavityCount = null;
            try { cavityCount = payload.get("cavityCount") != null ? Integer.valueOf(String.valueOf(payload.get("cavityCount"))) : null; } catch (Exception ignored) {}
            String remark = String.valueOf(payload.getOrDefault("remark", ""));
            if (remark != null && remark.length() > 200) {
                remark = null;
            }
            Long operatorId = null;
            try { operatorId = payload.get("operatorId") != null ? Long.valueOf(String.valueOf(payload.get("operatorId"))) : null; } catch (Exception ignored) {}

            MoldProcess existed = null;
            try { existed = moldProcessService.getMoldProcessByCode(moldNumber); } catch (Exception ignored) {}
            if (existed != null && existed.getId() != null) {
                return ResponseEntity.ok(java.util.Map.of("id", existed.getId(), "exists", true));
            }

            Mold mold = null;
            try { mold = moldService.getMoldByCode(moldNumber); } catch (Exception ignored) {}
            if (mold == null || mold.getId() == null) {
                String defaultName = moldName != null && !moldName.isBlank() ? moldName : ("流程-" + moldNumber);
                String specName = defaultName;
                try {
                    com.mold.digitalization.entity.MoldInitialParameter latest = moldInitialParameterService.getLatestByMoldCode(moldNumber);
                    if (latest != null && latest.getProductSpec() != null && !latest.getProductSpec().isBlank()) {
                        specName = latest.getProductSpec();
                    }
                } catch (Exception ignored) {}
                Mold newMold = new Mold();
                newMold.setMoldCode(moldNumber);
                newMold.setMoldName(specName);
                newMold.setMoldTypeId(1L);
                newMold.setMoldStatusId(1L);
                newMold.setResponsibleUserId(operatorId != null && operatorId > 0 ? operatorId : 1L);
                newMold.setCreateTime(LocalDateTime.now());
                newMold.setUpdateTime(LocalDateTime.now());
                boolean created = false;
                try { created = moldService.createMold(newMold); } catch (Exception ignored) {}
                if (created) {
                    mold = moldService.getMoldByCode(moldNumber);
                }
                if (mold == null || mold.getId() == null) {
                    return ResponseEntity.status(400).body(Map.of(
                            "code", 400,
                            "message", "模号不存在：请先在《模具初始参数列表》建立该模具初始参数",
                            "moldCode", moldNumber
                    ));
                }
            }

            MoldProcess p = new MoldProcess();
            p.setProcessName(moldName);
            p.setProcessCode(moldNumber);
            String desc = "类别:" + moldCategory + (material.isEmpty() ? "" : (" 材料:" + material)) + (cavityCount != null ? (" 孔密度:" + cavityCount) : "");
            if (desc != null && desc.length() > 200) desc = desc.substring(0, 200);
            p.setDescription(desc);
            p.setRemark(remark);
            p.setCurrentStatus(ProcessStatusEnum.PENDING.getCode());
            p.setCurrentProgress(0);
            p.setCreateTime(LocalDateTime.now());
            p.setUpdateTime(LocalDateTime.now());
            p.setMoldId(mold.getId());
            if (p.getEquipmentId() == null) {
                p.setEquipmentId(null);
            }
            if (operatorId != null && operatorId > 0) {
                p.setOperatorId(operatorId);
            } else {
                p.setOperatorId(null);
            }

            int rows = moldProcessMapper.insertDev(p);
            if (rows <= 0) return ResponseEntity.status(500).body(Map.of("code", 500, "message", "Create failed"));
            Long id = p.getId();
            if (id == null) {
                try {
                    MoldProcess found = moldProcessService.getMoldProcessByCode(moldNumber);
                    if (found != null) id = found.getId();
                } catch (Exception ignored) {}
            }
            return ResponseEntity.status(201).body(Map.of("id", id != null ? id : -1));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("code", 500, "message", e.getMessage()));
        }
    }

    @GetMapping("/dev-create")
    public ResponseEntity<Map<String, Object>> devCreate(
            @RequestParam String moldName,
            @RequestParam String moldNumber,
            @RequestParam(required = false) String moldCategory,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) Integer cavityCount,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) Long operatorId
    ) {
        try {
            MoldProcess existed = null;
            try { existed = moldProcessService.getMoldProcessByCode(moldNumber); } catch (Exception ignored) {}
            if (existed != null && existed.getId() != null) {
                return ResponseEntity.ok(java.util.Map.of("id", existed.getId(), "exists", true));
            }
            MoldProcess p = new MoldProcess();
            p.setProcessName(moldName);
            p.setProcessCode(moldNumber);
            String desc = (moldCategory != null ? ("类别:" + moldCategory + " ") : "") +
                    (material != null ? ("材料:" + material + " ") : "") +
                    (cavityCount != null ? ("孔密度:" + cavityCount) : "");
            if (desc != null && desc.length() > 200) desc = desc.substring(0, 200);
            p.setDescription(desc.trim());
            if (remark != null && remark.length() > 200) remark = null;
            p.setRemark(remark);
            p.setCurrentStatus(com.mold.digitalization.enums.ProcessStatusEnum.PENDING.getCode());
            p.setCurrentProgress(0);
            p.setCreateTime(java.time.LocalDateTime.now());
            p.setUpdateTime(java.time.LocalDateTime.now());
            Mold mold = null;
            try { mold = moldService.getMoldByCode(moldNumber); } catch (Exception ignored) {}
            if (mold == null || mold.getId() == null) {
                return ResponseEntity.status(400).body(java.util.Map.of(
                        "code", 400,
                        "message", "模号不存在：请先在《模具初始参数列表》建立该模具初始参数",
                        "moldCode", moldNumber
                ));
            }
            p.setMoldId(mold.getId());
            p.setEquipmentId(null);
            if (operatorId != null && operatorId > 0) p.setOperatorId(operatorId);
            else p.setOperatorId(null);
            int rows = moldProcessMapper.insertDev(p);
            if (rows <= 0) return ResponseEntity.status(500).body(java.util.Map.of("code", 500, "message", "Create failed"));
            Long id = p.getId();
            if (id == null) {
                try {
                    MoldProcess found = moldProcessService.getMoldProcessByCode(moldNumber);
                    if (found != null) id = found.getId();
                } catch (Exception ignored) {}
            }
            return ResponseEntity.status(201).body(java.util.Map.of("id", id != null ? id : -1));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("code", 500, "message", e.getMessage()));
        }
    }
}
