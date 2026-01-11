package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.MoldProcessMapper;
import com.mold.digitalization.mapper.ProcessStatusHistoryMapper;
import com.mold.digitalization.mapper.ProcessExceptionMapper;
import com.mold.digitalization.mapper.InspectionResultMapper;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.entity.ProcessException;
import com.mold.digitalization.entity.InspectionResult;
import com.mold.digitalization.enums.ProcessStatusEnum;
import com.mold.digitalization.service.ProcessStateMachineService;
import com.mold.digitalization.engine.ProcessStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 流程状态机核心服务实现
 * 实现状态迁移、历史记录与异常处理等核心业务逻辑
 */
@Service
public class ProcessStateMachineServiceImpl extends ServiceImpl<MoldProcessMapper, MoldProcess> implements ProcessStateMachineService {
    
    @Autowired
    private MoldProcessMapper moldProcessMapper;
    
    @Autowired
    private ProcessStatusHistoryMapper processStatusHistoryMapper;
    
    @Autowired
    private ProcessExceptionMapper processExceptionMapper;
    
    @Autowired
    private InspectionResultMapper inspectionResultMapper;
    
    @Autowired
    private ProcessStateMachine processStateMachine;
    
        // Transition rules for process state machine
        private static final List<TransitionRule> TRANSITION_RULES = Arrays.asList(
                new TransitionRule(ProcessStatusEnum.PENDING.getCode(), "START", ProcessStatusEnum.PREPARING.getCode()),
                new TransitionRule(ProcessStatusEnum.PREPARING.getCode(), "PROCESS", ProcessStatusEnum.PROCESSING.getCode()),
                new TransitionRule(ProcessStatusEnum.PROCESSING.getCode(), "PAUSE", ProcessStatusEnum.PAUSED.getCode()),
                new TransitionRule(ProcessStatusEnum.PAUSED.getCode(), "RESUME", ProcessStatusEnum.PROCESSING.getCode()),
                new TransitionRule(ProcessStatusEnum.PROCESSING.getCode(), "COMPLETE", ProcessStatusEnum.COMPLETED.getCode()),
                new TransitionRule(ProcessStatusEnum.PAUSED.getCode(), "COMPLETE", ProcessStatusEnum.COMPLETED.getCode()),
                new TransitionRule(null, "CANCEL", ProcessStatusEnum.CANCELLED.getCode()),
                new TransitionRule(null, "EXCEPTION", ProcessStatusEnum.EXCEPTION.getCode())
        );
    
    @Override
    @Transactional
    public boolean triggerStateTransition(Long processId, String event, Long operatorId) {
        return triggerStateTransition(processId, event, operatorId, null);
    }
    
