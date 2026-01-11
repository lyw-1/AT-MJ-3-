package com.mold.digitalization.controller;

import com.mold.digitalization.engine.ProcessStateMachine;
import com.mold.digitalization.common.exception.BusinessException;
import com.mold.digitalization.enums.ErrorCodeEnum;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.service.MoldProcessService;
import com.mold.digitalization.service.ProcessStateMachineService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程状态机控制器
 * 提供流程状态迁移、可用事件、状态历史与当前状态查询等接口
 */
@RestController
@RequestMapping("/api/process-state-machine")
@Api(tags = "流程状态机")
public class ProcessStateMachineController extends BaseController {
    
    @Autowired
    private ProcessStateMachine processStateMachine;

    @Autowired
    private ProcessStateMachineService processStateMachineService;

    @Autowired
    private MoldProcessService moldProcessService;

    @Autowired
    private com.mold.digitalization.service.ProgressTrackingService progressTrackingService;
    
    /**
     * 触发状态迁移
     * @param processId 流程ID
     * @param event 事件名称
     * @param operator 操作人（ID或名称）
     * @param remark 备注
     * @return 操作结果
     */
    @PostMapping("/{processId}/transition")
    @ApiOperation("触发状态迁移")
    public ResponseEntity<Void> triggerTransition(
            @PathVariable Long processId,
            @RequestParam String event,
            @RequestParam String operator,
            @RequestParam(required = false) String remark) {
        
        try {
            MoldProcess before = moldProcessService.getById(processId);
            Integer fromStatus = before != null ? before.getCurrentStatus() : null;
            ProcessStateMachine.ProcessEvent processEvent = ProcessStateMachine.ProcessEvent.valueOf(event);
            if (ProcessStateMachine.ProcessEvent.START.equals(processEvent)) {
                Integer p = before == null ? null : before.getCurrentProgress();
                if (p == null || p <= 0) {
                    throw new BusinessException(ErrorCodeEnum.INVALID_OPERATION, new IllegalStateException("需先设置进度>0"));
                }
            }
            boolean success = processStateMachine.triggerTransition(processId, processEvent, operator, remark);
            
            if (success) {
                return success();
            } else {
                // 当前状态不允许该操作：返回 422
                throw new BusinessException(ErrorCodeEnum.INVALID_OPERATION, new IllegalStateException("当前状态不允许执行该操作"));
            }
        } catch (IllegalArgumentException e) {
            // 非法事件名或参数错误
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, e);
        } catch (IllegalStateException e) {
            // 当前状态不允许该操作
            throw new BusinessException(ErrorCodeEnum.INVALID_OPERATION, e);
        } catch (BusinessException e) {
            // 业务异常透传，由全局异常处理器映射为相应 HTTP 状态码（如 422）
            throw e;
        } catch (Exception e) {
            // 其他系统异常
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 获取状态历史
     * @param processId 流程ID
     * @return 历史列表（按 transition_time 倒序）
     */
    @GetMapping("/{processId}/history")
    @ApiOperation("获取状态历史")
    public ResponseEntity<List<ProcessStatusHistory>> getHistory(@PathVariable Long processId) {
        try {
            List<ProcessStatusHistory> histories = processStateMachineService.getStateTransitionHistory(processId);
            // 后端兜底按时间倒序
            histories = histories.stream()
                    .sorted((a, b) -> {
                        if (a.getTransitionTime() == null && b.getTransitionTime() == null) return 0;
                        if (a.getTransitionTime() == null) return 1;
                        if (b.getTransitionTime() == null) return -1;
                        return b.getTransitionTime().compareTo(a.getTransitionTime());
                    })
                    .collect(Collectors.toList());
            return success(histories);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 获取当前状态
     * @param processId 流程ID
     * @return 当前状态与关键时间
     */
    @GetMapping("/{processId}/current")
    @ApiOperation("获取当前状态")
    public ResponseEntity<CurrentStateDTO> getCurrentState(@PathVariable Long processId) {
        try {
            MoldProcess process = moldProcessService.getById(processId);
            if (process == null) {
                throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, new IllegalArgumentException("流程不存在: " + processId));
            }
            Integer currentStatus = process.getCurrentStatus();
            String statusDesc = processStateMachine.getStatusDescription(currentStatus == null ? 0 : currentStatus);
            CurrentStateDTO dto = new CurrentStateDTO();
            dto.setProcessId(processId);
            dto.setCurrentStatus(currentStatus);
            dto.setStatusDescription(statusDesc);
            dto.setActualStartTime(process.getActualStartTime());
            dto.setActualEndTime(process.getActualEndTime());
            return success(dto);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }

    /**
     * 检查是否允许状态迁移
     * @param processId 流程ID
     * @param event 事件名称
     * @return 是否允许
     */
    @GetMapping("/{processId}/can-transition")
    @ApiOperation("检查是否允许状态迁移")
    public ResponseEntity<Boolean> canTransition(
            @PathVariable Long processId,
            @RequestParam String event) {
        
        try {
            ProcessStateMachine.ProcessEvent processEvent = ProcessStateMachine.ProcessEvent.valueOf(event);
            boolean canTransition = processStateMachine.canTransition(processId, processEvent);
            return success(canTransition);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, e);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }

    @GetMapping("/{processId}/can-transition-detail")
    @ApiOperation("检查迁移可行性（含原因）")
    public ResponseEntity<CanTransitionDTO> canTransitionDetail(
            @PathVariable Long processId,
            @RequestParam String event) {
        try {
            ProcessStateMachine.ProcessEvent processEvent = ProcessStateMachine.ProcessEvent.valueOf(event);
            ProcessStateMachine.CanTransitionDetail detail = processStateMachine.canTransitionDetail(processId, processEvent);
            CanTransitionDTO dto = new CanTransitionDTO(detail.isAllowed(), detail.getReason());
            return success(dto);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, e);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取流程的可用事件
     * @param processId 流程ID
     * @return 可用事件列表
     */
    @GetMapping("/{processId}/available-events")
    @ApiOperation("获取可用事件列表")
    public ResponseEntity<List<EventInfo>> getAvailableEvents(@PathVariable Long processId) {
        try {
            ProcessStateMachine.ProcessEvent[] events = processStateMachine.getAvailableEvents(processId);
            List<EventInfo> eventInfos = Arrays.stream(events)
                    .map(event -> new EventInfo(event.name(), event.getDescription()))
                    .collect(Collectors.toList());
            return success(eventInfos);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取全部状态定义
     * @return 状态定义列表
     */
    @GetMapping("/states")
    @ApiOperation("获取全部状态定义")
    public ResponseEntity<List<StateInfo>> getAllStates() {
        try {
            List<StateInfo> stateInfos = Arrays.stream(ProcessStateMachine.ProcessState.values())
                    .map(state -> new StateInfo(state.getCode(), state.name(), state.getDescription()))
                    .collect(Collectors.toList());
            String raw = stateInfos.stream()
                    .map(s -> s.getCode() + ":" + s.getName() + ":" + (s.getDescription() == null ? "" : s.getDescription()))
                    .collect(Collectors.joining("|"));
            String version;
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
                byte[] digest = md.digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) sb.append(String.format("%02x", b));
                version = sb.toString();
            } catch (Exception e) {
                version = Integer.toHexString(raw.hashCode());
            }
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.add("X-States-Version", version);
            headers.add("X-States-Updated-At", java.time.OffsetDateTime.now().toString());
            return new ResponseEntity<>(stateInfos, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 获取全部事件定义
     * @return 事件定义列表
     */
    @GetMapping("/events")
    @ApiOperation("获取全部事件定义")
    public ResponseEntity<List<EventInfo>> getAllEvents() {
        try {
            List<EventInfo> eventInfos = Arrays.stream(ProcessStateMachine.ProcessEvent.values())
                    .map(event -> new EventInfo(event.name(), event.getDescription()))
                    .collect(Collectors.toList());
            return success(eventInfos);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }

    @GetMapping("/route")
    @ApiOperation("规划加工路线")
    public ResponseEntity<List<RouteStep>> planRoute(
            @RequestParam(required = false, defaultValue = "false") boolean needHeatTreatment,
            @RequestParam(required = false, defaultValue = "false") boolean preOutshapeForGuideSlot,
            @RequestParam(required = false) String guideSlotEquipment) {
        List<RouteStep> steps = new java.util.ArrayList<>();
        steps.add(new RouteStep("PREPARE_MATERIAL", "备料", java.util.List.of("磨床")));
        steps.add(new RouteStep("DEEP_HOLE_DRILLING", "进泥孔加工", java.util.List.of("深孔钻", "精雕机", "电火花成型机")));
        if (needHeatTreatment) {
            steps.add(new RouteStep("OUTSOURCED_HT", "委外热处理", java.util.List.of("外协")));
            steps.add(new RouteStep("STRESS_RELIEF", "去应力热处理", java.util.List.of("外协")));
            steps.add(new RouteStep("HARDENING", "加硬热处理", java.util.List.of("外协")));
        }
        boolean mustPreOutshape = preOutshapeForGuideSlot || (guideSlotEquipment != null && guideSlotEquipment.equals("切槽机"));
        if (mustPreOutshape) {
            steps.add(new RouteStep("OUTSHAPE_ENGRAVING", "外形加工", java.util.List.of("精雕机")));
        }
        steps.add(new RouteStep("GUIDE_SLOT_PROCESS", "导料槽加工", java.util.List.of("切槽机", "中走丝", "电火花成型机")));
        steps.add(new RouteStep("SELF_INSPECTION", "自检", java.util.List.of("检验工位")));
        steps.add(new RouteStep("WAREHOUSING", "入库（模库）", java.util.List.of("仓库")));
        return success(steps);
    }
    
    /**
     * Get status description
     * @param statusCode status code
     * @return description
     */
    @GetMapping("/status-description")
    @ApiOperation("获取状态描述")
    public ResponseEntity<String> getStatusDescription(@RequestParam int statusCode) {
        try {
            String description = processStateMachine.getStatusDescription(statusCode);
            return success(description);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * Get event description
     * @param eventName event name
     * @return description
     */
    @GetMapping("/event-description")
    @ApiOperation("获取事件描述")
    public ResponseEntity<String> getEventDescription(@RequestParam String eventName) {
        try {
            String description = processStateMachine.getEventDescription(eventName);
            return success(description);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }

    @PostMapping("/dev-force-complete")
    @ApiOperation("开发模式：强制完成流程")
    public ResponseEntity<?> devForceComplete(
            @RequestParam Long processId,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) String remark
    ) {
        try {
            boolean ok1 = progressTrackingService.updateProgress(processId, 100, operatorId);
            boolean ok2 = processStateMachine.triggerTransition(processId, ProcessStateMachine.ProcessEvent.COMPLETE, 
                    operatorId == null ? "dev-user" : String.valueOf(operatorId), remark);
            if (ok1 && ok2) {
                MoldProcess after = moldProcessService.getById(processId);
                Integer toStatus = after != null ? after.getCurrentStatus() : null;
                Integer fromStatus = com.mold.digitalization.enums.ProcessStatusEnum.PROCESSING.getCode();
                String desc = processStateMachine.getEventDescription("COMPLETE");
                processStateMachineService.recordStateTransition(processId, fromStatus, toStatus, "COMPLETE", operatorId, desc);
                return success();
            }
            return notFoundGeneric("Force complete failed");
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }
    
    /** 状态信息 */
    public static class StateInfo {
        private int code;
        private String name;
        private String description;
        
        public StateInfo(int code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
        
        public int getCode() {
            return code;
        }
        
        public void setCode(int code) {
            this.code = code;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
    }
    
    /** 事件信息 */
    public static class EventInfo {
        private String name;
        private String description;
        
        public EventInfo(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class RouteStep {
        private String code;
        private String name;
        private java.util.List<String> equipmentOptions;

        public RouteStep(String code, String name, java.util.List<String> equipmentOptions) {
            this.code = code;
            this.name = name;
            this.equipmentOptions = equipmentOptions;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public java.util.List<String> getEquipmentOptions() { return equipmentOptions; }
        public void setEquipmentOptions(java.util.List<String> equipmentOptions) { this.equipmentOptions = equipmentOptions; }
    }

    public static class CanTransitionDTO {
        private boolean allowed;
        private String reason;

        public CanTransitionDTO(boolean allowed, String reason) {
            this.allowed = allowed;
            this.reason = reason;
        }

        public boolean isAllowed() { return allowed; }
        public void setAllowed(boolean allowed) { this.allowed = allowed; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    /**
     * 当前状态 DTO
     */
    public static class CurrentStateDTO {
        private Long processId;
        private Integer currentStatus;
        private String statusDescription;
        private java.time.LocalDateTime actualStartTime;
        private java.time.LocalDateTime actualEndTime;

        public Long getProcessId() {
            return processId;
        }

        public void setProcessId(Long processId) {
            this.processId = processId;
        }

        public Integer getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(Integer currentStatus) {
            this.currentStatus = currentStatus;
        }

        public String getStatusDescription() {
            return statusDescription;
        }

        public void setStatusDescription(String statusDescription) {
            this.statusDescription = statusDescription;
        }

        public java.time.LocalDateTime getActualStartTime() {
            return actualStartTime;
        }

        public void setActualStartTime(java.time.LocalDateTime actualStartTime) {
            this.actualStartTime = actualStartTime;
        }

        public java.time.LocalDateTime getActualEndTime() {
            return actualEndTime;
        }

        public void setActualEndTime(java.time.LocalDateTime actualEndTime) {
            this.actualEndTime = actualEndTime;
        }
    }
}
