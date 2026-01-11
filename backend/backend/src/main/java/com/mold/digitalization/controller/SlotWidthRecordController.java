package com.mold.digitalization.controller;

import com.mold.digitalization.entity.SlotWidthRecord;
import com.mold.digitalization.service.SlotWidthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 妲藉记录Controller
 * 鎻愪緵妲藉娴嬮噺记录鐩稿叧鐨凥TTP接口
 */
@RestController
@RequestMapping("/api/mold/slot-width")
public class SlotWidthRecordController {
    
    @Autowired
    private SlotWidthRecordService slotWidthRecordService;
    
    /**
     * 创建妲藉记录
     */
    @PostMapping("/create")
    public boolean create(@RequestBody SlotWidthRecord record) {
        return slotWidthRecordService.createRecord(record);
    }
    
    /**
     * 鏍规嵁妯″叿ID查询妲藉记录
     */
    @GetMapping("/by-mold/{moldId}")
    public List<SlotWidthRecord> getByMoldId(@PathVariable Long moldId) {
        return slotWidthRecordService.getByMoldId(moldId);
    }
    
    /**
     * 查询涓嶅悎鏍肩殑妲藉记录
     */
    @GetMapping("/unqualified")
    public List<SlotWidthRecord> getUnqualifiedRecords() {
        return slotWidthRecordService.getUnqualifiedRecords();
    }
    
    /**
     * 更新记录状态     */
    @PutMapping("/update-status")
    public boolean updateStatus(
            @RequestParam Long id,
            @RequestParam Integer status) {
        return slotWidthRecordService.updateStatus(id, status);
    }
}
