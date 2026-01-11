package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.MoldProcessRouteStep;
import com.mold.digitalization.service.MoldProcessRouteService;
import com.mold.digitalization.service.MoldProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/process-route")
public class ProcessRouteController extends BaseController {
    @Autowired private MoldProcessRouteService routeService;
    @Autowired private MoldProcessService moldProcessService;

    @GetMapping("/templates")
    public ResponseEntity<?> templates() {
        return success(List.of(
                Map.of("code","PREP","name","备料工序"),
                Map.of("code","DRILL_NON_LAYER","name","进泥孔粗加工工序"),
                Map.of("code","DRILL_LAYER","name","进泥孔精加工工序"),
                Map.of("code","HEAT_TREATMENT","name","热处理"),
                Map.of("code","GUIDE_SLOT","name","导料槽加工工序"),
                Map.of("code","SHAPE","name","外形加工工序"),
                Map.of("code","SELF_CHECK","name","裸模自检"),
                Map.of("code","QUALITY_CHECK","name","品质检测工序"),
                Map.of("code","IN_STORAGE","name","入库")
        ));
    }

    @GetMapping("/{moldId}/steps")
    public ResponseEntity<List<MoldProcessRouteStep>> list(@PathVariable Long moldId) {
        return success(routeService.listByMold(moldId));
    }

    @PostMapping("/{moldId}/steps")
    public ResponseEntity<Void> replace(@PathVariable Long moldId, @RequestBody List<MoldProcessRouteStep> steps) {
        routeService.replaceRoute(moldId, steps);
        return success();
    }

    @PostMapping("/{moldId}/instantiate")
    public ResponseEntity<?> instantiate(@PathVariable Long moldId) {
        List<MoldProcessRouteStep> steps = routeService.listByMold(moldId);
        for (MoldProcessRouteStep s : steps) {
            MoldProcess p = new MoldProcess();
            p.setMoldId(moldId);
            p.setProcessCode(String.valueOf(moldId));
            p.setProcessName(s.getStepName());
            p.setEquipmentId(s.getEquipmentId());
            p.setOperatorId(s.getOperatorId());
            p.setCurrentStatus(com.mold.digitalization.enums.ProcessStatusEnum.PENDING.getCode());
            p.setCurrentProgress(0);
            moldProcessService.createMoldProcess(p);
        }
        return success(Map.of("count", steps.size()));
    }
}