    @Override
    @Transactional
    public boolean triggerStateTransition(Long processId, String event, Long operatorId, Object parameters) {
        try {
            ProcessStateMachine.ProcessEvent e = ProcessStateMachine.ProcessEvent.valueOf(String.valueOf(event).toUpperCase());
            String operator = operatorId != null ? String.valueOf(operatorId) : null;
            String remark = parameters instanceof String ? (String) parameters : null;
            return processStateMachine.triggerTransition(processId, e, operator, remark);
        } catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean validateTransitionConditions(Long processId, String event) {
        try {
            ProcessStateMachine.ProcessEvent e = ProcessStateMachine.ProcessEvent.valueOf(String.valueOf(event).toUpperCase());
            return processStateMachine.canTransition(Long.valueOf(processId), e);
        } catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public List<String> getAllowedEvents(Long processId) {
        try {
            ProcessStateMachine.ProcessEvent[] events = processStateMachine.getAvailableEvents(processId);
            List<String> list = new ArrayList<>();
            for (ProcessStateMachine.ProcessEvent ev : events) list.add(ev.name());
            return list;
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<ProcessStatusHistory> getStateTransitionHistory(Long processId) {
        QueryWrapper<ProcessStatusHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        queryWrapper.orderByAsc("transition_time");
        return processStatusHistoryMapper.selectList(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean recordStateTransition(Long processId, Integer fromState, Integer toState, 
                                       String event, Long operatorId, String description) {
        ProcessStatusHistory history = new ProcessStatusHistory();
        history.setProcessId(processId);
        history.setFromStatus(fromState);
        history.setToStatus(toState);
        history.setEvent(event);
        history.setOperatorId(operatorId);
        history.setDescription(description);
        history.setTransitionTime(LocalDateTime.now());
        
        return processStatusHistoryMapper.insert(history) > 0;
    }
    
    @Override
    @Transactional
    public boolean handleProcessException(Long processId, String exceptionCode, 
                                        String exceptionMessage, Long operatorId) {
    // 记录开傚父淇℃伅
        ProcessException exception = new ProcessException();
        exception.setProcessId(processId);
        exception.setExceptionCode(exceptionCode);
        exception.setExceptionMessage(exceptionMessage);
        exception.setOperatorId(operatorId);
        exception.setExceptionTime(LocalDateTime.now());
        exception.setResolved(false);
        
        boolean recordSuccess = processExceptionMapper.insert(exception) > 0;
        if (!recordSuccess) {
            return false;
        }
        
    // Trigger exception state transition
    return triggerStateTransition(processId, "EXCEPTION", operatorId);
    }
    
    @Override
    public List<ProcessException> getProcessExceptions(Long processId) {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        queryWrapper.orderByDesc("exception_time");
        return processExceptionMapper.selectList(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean recordInspectionResult(Long processId, InspectionResult inspectionResult, Long inspectorId) {
        inspectionResult.setProcessId(processId);
        inspectionResult.setInspectorId(inspectorId);
        inspectionResult.setInspectionTime(LocalDateTime.now());
        
        return inspectionResultMapper.insert(inspectionResult) > 0;
    }
    
    @Override
    public List<InspectionResult> getInspectionResults(Long processId) {
        QueryWrapper<InspectionResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        queryWrapper.orderByDesc("inspection_time");
        return inspectionResultMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean canStartProcess(Long processId) {
        MoldProcess process = getById(processId);
        return process != null && canTransition(process.getCurrentStatus(), "START");
    }
    
    @Override
    public boolean canPauseProcess(Long processId) {
        MoldProcess process = getById(processId);
        return process != null && canTransition(process.getCurrentStatus(), "PAUSE");
    }
    
    @Override
    public boolean canResumeProcess(Long processId) {
        MoldProcess process = getById(processId);
        return process != null && canTransition(process.getCurrentStatus(), "RESUME");
    }
    
    @Override
    public boolean canCompleteProcess(Long processId) {
        MoldProcess process = getById(processId);
        return process != null && canTransition(process.getCurrentStatus(), "COMPLETE");
    }
    
    @Override
    public boolean canCancelProcess(Long processId) {
        MoldProcess process = getById(processId);
        return process != null && canTransition(process.getCurrentStatus(), "CANCEL");
    }
    
    @Override
    public ProcessStatusInfo getCurrentProcessStatus(Long processId) {
        MoldProcess process = getById(processId);
        if (process == null) {
            return null;
        }
        
        ProcessStatusInfo statusInfo = new ProcessStatusInfo();
        statusInfo.setCurrentState(process.getCurrentStatus());
        statusInfo.setStateName(getStatusName(process.getCurrentStatus()));
        statusInfo.setStateDescription(getStatusDescription(process.getCurrentStatus()));
        statusInfo.setAllowedEvents(getAllowedEvents(processId));
        
        // 计算在当前状态的持续时长
        if (process.getActualStartTime() != null) {
            LocalDateTime startTime = process.getActualStartTime();
            LocalDateTime now = LocalDateTime.now();
            statusInfo.setDurationInCurrentState(ChronoUnit.MILLIS.between(startTime, now));
        }
        
        return statusInfo;
    }
    
    // 内部辅助方法
    private TransitionRule findMatchingTransitionRule(Integer currentState, String event) {
        for (TransitionRule rule : TRANSITION_RULES) {
            if (rule.matches(currentState, event)) {
                return rule;
            }
        }
        return null;
    }
    
    private boolean canTransition(Integer currentState, String event) {
        return findMatchingTransitionRule(currentState, event) != null;
    }
    
    private void updateProcessTimestamps(MoldProcess process, String event) {
        LocalDateTime now = LocalDateTime.now();
        
        switch (event) {
            case "START":
                process.setActualStartTime(now);
                break;
            case "COMPLETE":
                process.setActualEndTime(now);
                process.setCurrentProgress(100);
                break;
            case "PAUSE":
                process.setLastPauseTime(now);
                break;
            case "RESUME":
                // 可选：记录恢复时间或计算暂停时长
                break;
            default:
                break;
        }
    }
    
    private String generateTransitionDescription(Integer fromState, Integer toState, String event) {
        String fromStatusName = getStatusName(fromState);
        String toStatusName = getStatusName(toState);
        return String.format("State changed from %s to %s by event %s", fromStatusName, toStatusName, event);
    }
    
    private String getStatusName(Integer status) {
        if (status == null) return "未知";
        try {
            return ProcessStateMachine.ProcessState.fromCode(status).getDescription();
        } catch (Exception ex) {
            return "Unknown";
        }
    }
    
    private String getStatusDescription(Integer status) {
        if (status == null) return "Unknown status description";
        try {
            return ProcessStateMachine.ProcessState.fromCode(status).getDescription();
        } catch (Exception ex) {
            return "Unknown status description";
        }
    }
    
    private void handleSpecialEvents(Long processId, String event, Object parameters, Long operatorId) {
        // 处理事件的特殊业务逻辑
        if ("EXCEPTION".equals(event)) {
            // 异常处理逻辑
        } else if ("COMPLETE".equals(event)) {
            // 完成处理逻辑
        }
    }
    
    // 迁移规则
    private static class TransitionRule {
        private final Integer fromState; // null 表示任意状态
        private final String event;
        private final Integer toState;
        
        public TransitionRule(Integer fromState, String event, Integer toState) {
            this.fromState = fromState;
            this.event = event;
            this.toState = toState;
        }
        
        public boolean matches(Integer currentState) {
            return fromState == null || fromState.equals(currentState);
        }
        
        public boolean matches(Integer currentState, String event) {
            return matches(currentState) && this.event.equals(event);
        }
        
        public Integer getFromState() { return fromState; }
        public String getEvent() { return event; }
        public Integer getToState() { return toState; }
    }
}
