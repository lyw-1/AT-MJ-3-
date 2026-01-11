package com.mold.digitalization.controller;

import com.mold.digitalization.entity.Log;
import com.mold.digitalization.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 鏃ュ織控制鍣? * 澶勭悊鏃ュ織鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/logs")
public class LogController extends BaseController {
    
    @Autowired
    private LogService logService;
    
    /**
     * 鏍规嵁鏃ュ織ID获取鏃ュ織淇℃伅
     * @param id 鏃ュ織ID
     * @return 鏃ュ織淇℃伅
     */
    @GetMapping("/{id}")
    public ResponseEntity<Log> getLogById(@PathVariable Long id) {
        Log log = logService.getById(id);
        if (log != null) {
            return success(log);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 鏍规嵁用户ID获取鏃ュ織鍒楄〃
     * @param userId 用户ID
     * @return 鏃ュ織鍒楄〃
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Log>> getLogsByUserId(@PathVariable Long userId) {
        List<Log> logs = logService.getLogsByUserId(userId);
        return success(logs);
    }
    
    /**
     * 鏍规嵁操作绫诲瀷获取鏃ュ織鍒楄〃
     * @param operationType 操作绫诲瀷
     * @return 鏃ュ織鍒楄〃
     */
    @GetMapping("/operation/{operationType}")
    public ResponseEntity<List<Log>> getLogsByOperationType(@PathVariable String operationType) {
        List<Log> logs = logService.getLogsByOperationType(operationType);
        return success(logs);
    }
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿获取鏃ュ織鍒楄〃
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 鏃ュ織鍒楄〃
     */
    @GetMapping("/time-range")
    public ResponseEntity<List<Log>> getLogsByTimeRange(
            @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        List<Log> logs = logService.getLogsByTimeRange(startTime, endTime);
        return success(logs);
    }
    
    /**
     * 创建鏂版棩蹇?     * @param log 鏃ュ織淇℃伅
     * @return 创建鐨勬棩蹇椾俊鎭?     */
    @PostMapping
    public ResponseEntity<Log> createLog(@RequestBody Log log) {
        boolean created = logService.createLog(log);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(log);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 娓呯悊鎸囧畾日期涔嬪墠鐨勬棩蹇?     * @param beforeDate 娓呯悊日期
     * @return 娓呯悊结果
     */
    @DeleteMapping("/clean-before")
    public ResponseEntity<String> cleanLogsBefore(@RequestParam LocalDateTime beforeDate) {
        int cleaned = logService.cleanLogsBefore(beforeDate);
        if (cleaned > 0) {
            return success("鏃ュ織娓呯悊成功");
        } else {
            return badRequest("鏃ュ織娓呯悊失败");
        }
    }
    
    /**
     * 获取鎵€鏈夋棩蹇楀垪琛?     * @return 鏃ュ織鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<Log>> getAllLogs() {
        List<Log> logs = logService.getAllLogs();
        return success(logs);
    }
}
