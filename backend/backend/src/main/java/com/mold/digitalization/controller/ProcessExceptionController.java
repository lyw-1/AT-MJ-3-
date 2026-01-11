package com.mold.digitalization.controller;

import com.mold.digitalization.dto.ResponseDTO;
import com.mold.digitalization.entity.ExceptionStatistics;
import com.mold.digitalization.entity.ProcessException;
import com.mold.digitalization.service.ProcessExceptionService;
import com.mold.digitalization.common.exception.BusinessException;
import com.mold.digitalization.enums.ErrorCodeEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 流程异常记录控制器
 * 处理流程异常记录相关的HTTP请求
 */
@RestController
@RequestMapping("/api/process-exceptions")
public class ProcessExceptionController extends BaseController {
    
    @Autowired
    private ProcessExceptionService processExceptionService;
    
    /**
     * 鏍规嵁开傚父记录ID获取开傚父淇℃伅
     * @param id 开傚父记录ID
     * @return 开傚父淇℃伅
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取异常记录")
    public ResponseDTO<ProcessException> getExceptionById(@PathVariable Long id) {
        ProcessException exception = processExceptionService.getById(id);
        if (exception != null) {
            return ResponseDTO.success(exception);
        } else {
            // 统一异常风格：使用业务异常 + 错误枚举
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
    }
    
    /**
     * 鏍规嵁娴佺▼ID获取开傚父记录鍒楄〃
     * @param processId 娴佺▼ID
     * @return 开傚父记录鍒楄〃
     */
    @GetMapping("/process/{processId}")
    @ApiOperation("根据流程ID获取异常记录列表")
    public ResponseDTO<List<ProcessException>> getExceptionsByProcessId(@PathVariable Long processId) {
        List<ProcessException> exceptions = processExceptionService.getExceptionsByProcessId(processId);
        return ResponseDTO.success(exceptions);
    }
    
    /**
     * 鏍规嵁开傚父绫诲瀷获取开傚父记录鍒楄〃
     * @param exceptionType 开傚父绫诲瀷
     * @return 开傚父记录鍒楄〃
     */
    @GetMapping("/type/{exceptionType}")
    @ApiOperation("根据异常类型获取异常记录列表")
    public ResponseDTO<List<ProcessException>> getExceptionsByType(@PathVariable Integer exceptionType) {
        List<ProcessException> exceptions = processExceptionService.getExceptionsByExceptionType(exceptionType);
        return ResponseDTO.success(exceptions);
    }
    
    /**
     * 鏍规嵁涓ラ噸绋嬪害获取开傚父记录鍒楄〃
     * @param severity 涓ラ噸绋嬪害
     * @return 开傚父记录鍒楄〃
     */
    @GetMapping("/severity/{severity}")
    @ApiOperation("根据严重程度获取异常记录列表")
    public ResponseDTO<List<ProcessException>> getExceptionsBySeverity(@PathVariable Integer severity) {
        List<ProcessException> exceptions = processExceptionService.getExceptionsBySeverity(severity);
        return ResponseDTO.success(exceptions);
    }
    
    /**
     * 鏍规嵁澶勭悊鐘舵€佽幏鍙栧紓甯歌褰曞垪琛?
     * @param handlingStatus 澶勭悊状态
     * @return 开傚父记录鍒楄〃
     */
    @GetMapping("/status/{handlingStatus}")
    @ApiOperation("根据处理状态获取异常记录列表")
    public ResponseDTO<List<ProcessException>> getExceptionsByStatus(@PathVariable Integer handlingStatus) {
        List<ProcessException> exceptions = processExceptionService.getExceptionsByHandlingStatus(handlingStatus);
        return ResponseDTO.success(exceptions);
    }
    
    /**
     * 获取鏈鐞嗙殑开傚父记录鍒楄〃
     * @return 鏈鐞嗙殑开傚父记录鍒楄〃
     */
    @GetMapping("/unhandled")
    @ApiOperation("获取未处理的异常记录列表")
    public ResponseDTO<List<ProcessException>> getUnhandledExceptions() {
        List<ProcessException> exceptions = processExceptionService.getUnhandledExceptions();
        return ResponseDTO.success(exceptions);
    }
    
    /**
     * 获取楂樹弗閲嶇▼搴︾殑开傚父记录鍒楄〃
     * @return 楂樹弗閲嶇▼搴︾殑开傚父记录鍒楄〃
     */
    @GetMapping("/high-severity")
    @ApiOperation("获取高严重度的异常记录列表")
    public ResponseDTO<List<ProcessException>> getHighSeverityExceptions() {
        List<ProcessException> exceptions = processExceptionService.getHighSeverityExceptions();
        return ResponseDTO.success(exceptions);
    }
    
