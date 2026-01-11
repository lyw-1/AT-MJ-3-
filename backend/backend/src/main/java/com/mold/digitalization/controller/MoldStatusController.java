package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldStatus;
import com.mold.digitalization.service.MoldStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 妯″叿鐘舵€佹帶鍒跺櫒
 * 澶勭悊妯″叿鐘舵€佺浉鍏崇殑HTTP璇锋眰
 */
@RestController
@RequestMapping("/api/mold-statuses")
public class MoldStatusController extends BaseController {
    
    @Autowired
    private MoldStatusService moldStatusService;
    
    /**
     * 鏍规嵁妯″叿鐘舵€両D获取妯″叿鐘舵€佷俊鎭?
     * @param id 妯″叿鐘舵€両D
     * @return 妯″叿鐘舵€佷俊鎭?
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoldStatus> getMoldStatusById(@PathVariable Long id) {
        MoldStatus moldStatus = moldStatusService.getById(id);
        if (moldStatus != null) {
            return success(moldStatus);
        } else {
            return notFoundGeneric("Mold status not found");
        }
    }
    
    /**
     * 鏍规嵁妯″叿鐘舵€佺紪鐮佽幏鍙栨ā鍏风姸鎬佷俊鎭?
     * @param statusCode 妯″叿鐘舵€佺紪鐮?
     * @return 妯″叿鐘舵€佷俊鎭?
     */
    @GetMapping("/code/{statusCode}")
    public ResponseEntity<MoldStatus> getMoldStatusByCode(@PathVariable String statusCode) {
        MoldStatus moldStatus = moldStatusService.getMoldStatusByCode(statusCode);
        if (moldStatus != null) {
            return success(moldStatus);
        } else {
            return notFoundGeneric("Mold status not found");
        }
    }
    
    /**
     * 创建鏂版ā鍏风姸鎬?
     * @param moldStatus 妯″叿鐘舵€佷俊鎭?
     * @return 创建鐨勬ā鍏风姸鎬佷俊鎭?
     */
    @PostMapping
    public ResponseEntity<MoldStatus> createMoldStatus(@RequestBody MoldStatus moldStatus) {
        boolean created = moldStatusService.createMoldStatus(moldStatus);
        if (created) {
            return created(moldStatus);
        } else {
            return notFoundGeneric("Create mold status failed");
        }
    }
    
    /**
     * 更新妯″叿鐘舵€佷俊鎭?
     * @param id 妯″叿鐘舵€両D
     * @param moldStatus 妯″叿鐘舵€佷俊鎭?
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMoldStatus(@PathVariable Long id, @RequestBody MoldStatus moldStatus) {
        moldStatus.setId(id);
        boolean updated = moldStatusService.updateMoldStatus(moldStatus);
        if (updated) {
            return success();
        } else {
            return notFoundGeneric("Mold status not found or update failed");
        }
    }
    
    /**
     * 删除妯″叿状态
     * @param id 妯″叿鐘舵€両D
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoldStatus(@PathVariable Long id) {
        boolean deleted = moldStatusService.deleteMoldStatus(id);
        if (deleted) {
            return success();
        } else {
            return notFoundGeneric("Mold status not found or delete failed");
        }
    }
    
    /**
     * 获取鎵€鏈夋ā鍏风姸鎬佸垪琛?
     * @return 妯″叿鐘舵€佸垪琛?
     */
    @GetMapping
    public ResponseEntity<List<MoldStatus>> getAllMoldStatuses() {
        try {
            List<MoldStatus> moldStatuses = moldStatusService.getAllMoldStatuses();
            return success(moldStatuses);
        } catch (Exception e) {
            return success(java.util.Collections.emptyList());
        }
    }
}
