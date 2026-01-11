package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldBorrowReturnRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 妯″叿棰嗚繕记录Mapper接口
 * 缁ф壙BaseMapper骞舵彁渚涜嚜瀹氫箟查询方法
 */
public interface MoldBorrowReturnRecordMapper extends BaseMapper<MoldBorrowReturnRecord> {

    /**
     * 鏍规嵁妯″叿ID查询棰嗚繕记录
     * @param moldId 妯″叿ID
     * @return 棰嗚繕记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> selectByMoldId(@Param("moldId") Long moldId);

    /**
     * 鏍规嵁鍊熺敤浜篒D查询棰嗚繕记录
     * @param borrowerId 鍊熺敤浜篒D
     * @return 棰嗚繕记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> selectByBorrowerId(@Param("borrowerId") Long borrowerId);

    /**
     * 鏍规嵁褰掕繕鐘舵€佹煡璇㈣褰?     * @param returnStatus 褰掕繕鐘舵€侊細0-鏈綊杩橈紝1-宸插綊杩橈紝2-閫炬湡
     * @return 棰嗚繕记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> selectByReturnStatus(@Param("returnStatus") Integer returnStatus);

    /**
     * 查询閫炬湡鏈綊杩樼殑记录
     * @param currentTime 褰撳墠鏃堕棿
     * @return 閫炬湡记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> selectOverdueRecords(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 鏍规嵁鍊熺敤日期鑼冨洿查询记录
     * @param startDate 开€濮嬫棩鏈?     * @param endDate 缁撴潫日期
     * @return 棰嗚繕记录鍒楄〃
     */
    List<MoldBorrowReturnRecord> selectByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * 更新褰掕繕淇℃伅
     * @param id 记录ID
     * @param actualReturnDate 实际褰掕繕日期
     * @param returnStatus 褰掕繕状态     * @param returnCondition 褰掕繕鏃剁姸鎬佹弿杩?     * @return 更新结果
     */
    int updateReturnInfo(@Param("id") Long id, @Param("actualReturnDate") LocalDateTime actualReturnDate,
                         @Param("returnStatus") Integer returnStatus, @Param("returnCondition") String returnCondition);
}
