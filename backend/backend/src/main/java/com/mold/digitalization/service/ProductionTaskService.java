package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.ProductionTask;
import java.util.Date;
import java.util.List;

/**
 * 鐢熶骇浠诲姟服务接口
 * 瀹氫箟鐢熶骇浠诲姟鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
public interface ProductionTaskService extends IService<ProductionTask> {
    
    /**
     * 鏍规嵁浠诲姟缂栧彿查询鐢熶骇浠诲姟
     * @param taskCode 浠诲姟缂栧彿
     * @return 鐢熶骇浠诲姟淇℃伅
     */
    ProductionTask getProductionTaskByCode(String taskCode);
    
    /**
     * 鏍规嵁璁惧ID查询鐢熶骇浠诲姟鍒楄〃
     * @param equipmentId 璁惧ID
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    List<ProductionTask> getProductionTasksByEquipmentId(Long equipmentId);
    
    /**
     * 鏍规嵁鐘舵€佹煡璇㈢敓浜т换鍔″垪琛?     * @param status 浠诲姟状态     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    List<ProductionTask> getProductionTasksByStatus(Integer status);
    
    /**
     * 鏍规嵁妯″叿ID查询鐢熶骇浠诲姟鍒楄〃
     * @param moldId 妯″叿ID
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    List<ProductionTask> getProductionTasksByMoldId(Long moldId);
    
    /**
     * 鏍规嵁鏃堕棿鑼冨洿查询鐢熶骇浠诲姟鍒楄〃
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    List<ProductionTask> getProductionTasksByTimeRange(Date startTime, Date endTime);
    
    /**
     * 创建鏂扮敓浜т换鍔?     * @param productionTask 鐢熶骇浠诲姟淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createProductionTask(ProductionTask productionTask);
    
    /**
     * 更新鐢熶骇浠诲姟淇℃伅
     * @param productionTask 鐢熶骇浠诲姟淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateProductionTask(ProductionTask productionTask);
    
    /**
     * 删除鐢熶骇浠诲姟
     * @param taskId 浠诲姟ID
     * @return 鏄惁删除成功
     */
    boolean deleteProductionTask(Long taskId);
    
    /**
     * 获取鎵€鏈夌敓浜т换鍔″垪琛?     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    List<ProductionTask> getAllProductionTasks();
    
    /**
     * 更新鐢熶骇浠诲姟状态     * @param taskId 浠诲姟ID
     * @param status 鏂扮姸鎬?     * @return 鏄惁更新成功
     */
    boolean updateProductionTaskStatus(Long taskId, Integer status);
}