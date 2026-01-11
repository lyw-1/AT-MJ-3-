package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.ProcessExceptionMapper;
import com.mold.digitalization.entity.ExceptionStatistics;
import com.mold.digitalization.entity.ProcessException;
import com.mold.digitalization.enums.ExceptionSeverityEnum;
import com.mold.digitalization.enums.ExceptionTypeEnum;
import com.mold.digitalization.enums.HandlingStatusEnum;
import com.mold.digitalization.service.ProcessExceptionService;
import com.mold.digitalization.common.exception.BusinessException;
import com.mold.digitalization.enums.ErrorCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 娴佺▼开傚父记录服务实现绫? * 实现娴佺▼开傚父记录鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
@Service
public class ProcessExceptionServiceImpl extends ServiceImpl<ProcessExceptionMapper, ProcessException> implements ProcessExceptionService {
    
    @Autowired
    private ProcessExceptionMapper processExceptionMapper;
    
    @Override
    public List<ProcessException> getExceptionsByProcessId(Long processId) {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId)
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<ProcessException> getExceptionsByExceptionType(Integer exceptionType) {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exception_type", exceptionType)
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<ProcessException> getExceptionsBySeverity(Integer severity) {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("severity", severity)
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<ProcessException> getExceptionsByHandlingStatus(Integer handlingStatus) {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("handling_status", handlingStatus)
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<ProcessException> getExceptionsByHandlerId(Long handlerId) {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("handler_id", handlerId)
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    public int countExceptionsByProcessId(Long processId) {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        return Math.toIntExact(count(queryWrapper));
    }
    
    @Override
    public List<ProcessException> getHighSeverityExceptions() {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("severity", ExceptionSeverityEnum.SEVERE.getCode())
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    public List<ProcessException> getUnhandledExceptions() {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("handling_status", HandlingStatusEnum.UNHANDLED.getCode())
                   .orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean createProcessException(ProcessException processException) {
        return save(processException);
    }
    
    @Override
    @Transactional
    public boolean updateProcessException(ProcessException processException) {
        return updateById(processException);
    }
    
    @Override
    @Transactional
    public boolean deleteProcessException(Long exceptionId) {
        return removeById(exceptionId);
    }
    
    @Override
    @Transactional
    public boolean handleException(Long exceptionId, Long handlerId, String handlingResult) {
        ProcessException exception = getById(exceptionId);
        if (exception == null) {
            return false;
        }
        
        exception.setHandlingStatus(HandlingStatusEnum.HANDLING.getCode());
        exception.setHandlerId(handlerId);
        exception.setHandlingResult(handlingResult);
        exception.setHandlingStartTime(LocalDateTime.now());
        
        return updateById(exception);
    }
    
    @Override
    public List<ProcessException> getAllProcessExceptions() {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return list(queryWrapper);
    }
    
    @Override
    @Transactional
    public boolean updateExceptionHandlingStatus(Long exceptionId, Integer handlingStatus, Long handlerId) {
        ProcessException exception = getById(exceptionId);
        if (exception == null) {
            return false;
        }
        // 参数校验：状态码必须合法
        HandlingStatusEnum targetStatus = HandlingStatusEnum.getByCode(handlingStatus);
        if (handlingStatus == null || targetStatus == null) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR);
        }

        // 当目标为处理中或已处理时，必须提供处理人
        if ((targetStatus == HandlingStatusEnum.HANDLING || targetStatus == HandlingStatusEnum.HANDLED)
                && (handlerId == null)) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR);
        }

        Integer currentStatusCode = exception.getHandlingStatus();
        HandlingStatusEnum currentStatus = HandlingStatusEnum.getByCode(currentStatusCode);

        // 允许的状态流转：UNHANDLED -> HANDLING -> HANDLED
        if (!isValidTransition(currentStatus, targetStatus)) {
            throw new BusinessException(ErrorCodeEnum.INVALID_OPERATION);
        }

        // 设置状态与处理人，若设置为已处理，补全结束时间
        exception.setHandlingStatus(handlingStatus);
        exception.setHandlerId(handlerId);
        if (targetStatus == HandlingStatusEnum.HANDLED) {
            exception.setHandlingEndTime(LocalDateTime.now());
        }

        return updateById(exception);
    }

    /**
     * 校验处理状态的合法流转
     * 允许：UNHANDLED -> HANDLING -> HANDLED；
     * 不允许倒退或跨越（如 UNHANDLED 直接到 HANDLED）。
     */
    private boolean isValidTransition(HandlingStatusEnum current, HandlingStatusEnum target) {
        if (current == null) {
            // 若当前为空（历史数据缺失），只允许设置为 UNHANDLED 或 HANDLING
            return target == HandlingStatusEnum.UNHANDLED || target == HandlingStatusEnum.HANDLING;
        }
        if (current == target) {
            // 同状态设置认为合法（幂等）
            return true;
        }
        switch (current) {
            case UNHANDLED:
                return target == HandlingStatusEnum.HANDLING; // 只能进入处理中
            case HANDLING:
                return target == HandlingStatusEnum.HANDLED; // 只能进入已处理
            case HANDLED:
                return false; // 已处理后不允许再变更
            default:
                return false;
        }
    }
    
    @Override
    public int countUnhandledExceptions() {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("handling_status", HandlingStatusEnum.UNHANDLED.getCode());
        return Math.toIntExact(count(queryWrapper));
    }
    
    @Override
    @Transactional
    public boolean deleteExceptionsByProcessId(Long processId) {
        QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", processId);
        return remove(queryWrapper);
    }
    
    @Override
    public com.mold.digitalization.entity.ExceptionStatistics getExceptionStatistics() {
        com.mold.digitalization.entity.ExceptionStatistics statistics = new com.mold.digitalization.entity.ExceptionStatistics();
        
        // 缁熻鎬绘暟
        statistics.setTotalExceptions(Math.toIntExact(count()));
        
    // 缁熻鏈鐞嗗紓甯告暟閲?
    QueryWrapper<ProcessException> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("handling_status", HandlingStatusEnum.UNHANDLED.getCode());
        statistics.setUnhandledCount(Math.toIntExact(count(queryWrapper)));
        
    // 缁熻宸插鐞嗗紓甯告暟閲?
    queryWrapper.clear();
    queryWrapper.eq("handling_status", HandlingStatusEnum.HANDLED.getCode());
        statistics.setHandledCount(Math.toIntExact(count(queryWrapper)));
        
    // 缁熻楂樹弗閲嶇▼搴﹀紓甯告暟閲?
    queryWrapper.clear();
    queryWrapper.ge("severity", ExceptionSeverityEnum.SEVERE.getCode());
        statistics.setSeriousCount(Math.toIntExact(count(queryWrapper)));
        
    // 缁熻鏅€氫弗閲嶇▼搴﹀紓甯告暟閲?
    queryWrapper.clear();
    queryWrapper.lt("severity", ExceptionSeverityEnum.SEVERE.getCode());
        statistics.setNormalCount(Math.toIntExact(count(queryWrapper)));
        
        return statistics;
    }
}
