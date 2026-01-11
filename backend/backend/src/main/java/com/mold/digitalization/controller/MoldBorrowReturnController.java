package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldBorrowReturnRecord;
import com.mold.digitalization.service.MoldBorrowReturnRecordService;
import com.mold.digitalization.service.MoldAccessoryInfoService;
import com.mold.digitalization.service.MoldArchiveService;
import com.mold.digitalization.service.MoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 妯″叿棰嗚繕记录控制鍣?
 * 澶勭悊妯″叿鍊熺敤褰掕繕鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/mold-borrow-return")
public class MoldBorrowReturnController extends BaseController {

    @Autowired
    private MoldBorrowReturnRecordService moldBorrowReturnRecordService;
    @Autowired
    private MoldAccessoryInfoService moldAccessoryInfoService;
    @Autowired
    private MoldArchiveService moldArchiveService;
    @Autowired
    private MoldService moldService;

    /**
     * 鏍规嵁记录ID获取鍊熺敤褰掕繕记录
     * @param id 记录ID
     * @return 鍊熺敤褰掕繕记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoldBorrowReturnRecord> getRecordById(@PathVariable Long id) {
        MoldBorrowReturnRecord record = moldBorrowReturnRecordService.getById(id);
        if (record != null) {
            return success(record);
        } else {
            return notFoundGeneric("Record not found");
        }
    }

    @PostMapping("/borrow-with-accessories")
    public ResponseEntity<Void> borrowWithAccessories(@RequestBody Map<String, Object> payload) {
        String moldCode = payload.get("moldCode") != null ? payload.get("moldCode").toString() : null;
        @SuppressWarnings("unchecked")
        List<String> accessorySeqCodes = (List<String>) payload.getOrDefault("accessorySeqCodes", java.util.Collections.emptyList());
        boolean borrowAccessoriesOnly = payload.get("borrowAccessoriesOnly") != null && Boolean.parseBoolean(payload.get("borrowAccessoriesOnly").toString());
        String borrowerDepartment = payload.get("borrowerDepartment") != null ? payload.get("borrowerDepartment").toString() : null;
        String borrowerName = payload.get("borrowerName") != null ? payload.get("borrowerName").toString() : null;
        Long borrowerId = payload.get("borrowerId") != null ? Long.valueOf(payload.get("borrowerId").toString()) : null;
        String purpose = payload.get("purpose") != null ? payload.get("purpose").toString() : "领用";
        java.time.LocalDateTime expectedReturnDate = payload.get("expectedReturnDate") != null ? java.time.LocalDateTime.parse(payload.get("expectedReturnDate").toString()) : java.time.LocalDateTime.now().plusDays(7);

        if (moldCode == null && accessorySeqCodes.isEmpty()) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(null);
        }

        if (!borrowAccessoriesOnly && moldCode != null) {
            MoldBorrowReturnRecord record = new MoldBorrowReturnRecord();
            record.setBorrowDate(LocalDateTime.now());
            record.setExpectedReturnDate(expectedReturnDate);
            record.setBorrowerName(borrowerName);
            record.setBorrowerId(borrowerId);
            record.setPurpose(purpose);
            com.mold.digitalization.entity.Mold mold = moldService.getMoldByCode(moldCode);
            if (mold != null) {
                record.setMoldId(mold.getId());
                record.setMoldCode(moldCode);
            }
            boolean ok = moldBorrowReturnRecordService.createBorrowRecord(record);
            if (!ok) {
                return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            com.mold.digitalization.entity.MoldArchive archive = moldArchiveService.getByMoldCode(moldCode);
            if (archive != null) {
                archive.setStatusTracking(borrowerDepartment);
                archive.setHandler(borrowerName);
                moldArchiveService.updateArchive(archive);
            }
        }

        if (!accessorySeqCodes.isEmpty()) {
            moldAccessoryInfoService.updateStatusAndHandler(accessorySeqCodes, "已领用", borrowerName);
        }

        return success();
    }

    /**
     * 鏍规嵁妯″叿ID获取鍊熺敤褰掕繕记录鍒楄〃
     * @param moldId 妯″叿ID
     * @return 鍊熺敤褰掕繕记录鍒楄〃
     */
    @GetMapping("/mold/{moldId}")
    public ResponseEntity<List<MoldBorrowReturnRecord>> getRecordsByMoldId(@PathVariable Long moldId) {
        List<MoldBorrowReturnRecord> records = moldBorrowReturnRecordService.getByMoldId(moldId);
        return success(records);
    }

