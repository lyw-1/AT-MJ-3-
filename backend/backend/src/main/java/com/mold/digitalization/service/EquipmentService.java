package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.Equipment;
import java.util.List;

/**
 * 璁惧服务接口
 * 瀹氫箟璁惧鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
public interface EquipmentService extends IService<Equipment> {
    
    /**
     * 鏍规嵁璁惧缂栧彿查询璁惧淇℃伅
     * @param equipmentCode 璁惧缂栧彿
     * @return 璁惧淇℃伅
     */
    Equipment getEquipmentByCode(String equipmentCode);
    
    /**
     * 鏍规嵁绫诲瀷ID查询璁惧鍒楄〃
     * @param typeId 璁惧绫诲瀷ID
     * @return 璁惧鍒楄〃
     */
    List<Equipment> getEquipmentByTypeId(Long typeId);
    
    /**
     * 创建鏂拌澶?
     * @param equipment 璁惧淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createEquipment(Equipment equipment);
    
    /**
     * 更新璁惧淇℃伅
     * @param equipment 璁惧淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateEquipment(Equipment equipment);
    
    /**
     * 删除璁惧
     * @param equipmentId 璁惧ID
     * @return 鏄惁删除成功
     */
    boolean deleteEquipment(Long equipmentId);
    
    /**
     * 获取鎵€鏈夎澶囧垪琛?
     * @return 璁惧鍒楄〃
     */
    List<Equipment> getAllEquipment();
    
    /**
     * 更新璁惧状态
     * @param equipmentId 璁惧ID
     * @param status 鏂扮姸鎬?
     * @return 鏄惁更新成功
     */
    boolean updateEquipmentStatus(Long equipmentId, Integer status);
}
