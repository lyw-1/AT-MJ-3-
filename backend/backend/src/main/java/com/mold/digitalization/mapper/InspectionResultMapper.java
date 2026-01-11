package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.InspectionResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 璐ㄩ噺妫€楠岀粨鏋淢apper接口
 * 鎻愪緵璐ㄩ噺妫€楠岀粨鏋滆〃鐨勬暟鎹簱操作方法
 */
@Mapper
public interface InspectionResultMapper extends BaseMapper<InspectionResult> {
    
    /**
     * 鏍规嵁娴佺▼ID查询妫€楠岀粨鏋滃垪琛?
     * @param processId 娴佺▼ID
     * @return 妫€楠岀粨鏋滃垪琛?
     */
    List<InspectionResult> selectByProcessId(Long processId);
    
    /**
     * 鏍规嵁妫€楠岄」鐩煡璇㈡楠岀粨鏋滃垪琛?
     * @param inspectionItem 妫€楠岄」鐩?
     * @return 妫€楠岀粨鏋滃垪琛?
     */
    List<InspectionResult> selectByInspectionItem(String inspectionItem);
    
    /**
     * 鏍规嵁妫€楠岀粨鏋滄煡璇㈡楠岀粨鏋滃垪琛?
     * @param inspectionResult 妫€楠岀粨鏋?
     * @return 妫€楠岀粨鏋滃垪琛?
     */
    List<InspectionResult> selectByInspectionResult(String inspectionResult);
    
    /**
     * 鏍规嵁妫€楠屼汉鍛業D查询妫€楠岀粨鏋滃垪琛?
     * @param inspectorId 妫€楠屼汉鍛業D
     * @return 妫€楠岀粨鏋滃垪琛?
     */
    List<InspectionResult> selectByInspectorId(Long inspectorId);
    
    /**
     * 查询鍚堟牸鐨勬楠岀粨鏋滃垪琛?
     * @return 鍚堟牸鐨勬楠岀粨鏋滃垪琛?
     */
    List<InspectionResult> selectPassedResults();
    
    /**
     * 查询涓嶅悎鏍肩殑妫€楠岀粨鏋滃垪琛?
     * @return 涓嶅悎鏍肩殑妫€楠岀粨鏋滃垪琛?
     */
    List<InspectionResult> selectFailedResults();
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询妫€楠岀粨鏋滃垪琛?
     * @param startTime 开€濮嬫椂闂?
     * @param endTime 缁撴潫鏃堕棿
     * @return 妫€楠岀粨鏋滃垪琛?
     */
    List<InspectionResult> selectByTimeRange(@Param("startTime") java.time.LocalDateTime startTime, 
                                           @Param("endTime") java.time.LocalDateTime endTime);
    
    /**
     * 缁熻鎸囧畾娴佺▼鐨勬楠岀粨鏋滄暟閲?
     * @param processId 娴佺▼ID
     * @return 妫€楠岀粨鏋滄暟閲?
     */
    int countResultsByProcessId(Long processId);
    
    /**
     * 缁熻鎸囧畾娴佺▼鐨勫悎鏍兼楠岀粨鏋滄暟閲?
     * @param processId 娴佺▼ID
     * @return 鍚堟牸妫€楠岀粨鏋滄暟閲?
     */
    int countPassedResultsByProcessId(Long processId);
    
    /**
     * 缁熻鎸囧畾娴佺▼鐨勪笉鍚堟牸妫€楠岀粨鏋滄暟閲?
     * @param processId 娴佺▼ID
     * @return 涓嶅悎鏍兼楠岀粨鏋滄暟閲?
     */
    int countFailedResultsByProcessId(Long processId);
    
    /**
     * 璁＄畻鎸囧畾娴佺▼鐨勬楠屽悎鏍肩巼
     * @param processId 娴佺▼ID
     * @return 妫€楠屽悎鏍肩巼锛堢櫨鍒嗘瘮锛?
     */
    Double calculatePassRateByProcessId(Long processId);
    
    /**
     * 删除鎸囧畾娴佺▼鐨勬墍鏈夋楠岀粨鏋滆褰?
     * @param processId 娴佺▼ID
     * @return 删除褰卞搷鐨勮鏁?
     */
    int deleteByProcessId(Long processId);
}