    /**
     * 鏍规嵁鍊熺敤浜篒D获取鍊熺敤褰掕繕记录鍒楄〃
     * @param borrowerId 鍊熺敤浜篒D
     * @return 鍊熺敤褰掕繕记录鍒楄〃
     */
    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<List<MoldBorrowReturnRecord>> getRecordsByBorrowerId(@PathVariable Long borrowerId) {
        List<MoldBorrowReturnRecord> records = moldBorrowReturnRecordService.getByBorrowerId(borrowerId);
        return success(records);
    }

    /**
     * 获取鏈綊杩樼殑记录鍒楄〃
     * @return 鏈綊杩樿褰曞垪琛?
     */
    @GetMapping("/unreturned")
    public ResponseEntity<List<MoldBorrowReturnRecord>> getUnreturnedRecords() {
        List<MoldBorrowReturnRecord> records = moldBorrowReturnRecordService.getUnreturnedRecords();
        return success(records);
    }

    /**
     * 获取閫炬湡鏈綊杩樼殑记录鍒楄〃
     * @return 閫炬湡记录鍒楄〃
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<MoldBorrowReturnRecord>> getOverdueRecords() {
        List<MoldBorrowReturnRecord> records = moldBorrowReturnRecordService.getOverdueRecords();
        return success(records);
    }

    /**
     * 鏍规嵁日期鑼冨洿查询记录
     * @param startDate 开€濮嬫棩鏈?
     * @param endDate 缁撴潫日期
     * @return 记录鍒楄〃
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<MoldBorrowReturnRecord>> getRecordsByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        List<MoldBorrowReturnRecord> records = moldBorrowReturnRecordService.getByDateRange(startDate, endDate);
        return success(records);
    }

    /**
     * 创建鏂扮殑鍊熺敤记录
     * @param record 鍊熺敤记录淇℃伅
     * @return 创建鐨勮褰?
     */
    @PostMapping("/borrow")
    public ResponseEntity<?> createBorrowRecord(@RequestBody MoldBorrowReturnRecord record) {
        boolean created = moldBorrowReturnRecordService.createBorrowRecord(record);
        if (created) {
            return success(record);
        } else {
            return internalServerError("Failed to create borrow record");
        }
    }

    /**
     * 褰掕繕妯″叿
     * @param id 记录ID
     * @param returnInfo 褰掕繕淇℃伅锛堝寘鍚綊杩樼姸鎬佸拰褰掕繕鏃剁姸鎬佹弿杩帮級
     * @return 褰掕繕结果
     */
    @PutMapping("/return/{id}")
    public ResponseEntity<Void> returnMold(@PathVariable Long id, @RequestBody Map<String, Object> returnInfo) {
        String returnCondition = returnInfo.get("returnCondition") != null ? returnInfo.get("returnCondition").toString() : null;
        boolean returned = moldBorrowReturnRecordService.returnMold(id, returnCondition);
        if (returned) {
            return success();
        } else {
            return notFoundGeneric("Record not found or return failed");
        }
    }

    /**
     * 更新鍊熺敤记录淇℃伅
     * @param id 记录ID
     * @param record 记录淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecord(@PathVariable Long id, @RequestBody MoldBorrowReturnRecord record) {
        record.setId(id);
        boolean updated = moldBorrowReturnRecordService.updateRecord(record);
        if (updated) {
            return success();
        } else {
            return notFoundGeneric("Record not found or update failed");
        }
    }

    /**
     * 删除鍊熺敤记录
     * @param id 记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        boolean deleted = moldBorrowReturnRecordService.deleteRecord(id);
        if (deleted) {
            return success();
        } else {
            return notFoundGeneric("Record not found or delete failed");
        }
    }

    /**
     * 获取鍊熺敤褰掕繕缁熻淇℃伅
     * @return 缁熻淇℃伅
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getBorrowReturnStats() {
        Map<String, Object> stats = moldBorrowReturnRecordService.getBorrowReturnStats();
        return success(stats);
    }

    /**
     * 获取鎵€鏈夊€熺敤褰掕繕记录
     * @return 记录鍒楄〃
     */
    @GetMapping
    public ResponseEntity<List<MoldBorrowReturnRecord>> getAllRecords() {
        List<MoldBorrowReturnRecord> records = moldBorrowReturnRecordService.getAllRecords();
        return success(records);
    }
}
