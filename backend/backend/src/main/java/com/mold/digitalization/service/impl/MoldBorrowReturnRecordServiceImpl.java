package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.MoldBorrowReturnRecordMapper;
import com.mold.digitalization.entity.MoldBorrowReturnRecord;
import com.mold.digitalization.service.MoldBorrowReturnRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 妯″叿棰嗚繕记录服务实现绫?
 * 实现妯″叿鍊熺敤鍜屽綊杩樼浉鍏崇殑涓氬姟逻辑
 */
@Service
public class MoldBorrowReturnRecordServiceImpl extends ServiceImpl<MoldBorrowReturnRecordMapper, MoldBorrowReturnRecord> implements MoldBorrowReturnRecordService {
    
    @Autowired
    private MoldBorrowReturnRecordMapper moldBorrowReturnRecordMapper;
    
    @Override
    public boolean borrowMold(MoldBorrowReturnRecord record) {
        // Set borrow date and initial status
        record.setBorrowDate(LocalDateTime.now());
        record.setActualReturnDate(null);
        record.setReturnStatus(0); // 0: 宸插€熺敤锛屾湭褰掕繕
        // 妫€鏌ユ槸鍚︽湁createTime鍜寀pdateTime瀛楁锛屽鏋滃疄浣撶被涓湁杩欎簺瀛楁
        // 鍚﹀垯可以娉ㄩ噴鎺変笅闈袱琛?       // record.setCreateTime(LocalDateTime.now());
        // record.setUpdateTime(LocalDateTime.now());
        return save(record);
    }
    
    @Override
    public boolean returnMold(Long borrowId, java.time.LocalDateTime returnTime, String returnRemark) {
        MoldBorrowReturnRecord record = new MoldBorrowReturnRecord();
        record.setId(borrowId);
        record.setActualReturnDate(returnTime);
        // record.setMoldCondition(condition); // 濡傛灉实体绫讳腑鏈夎瀛楁
        record.setReturnStatus(1); // 1: 宸插綊杩?       // record.setNotes(returnRemark); // 濡傛灉实体绫讳腑鏈夎瀛楁
        // record.setUpdateTime(LocalDateTime.now()); // 濡傛灉实体绫讳腑鏈夎瀛楁
        return updateById(record);
    }
    
    @Override
    public List<MoldBorrowReturnRecord> getUnreturnedByBorrower(Long borrowerId) {
        QueryWrapper<MoldBorrowReturnRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("borrower_id", borrowerId);
        queryWrapper.eq("status", 0); // 0: 宸插€熺敤锛屾湭褰掕繕
        return moldBorrowReturnRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<MoldBorrowReturnRecord> getHistoryByMoldId(Long moldId) {
        QueryWrapper<MoldBorrowReturnRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mold_id", moldId);
        queryWrapper.orderByDesc("borrow_time");
        return moldBorrowReturnRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean updateRecord(MoldBorrowReturnRecord record) {
        return updateById(record);
    }
    
    @Override
    public boolean deleteRecord(Long id) {
        return removeById(id);
    }
    
    @Override
    public java.util.Map<String, Object> getBorrowReturnStats() {
        // Currently returns a placeholder; implement aggregation later
        return new java.util.HashMap<>();
    }
    
    @Override
    public List<MoldBorrowReturnRecord> getAllRecords() {
        return list();
    }
    
    @Override
    public List<MoldBorrowReturnRecord> getByMoldId(Long moldId) {
        QueryWrapper<MoldBorrowReturnRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mold_id", moldId);
        return list(queryWrapper);
    }
    
    @Override
    public List<MoldBorrowReturnRecord> getByBorrowerId(Long borrowerId) {
        QueryWrapper<MoldBorrowReturnRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("borrower_id", borrowerId);
        return list(queryWrapper);
    }
    
    @Override
    public List<MoldBorrowReturnRecord> getUnreturnedRecords() {
        QueryWrapper<MoldBorrowReturnRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("return_status", 0); // 0: unreturned
        return list(queryWrapper);
    }
    
    @Override
    public List<MoldBorrowReturnRecord> getOverdueRecords() {
        // Currently return empty list; implement overdue calculation later
        return new java.util.ArrayList<>();
    }
    
    @Override
    public List<MoldBorrowReturnRecord> getByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        QueryWrapper<MoldBorrowReturnRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("borrow_date", startDate, endDate);
        return list(queryWrapper);
    }
    
    @Override
    public boolean createBorrowRecord(MoldBorrowReturnRecord record) {
        record.setBorrowDate(java.time.LocalDateTime.now());
        record.setReturnStatus(0); // 0: 宸插€熺敤锛屾湭褰掕繕
        return save(record);
    }
    
    @Override
    public boolean returnMold(Long id, String returnCondition) {
        MoldBorrowReturnRecord record = new MoldBorrowReturnRecord();
        record.setId(id);
        record.setActualReturnDate(java.time.LocalDateTime.now());
        record.setReturnStatus(1); // 1: 宸插綊杩?       // 濡傛灉实体绫讳腑鏈塺eturnCondition瀛楁锛屽彲浠ヨ缃?       // record.setReturnCondition(returnCondition);
        return updateById(record);
    }
}
