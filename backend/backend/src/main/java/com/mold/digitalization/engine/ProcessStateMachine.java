package com.mold.digitalization.engine;

import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.entity.MoldStorageRecord;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.entity.MoldStatus;
import com.mold.digitalization.service.MoldProcessService;
import com.mold.digitalization.service.ProcessStatusHistoryService;
import com.mold.digitalization.service.MoldStorageRecordService;
import com.mold.digitalization.service.MoldService;
import com.mold.digitalization.service.MoldStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 妯″叿鍔犲伐娴佺▼鐘舵€佹満开曟搸
 * 璐熻矗绠＄悊娴佺▼鐘舵€佽浆鎹㈠拰涓氬姟瑙勫垯验证
 */
@Component
public class ProcessStateMachine {
    
    @Autowired
    private MoldProcessService moldProcessService;
    
    @Autowired
    private ProcessStatusHistoryService statusHistoryService;
    @Autowired
    private MoldStorageRecordService moldStorageRecordService;
    @Autowired
    private MoldService moldService;
    @Autowired
    private MoldStatusService moldStatusService;
    
    /**
     * 娴佺▼鐘舵€佸畾涔?
     */
    public enum ProcessState {
        CREATED(0, "Created"),
        PLANNED(1, "Planned"),
        IN_PROGRESS(2, "In Progress"),
        PAUSED(3, "Paused"),
        COMPLETED(4, "Completed"),
        CANCELLED(5, "Cancelled"),
        FAILED(6, "Failed");
        
        private final int code;
        private final String description;
        
