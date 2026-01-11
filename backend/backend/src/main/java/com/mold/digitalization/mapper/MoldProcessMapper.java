package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 妯″叿鍔犲伐娴佺▼Mapper接口
 * 鎻愪緵妯″叿鍔犲伐娴佺▼琛ㄧ殑数据搴撴搷浣滄柟娉? */
@Mapper
public interface MoldProcessMapper extends BaseMapper<MoldProcess> {
    
    /**
     * 鏍规嵁娴佺▼浠ｇ爜查询娴佺▼淇℃伅
     * @param processCode 娴佺▼浠ｇ爜
     * @return 娴佺▼淇℃伅
     */
    MoldProcess selectByProcessCode(String processCode);
    
    /**
     * 鏍规嵁妯″叿ID查询娴佺▼鍒楄〃
     * @param moldId 妯″叿ID
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> selectByMoldId(Long moldId);
    
    /**
     * 鏍规嵁鐢熶骇浠诲姟ID查询娴佺▼鍒楄〃
     * @param productionTaskId 鐢熶骇浠诲姟ID
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> selectByProductionTaskId(Long productionTaskId);
    
    /**
     * 鏍规嵁鐘舵€佹煡璇㈡祦绋嬪垪琛?     * @param status 娴佺▼状态     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> selectByStatus(Integer status);
    
    /**
     * 鏍规嵁璐熻矗浜篒D查询娴佺▼鍒楄〃
     * @param responsibleUserId 璐熻矗浜篒D
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> selectByResponsibleUserId(Long responsibleUserId);
    
    /**
     * 鏍规嵁操作浜哄憳ID查询娴佺▼鍒楄〃
     * @param operatorId 操作浜哄憳ID
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> selectByOperatorId(Long operatorId);
    
    /**
     * 鏍规嵁璁惧ID查询娴佺▼鍒楄〃
     * @param equipmentId 璁惧ID
     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> selectByEquipmentId(Long equipmentId);
    
    /**
     * 鏍规嵁浼樺厛绾ф煡璇㈡祦绋嬪垪琛?     * @param priority 浼樺厛绾?     * @return 娴佺▼鍒楄〃
     */
    List<MoldProcess> selectByPriority(Integer priority);

    int insertDev(MoldProcess process);
    
    /**
     * 更新娴佺▼状态     * @param processId 娴佺▼ID
     * @param status 鏂扮姸鎬?     * @return 更新褰卞搷鐨勮鏁?     */
    int updateProcessStatus(@Param("processId") Long processId, @Param("status") Integer status);
    
    /**
     * 更新娴佺▼杩涘害
     * @param processId 娴佺▼ID
     * @param progress 鏂拌繘搴?     * @return 更新褰卞搷鐨勮鏁?     */
    int updateProcessProgress(@Param("processId") Long processId, @Param("progress") Integer progress);
    
    /**
     * 更新实际开€濮嬫椂闂?     * @param processId 娴佺▼ID
     * @param actualStartTime 实际开€濮嬫椂闂?     * @return 更新褰卞搷鐨勮鏁?     */
    int updateActualStartTime(@Param("processId") Long processId, @Param("actualStartTime") java.time.LocalDateTime actualStartTime);
    
    /**
     * 更新实际瀹屾垚鏃堕棿
     * @param processId 娴佺▼ID
     * @param actualEndTime 实际瀹屾垚鏃堕棿
     * @return 更新褰卞搷鐨勮鏁?     */
    int updateActualEndTime(@Param("processId") Long processId, @Param("actualEndTime") java.time.LocalDateTime actualEndTime);
    
    /**
     * 更新实际宸ユ椂
     * @param processId 娴佺▼ID
     * @param actualDuration 实际宸ユ椂
     * @return 更新褰卞搷鐨勮鏁?     */
    int updateActualDuration(@Param("processId") Long processId, @Param("actualDuration") Integer actualDuration);
}