    /**
     * 创建开傚父记录
     * @param processException 开傚父记录淇℃伅
     * @return 创建结果
     */
    @PostMapping
    @ApiOperation("创建异常记录")
    public ResponseDTO<ProcessException> createException(@RequestBody ProcessException processException) {
        boolean created = processExceptionService.createProcessException(processException);
        if (created) {
            return ResponseDTO.success(processException);
        } else {
            // 创建失败归类为通用操作失败
            throw new BusinessException(ErrorCodeEnum.OPERATION_FAILED);
        }
    }
    
    /**
     * 更新开傚父记录
     * @param id 开傚父记录ID
     * @param processException 开傚父记录淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @ApiOperation("更新异常记录")
    public ResponseDTO<ProcessException> updateException(@PathVariable Long id, @RequestBody ProcessException processException) {
        processException.setId(id);
        boolean updated = processExceptionService.updateProcessException(processException);
        if (updated) {
            return ResponseDTO.success(processException);
        } else {
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
    }
    
    /**
     * 删除开傚父记录
     * @param id 开傚父记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除异常记录")
    public ResponseDTO<Void> deleteException(@PathVariable Long id) {
        boolean deleted = processExceptionService.deleteProcessException(id);
        if (deleted) {
            return ResponseDTO.success(null);
        } else {
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
    }
    
    /**
     * 澶勭悊开傚父
     * @param id 开傚父记录ID
     * @param handlerId 澶勭悊浜哄憳ID
     * @param handlingResult 澶勭悊结果
     * @return 澶勭悊结果
     */
    @PutMapping("/{id}/handle")
    @ApiOperation("处理异常")
    public ResponseDTO<Void> handleException(
            @PathVariable Long id,
            @RequestParam(required = false) Long handlerId,
            @RequestParam(required = false) String handlingResult) {
        
        // 参数验证失败统一抛出业务异常
        if (handlerId == null) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR);
        }
        if (handlingResult == null || handlingResult.trim().isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR);
        }
        
        boolean handled = processExceptionService.handleException(id, handlerId, handlingResult);
        if (handled) {
            return ResponseDTO.success(null);
        } else {
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
    }
    
    /**
     * 更新开傚父澶勭悊状态
     * @param id 开傚父记录ID
     * @param handlingStatus 澶勭悊状态
     * @param handlerId 澶勭悊浜哄憳ID
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    @ApiOperation("更新异常处理状态")
    public ResponseDTO<Void> updateExceptionStatus(
            @PathVariable Long id,
            @RequestParam Integer handlingStatus,
            @RequestParam(required = false) Long handlerId) {
        
        boolean updated = processExceptionService.updateExceptionHandlingStatus(id, handlingStatus, handlerId);
        if (updated) {
            return ResponseDTO.success(null);
        } else {
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
    }
    
    /**
     * 获取鎵€鏈夊紓甯歌褰曞垪琛?
     * @return 开傚父记录鍒楄〃
     */
    @GetMapping
    @ApiOperation("Get all exceptions")
    public ResponseDTO<List<ProcessException>> getAllExceptions() {
        List<ProcessException> exceptions = processExceptionService.getAllProcessExceptions();
        return ResponseDTO.success(exceptions);
    }
    
    /**
     * 缁熻鎸囧畾娴佺▼鐨勫紓甯告暟閲?
     * @param processId 娴佺▼ID
     * @return 开傚父鏁伴噺
     */
    @GetMapping("/process/{processId}/count")
    @ApiOperation("Count exceptions by process id")
    public ResponseDTO<Integer> countExceptionsByProcessId(@PathVariable Long processId) {
        int count = processExceptionService.countExceptionsByProcessId(processId);
        return ResponseDTO.success(count);
    }
    
    /**
     * 缁熻鏈鐞嗙殑开傚父鏁伴噺
     * @return 鏈鐞嗙殑开傚父鏁伴噺
     */
    @GetMapping("/unhandled/count")
    @ApiOperation("缁熻鏈鐞嗙殑开傚父鏁伴噺")
    public ResponseDTO<Integer> countUnhandledExceptions() {
        int count = processExceptionService.countUnhandledExceptions();
        return ResponseDTO.success(count);
    }
    
    /**
     * 获取开傚父缁熻淇℃伅
     * @return 开傚父缁熻淇℃伅
     */
    @GetMapping("/statistics")
    @ApiOperation("获取开傚父缁熻淇℃伅")
    public ResponseDTO<ExceptionStatistics> getExceptionStatistics() {
        ExceptionStatistics statistics = processExceptionService.getExceptionStatistics();
        return ResponseDTO.success(statistics);
    }
    
    /**
     * 删除鎸囧畾娴佺▼鐨勬墍鏈夊紓甯歌褰?
     * @param processId 娴佺▼ID
     * @return 删除结果
     */
    @DeleteMapping("/process/{processId}")
    @ApiOperation("Delete exceptions by process id")
    public ResponseDTO<Void> deleteExceptionsByProcessId(@PathVariable Long processId) {
        boolean deleted = processExceptionService.deleteExceptionsByProcessId(processId);
        if (deleted) {
            return ResponseDTO.success(null);
        } else {
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
    }
}
