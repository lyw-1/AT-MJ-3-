package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.EquipmentType;
import java.util.List;

/**
 * 璁惧绫诲瀷服务接口
 * 瀹氫箟璁惧绫诲瀷鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
public interface EquipmentTypeService extends IService<EquipmentType> {
    
    /**
     * 鏍规嵁绫诲瀷浠ｇ爜查询璁惧绫诲瀷
     * @param typeCode 绫诲瀷浠ｇ爜
     * @return 璁惧绫诲瀷淇℃伅
     */
    EquipmentType getEquipmentTypeByCode(String typeCode);
    
    /**
     * 创建鏂拌澶囩被鍨?     * @param equipmentType 璁惧绫诲瀷淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createEquipmentType(EquipmentType equipmentType);
    
    /**
     * 更新璁惧绫诲瀷淇℃伅
     * @param equipmentType 璁惧绫诲瀷淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateEquipmentType(EquipmentType equipmentType);
    
    /**
     * 删除璁惧绫诲瀷
     * @param typeId 璁惧绫诲瀷ID
     * @return 鏄惁删除成功
     */
    boolean deleteEquipmentType(Long typeId);
    
    /**
     * 获取鎵€鏈夎澶囩被鍨嬪垪琛?     * @return 璁惧绫诲瀷鍒楄〃
     */
    List<EquipmentType> getAllEquipmentTypes();
}