package com.mold.digitalization.controller;

import com.mold.digitalization.entity.AccessoryProcessRecord;
import com.mold.digitalization.service.AccessoryProcessRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 閰嶄欢鍔犲伐记录Controller
 * 鎻愪緵閰嶄欢鍔犲伐鐩稿叧鐨凥TTP接口
 */
@RestController
@RequestMapping("/api/accessory/process")
public class AccessoryProcessRecordController {
    
    @Autowired
    private AccessoryProcessRecordService accessoryProcessRecordService;
    
    /**
     * 创建鍔犲伐记录
     */
    @PostMapping("/create")
    public boolean create(@RequestBody AccessoryProcessRecord record) {
        return accessoryProcessRecordService.createRecord(record);
    }
    
    /**
     * 鏍规嵁閰嶄欢ID查询鍔犲伐记录
     */
    @GetMapping("/by-accessory/{accessoryId}")
    public List<AccessoryProcessRecord> getByAccessoryId(@PathVariable Long accessoryId) {
        return accessoryProcessRecordService.getByAccessoryId(accessoryId);
    }
    
    /**
     * 鏍规嵁妯″叿ID查询鐩稿叧閰嶄欢鍔犲伐记录
     */
    @GetMapping("/by-mold/{moldId}")
    public List<AccessoryProcessRecord> getByMoldId(@PathVariable Long moldId) {
        return accessoryProcessRecordService.getByMoldId(moldId);
    }
    
    /**
     * 更新鍔犲伐记录状态
     */
    @PutMapping("/update-status")
    public boolean updateStatus(
            @RequestParam Long id,
            @RequestParam Integer status) {
        return accessoryProcessRecordService.updateStatus(id, status);
    }
}