        ProcessState(int code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
        
        public static ProcessState fromCode(int code) {
            for (ProcessState state : values()) {
                if (state.code == code) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Unknown state code: " + code);
        }

        public static boolean isTerminal(ProcessState state) {
            return state == COMPLETED || state == CANCELLED || state == FAILED;
        }
    }
    
    /**
     * 娴佺▼浜嬩欢瀹氫箟
     */
    public enum ProcessEvent {
        PLAN("Plan"),
        START("Start"),
        PAUSE("Pause"),
        RESUME("Resume"),
        COMPLETE("Complete"),
        CANCEL("Cancel"),
        FAIL("Fail");
        
        private final String description;
        
        ProcessEvent(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }

    public static class CanTransitionDetail {
        private boolean allowed;
        private String reason;

        public CanTransitionDetail(boolean allowed, String reason) {
            this.allowed = allowed;
            this.reason = reason;
        }

        public boolean isAllowed() { return allowed; }
        public void setAllowed(boolean allowed) { this.allowed = allowed; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    /**
     * 鐘舵€佽浆鎹㈣鍒?
     */
    private static class TransitionRule {
        private final ProcessState fromState;
        private final ProcessEvent event;
        private final ProcessState toState;
        private final String condition;
        
        public TransitionRule(ProcessState fromState, ProcessEvent event, ProcessState toState, String condition) {
            this.fromState = fromState;
            this.event = event;
            this.toState = toState;
            this.condition = condition;
        }
        
        public boolean canTransition(ProcessState currentState, ProcessEvent event) {
            return this.fromState == currentState && this.event == event;
        }
        
        public ProcessState getToState() {
            return toState;
        }
        
        public String getCondition() {
            return condition;
        }
    }
    
    // 鐘舵€佽浆鎹㈣鍒欒〃
    private final TransitionRule[] transitionRules = {
        // 浠庡凡创建状态
    new TransitionRule(ProcessState.CREATED, ProcessEvent.PLAN, ProcessState.PLANNED, "No condition"),
    new TransitionRule(ProcessState.CREATED, ProcessEvent.CANCEL, ProcessState.CANCELLED, "No condition"),
        
        // 浠庡凡璁″垝状态
    new TransitionRule(ProcessState.PLANNED, ProcessEvent.START, ProcessState.IN_PROGRESS, "No condition"),
    new TransitionRule(ProcessState.PLANNED, ProcessEvent.CANCEL, ProcessState.CANCELLED, "No condition"),
        
        // 浠庤繘琛屼腑状态
    new TransitionRule(ProcessState.IN_PROGRESS, ProcessEvent.PAUSE, ProcessState.PAUSED, "No condition"),
    new TransitionRule(ProcessState.IN_PROGRESS, ProcessEvent.COMPLETE, ProcessState.COMPLETED, "Must have 100% progress"),
    new TransitionRule(ProcessState.IN_PROGRESS, ProcessEvent.FAIL, ProcessState.FAILED, "No condition"),
    new TransitionRule(ProcessState.IN_PROGRESS, ProcessEvent.CANCEL, ProcessState.CANCELLED, "No condition"),
        
        // 浠庡凡鏆傚仠状态
    new TransitionRule(ProcessState.PAUSED, ProcessEvent.RESUME, ProcessState.IN_PROGRESS, "No condition"),
    new TransitionRule(ProcessState.PAUSED, ProcessEvent.CANCEL, ProcessState.CANCELLED, "No condition"),
        
        // 浠庡凡瀹屾垚鐘舵€侊紙涓嶅厑璁歌浆鎹級
    new TransitionRule(ProcessState.COMPLETED, ProcessEvent.START, ProcessState.COMPLETED, "Completed state cannot be restarted"),
        
        // 浠庡凡鍙栨秷鐘舵€侊紙涓嶅厑璁歌浆鎹級
    new TransitionRule(ProcessState.CANCELLED, ProcessEvent.START, ProcessState.CANCELLED, "Cancelled state cannot be restarted"),
        
        // 浠庡凡失败鐘舵€侊紙涓嶅厑璁歌浆鎹級
        new TransitionRule(ProcessState.FAILED, ProcessEvent.START, ProcessState.FAILED, "Failed state cannot be restarted")
    };
    
    /**
     * 瑙﹀彂鐘舵€佽浆鎹?
     * @param processId 娴佺▼ID
     * @param event 娴佺▼浜嬩欢
     * @param operator 操作浜哄憳
     * @param remark 澶囨敞淇℃伅
     * @return 杞崲鏄惁成功
     */
    public boolean triggerTransition(Long processId, ProcessEvent event, String operator, String remark) {
        // 获取褰撳墠娴佺▼
        MoldProcess process = moldProcessService.getById(processId);
        if (process == null) {
            throw new IllegalArgumentException("Process does not exist: " + processId);
        }
        
        // 获取当前状态（避免 currentStatus 为空导致 NPE）
        Integer currentStatus = process.getCurrentStatus();
        ProcessState currentState = currentStatus != null ? ProcessState.fromCode(currentStatus) : ProcessState.CREATED;
        
        // 终止态优化：禁止在终止态触发 START，避免形成自循环
        if (ProcessState.isTerminal(currentState) && event == ProcessEvent.START) {
            return false;
        }

        // 查找匹配的转换规则
        TransitionRule rule = findTransitionRule(currentState, event);
        if (rule == null) {
            // 无匹配规则，表示当前状态不允许该事件，返回 false 由控制器统一转换为 422/1007
            return false;
        }
        
        // 验证转换条件
        if (!validateTransitionCondition(process, rule)) {
            // 条件不满足，返回 false 由控制器统一转换为 422/1007
            return false;
        }
        
        // 执行鐘舵€佽浆鎹?
        ProcessState newState = rule.getToState();
        boolean updated = moldProcessService.updateMoldProcessStatus(processId, newState.getCode());
        
        if (updated) {
            // 记录状态变更（避免记录失败导致整体 500）
            try {
                recordStatusHistory(processId, currentState, newState, event, operator, remark);
            } catch (Exception logEx) {
                // 仅记录日志，不影响主流程
                org.slf4j.LoggerFactory.getLogger(ProcessStateMachine.class)
                        .warn("记录状态历史失败: processId={}, from={}, to={}, event={}, err={}",
                                processId, currentState, newState, event, logEx.getMessage());
            }
            
            // 触发状态变更后的业务逻辑
            try {
                handleStateChange(process, currentState, newState, event);
            } catch (Exception bizEx) {
                org.slf4j.LoggerFactory.getLogger(ProcessStateMachine.class)
                        .warn("状态变更后业务处理失败: processId={}, event={}, err={}",
                                processId, event, bizEx.getMessage());
            }
        }
        
        return updated;
    }
    
    /**
     * 鏌ユ壘鍖归厤鐨勮浆鎹㈣鍒?
     */
    private TransitionRule findTransitionRule(ProcessState currentState, ProcessEvent event) {
        for (TransitionRule rule : transitionRules) {
            if (rule.canTransition(currentState, event)) {
                return rule;
            }
        }
        return null;
    }
    
    /**
     * 验证杞崲鏉′欢
     */
    private boolean validateTransitionCondition(MoldProcess process, TransitionRule rule) {
        Integer currentStatus = process.getCurrentStatus();
        ProcessState currentState = currentStatus != null ? ProcessState.fromCode(currentStatus) : ProcessState.CREATED;
        ProcessEvent event = rule.event;
        
        // 鐗规畩鏉′欢验证
        if (event == ProcessEvent.COMPLETE && currentState == ProcessState.IN_PROGRESS) {
            // 瀹屾垚娴佺▼闇€瑕佽繘搴︿负100%
            return process.getCurrentProgress() != null && process.getCurrentProgress() >= 100;
        }
        
        // 鍏朵粬杞崲鏉′欢验证
        return true;
    }
    
    /**
     * 记录鐘舵€佸巻鍙?
     */
    private void recordStatusHistory(Long processId, ProcessState fromState, ProcessState toState, 
                                   ProcessEvent event, String operator, String remark) {
        ProcessStatusHistory history = new ProcessStatusHistory();
        history.setProcessId(processId);
        history.setStatus(toState.getCode());
        history.setStatusDescription(toState.getDescription());
        // 记录来源/目标状态与事件
        history.setFromStatus(fromState.getCode());
        history.setToStatus(toState.getCode());
        history.setEvent(event.name());
        history.setDescription(String.format("%s: %s -> %s", event.getDescription(), fromState.getDescription(), toState.getDescription()));
        // 操作人ID可能是非数字（例如用户名），避免 NumberFormatException 导致 500
        Long operatorId = null;
        if (operator != null) {
            try {
                operatorId = Long.parseLong(operator);
            } catch (NumberFormatException ex) {
                // 非数字操作人标识，记录为空，仍然继续流程
                operatorId = null;
            }
        }
        history.setOperatorId(operatorId);
        history.setOperationRemark(remark);
        history.setOperationTime(LocalDateTime.now());
        // 同步设置 transition_time，便于按时间排序
        history.setTransitionTime(LocalDateTime.now());
        
        // 直接保存完整历史记录，避免部分字段丢失
        statusHistoryService.save(history);
    }
    
    /**
     * 澶勭悊鐘舵€佸彉鏇村悗鐨勪笟鍔￠€昏緫
     */
    private void handleStateChange(MoldProcess process, ProcessState fromState, ProcessState toState, ProcessEvent event) {
        // 先同步最新状态到当前实体，避免后续 updateById 覆盖为旧状态
        try {
            process.setCurrentStatus(toState.getCode());
        } catch (Exception ignore) {
            // 防御式处理，确保不会因空指针影响后续业务
        }

        // 鏍规嵁涓嶅悓鐨勭姸鎬佽浆鎹㈡墽琛岀浉搴旂殑涓氬姟逻辑
        switch (event) {
            case START:
                // 娴佺▼开€濮嬫椂鐨勪笟鍔￠€昏緫
                handleProcessStart(process);
                break;
            case COMPLETE:
                // 娴佺▼瀹屾垚鏃剁殑涓氬姟逻辑
                handleProcessComplete(process);
                break;
            case FAIL:
                // 娴佺▼失败鏃剁殑涓氬姟逻辑
                handleProcessFailure(process);
                break;
            case CANCEL:
                // 娴佺▼鍙栨秷鏃剁殑涓氬姟逻辑
                handleProcessCancel(process);
                break;
            default:
                // 鍏朵粬浜嬩欢鐨勫鐞嗛€昏緫
                break;
        }
    }
    
    /**
     * 澶勭悊娴佺▼开€濮?
     */
    private void handleProcessStart(MoldProcess process) {
        // 设置开€濮嬫椂闂?
        process.setActualStartTime(LocalDateTime.now());
        moldProcessService.updateMoldProcess(process);
        
        // 可以鍦ㄨ繖閲屾坊鍔犲叾浠栦笟鍔￠€昏緫锛屽鍙戦€侀€氱煡銆佹洿鏂扮浉鍏宠祫婧愮姸鎬佺瓑
    }
    
    /**
     * 澶勭悊娴佺▼瀹屾垚
     */
    private void handleProcessComplete(MoldProcess process) {
        // 设置瀹屾垚鏃堕棿
        process.setActualEndTime(LocalDateTime.now());
        moldProcessService.updateMoldProcess(process);
        
        // 璁＄畻实际鑰楁椂
        if (process.getActualStartTime() != null && process.getActualEndTime() != null) {
            long duration = java.time.Duration.between(process.getActualStartTime(), process.getActualEndTime()).toMinutes();
            process.setActualDuration((int) duration); // 杞崲涓哄垎閽?
            moldProcessService.updateMoldProcess(process);
        }
        
        Long moldId = process.getMoldId();
        Mold mold = moldId != null ? moldService.getById(moldId) : null;
        String moldCode = mold != null ? mold.getMoldCode() : null;
        String location = (mold != null && mold.getLocation() != null && !mold.getLocation().isBlank()) ? mold.getLocation() : "仓库-待检";
        MoldStorageRecord record = new MoldStorageRecord();
        record.setMoldId(moldId);
        record.setMoldCode(moldCode);
        record.setStorageDate(LocalDateTime.now());
        record.setBatchNumber(process.getProcessCode());
        record.setQuantity(1);
        record.setLocation(location);
        record.setOperatorId(process.getOperatorId());
        record.setInspectionResult(0);
        record.setRemark("流程完成自动入库");
        try { moldStorageRecordService.createRecord(record); } catch (Exception ignored) {}
        if (mold != null) {
            mold.setLocation(location);
            mold.setStorageTime(LocalDateTime.now());
            MoldStatus stored = null;
            try { stored = moldStatusService.getMoldStatusByCode("IN_STORAGE"); } catch (Exception ignored) {}
            if (stored == null) {
                try { stored = moldStatusService.getMoldStatusByCode("STORED"); } catch (Exception ignored) {}
            }
            if (stored != null && stored.getId() != null) {
                mold.setMoldStatusId(stored.getId());
            }
            mold.setUpdateTime(LocalDateTime.now());
            try { moldService.updateMold(mold); } catch (Exception ignored) {}
        }
    }
    
    /**
     * 澶勭悊娴佺▼失败
     */
    private void handleProcessFailure(MoldProcess process) {
        // 设置缁撴潫鏃堕棿
        process.setActualEndTime(LocalDateTime.now());
        moldProcessService.updateMoldProcess(process);
        
        // 可以鍦ㄨ繖閲屾坊鍔犲け璐ュ鐞嗛€昏緫锛屽记录失败鍘熷洜銆佸彂閫佸憡璀︾瓑
    }
    
    /**
     * 澶勭悊娴佺▼鍙栨秷
     */
    private void handleProcessCancel(MoldProcess process) {
        // 设置缁撴潫鏃堕棿
        process.setActualEndTime(LocalDateTime.now());
        moldProcessService.updateMoldProcess(process);
        
        // 可以鍦ㄨ繖閲屾坊鍔犲彇娑堝鐞嗛€昏緫锛屽閲婃斁璧勬簮銆佹洿鏂拌鍒掔瓑
    }
    
    /**
     * 妫€鏌ユ槸鍚﹀厑璁哥姸鎬佽浆鎹?
     * @param processId 娴佺▼ID
     * @param event 娴佺▼浜嬩欢
     * @return 鏄惁鍏佽杞崲
     */
    public boolean canTransition(Long processId, ProcessEvent event) {
        MoldProcess process = moldProcessService.getById(processId);
        if (process == null) {
            return false;
        }
        
        Integer currentStatus = process.getCurrentStatus();
        ProcessState currentState = currentStatus != null ? ProcessState.fromCode(currentStatus) : ProcessState.CREATED;

        // 终止态优化：禁止在终止态触发 START
        if (ProcessState.isTerminal(currentState) && event == ProcessEvent.START) {
            return false;
        }

        TransitionRule rule = findTransitionRule(currentState, event);
        
        if (rule == null) {
            return false;
        }
        
        return validateTransitionCondition(process, rule);
    }

    public CanTransitionDetail canTransitionDetail(Long processId, ProcessEvent event) {
        MoldProcess process = moldProcessService.getById(processId);
        if (process == null) {
            return new CanTransitionDetail(false, "流程不存在");
        }

        Integer currentStatus = process.getCurrentStatus();
        ProcessState currentState = currentStatus != null ? ProcessState.fromCode(currentStatus) : ProcessState.CREATED;

        if (ProcessState.isTerminal(currentState) && event == ProcessEvent.START) {
            return new CanTransitionDetail(false, "终止态不可重新开始");
        }

        TransitionRule rule = findTransitionRule(currentState, event);
        if (rule == null) {
            return new CanTransitionDetail(false, "当前状态不允许该事件");
        }

        boolean ok = validateTransitionCondition(process, rule);
        if (!ok) {
            String reason = rule.getCondition() != null ? rule.getCondition() : "转换条件未满足";
            return new CanTransitionDetail(false, reason);
        }
        return new CanTransitionDetail(true, null);
    }
    
    /**
     * 获取娴佺▼鐨勫彲鐢ㄤ簨浠跺垪琛?
     * @param processId 娴佺▼ID
     * @return 鍙敤浜嬩欢鍒楄〃
     */
    public ProcessEvent[] getAvailableEvents(Long processId) {
        MoldProcess process = moldProcessService.getById(processId);
        if (process == null) {
            return new ProcessEvent[0];
        }
        
        Integer currentStatus = process.getCurrentStatus();
        ProcessState currentState = currentStatus != null ? ProcessState.fromCode(currentStatus) : ProcessState.CREATED;
        return java.util.Arrays.stream(transitionRules)
                .filter(rule -> rule.fromState == currentState)
                .filter(rule -> validateTransitionCondition(process, rule))
                // 终止态优化：从可用事件中剔除 START
                .filter(rule -> !(ProcessState.isTerminal(currentState) && rule.event == ProcessEvent.START))
                .map(rule -> rule.event)
                .toArray(ProcessEvent[]::new);
    }
    
    /**
     * 获取鐘舵€佹弿杩?
     * @param statusCode 鐘舵€佺爜
     * @return 鐘舵€佹弿杩?
     */
    public String getStatusDescription(int statusCode) {
        try {
            ProcessState state = ProcessState.fromCode(statusCode);
            return state.getDescription();
        } catch (IllegalArgumentException e) {
            return "Unknown status";
        }
    }
    
    /**
     * 获取浜嬩欢鎻忚堪
     * @param eventName 浜嬩欢鍚嶇О
     * @return 浜嬩欢鎻忚堪
     */
    public String getEventDescription(String eventName) {
        try {
            ProcessEvent event = ProcessEvent.valueOf(eventName);
            return event.getDescription();
        } catch (IllegalArgumentException e) {
            return "鏈煡浜嬩欢";
        }
    }
}
