package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldBorrowReturnRecord;
import java.util.List;

/**
 * 妯″叿棰嗚繕记录服务接口
 * 鎻愪緵妯″叿鍊熺敤鍜屽綊杩樼浉鍏崇殑涓氬姟操作
 */
public interface MoldBorrowReturnRecordService extends IService<MoldBorrowReturnRecord> {
    
    /**
     * 鍊熺敤妯″叿
     * @param record 鍊熺敤记录
     * @return 鏄惁鍊熺敤成功
     */
    boolean borrowMold(MoldBorrowReturnRecord record);
    
    /**
     * 褰掕繕妯″叿
     * @param borrowId 鍊熺敤记录ID
     * @param returnTime 褰掕繕鏃堕棿
     * @param returnRemark 澶囨敞
     * @return 鏄惁褰掕繕成功
     */
    boolean returnMold(Long borrowId, java.time.LocalDateTime returnTime, String returnRemark);
    
    /**
     * 查询用户鏈綊杩樼殑妯″叿记录
     * @param borrowerId 鍊熺敤浜篒D
     * @return 鏈綊杩樿褰曞垪琛?     */
    List<MoldBorrowReturnRecord> getUnreturnedByBorrower(Long borrowerId);
    
    /**
     * 查询妯″叿鐨勫€熺敤鍘嗗彶
     * @param moldId 妯″叿ID
     * @return 鍊熺敤鍘嗗彶鍒楄〃
     */
    List<MoldBorrowReturnRecord> getHistoryByMoldId(Long moldId);
    
    /**
     * 更新鍊熺敤记录淇℃伅
     * @param record 鍊熺敤记录淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateRecord(MoldBorrowReturnRecord record);
    
    /**
     * 删除鍊熺敤记录
     * @param id 记录ID
     * @return 鏄惁删除成功
     */
    boolean deleteRecord(Long id);
    
    /**
     * 获取鍊熺敤褰掕繕缁熻淇℃伅
     * @return 缁熻淇℃伅
     */
    java.util.Map<String, Object> getBorrowReturnStats();
    
    /**
     * 获取鎵€鏈夊€熺敤褰掕繕记录
     * @return 记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> getAllRecords();
    
    /**
     * 鏍规嵁妯″叿ID获取鍊熺敤褰掕繕记录鍒楄〃
     * @param moldId 妯″叿ID
     * @return 鍊熺敤褰掕繕记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> getByMoldId(Long moldId);
    
    /**
     * 鏍规嵁鍊熺敤浜篒D获取鍊熺敤褰掕繕记录鍒楄〃
     * @param borrowerId 鍊熺敤浜篒D
     * @return 鍊熺敤褰掕繕记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> getByBorrowerId(Long borrowerId);
    
    /**
     * 获取鏈綊杩樼殑记录鍒楄〃
     * @return 鏈綊杩樿褰曞垪琛?     */
    List<MoldBorrowReturnRecord> getUnreturnedRecords();
    
    /**
     * 获取閫炬湡鏈綊杩樼殑记录鍒楄〃
     * @return 閫炬湡记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> getOverdueRecords();
    
    /**
     * 鏍规嵁日期鑼冨洿查询记录
     * @param startDate 开€濮嬫棩鏈?     * @param endDate 缁撴潫日期
     * @return 记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> getByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    /**
     * 创建鏂扮殑鍊熺敤记录
     * @param record 鍊熺敤记录淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createBorrowRecord(MoldBorrowReturnRecord record);
    
    /**
     * 褰掕繕妯″叿
     * @param id 记录ID
     * @param returnCondition 褰掕繕鐘舵€佹弿杩?     * @return 鏄惁褰掕繕成功
     */
    boolean returnMold(Long id, String returnCondition);
}
