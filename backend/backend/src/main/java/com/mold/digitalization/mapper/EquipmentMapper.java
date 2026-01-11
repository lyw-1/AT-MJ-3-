package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 璁惧Mapper接口
 * 鎻愪緵璁惧琛ㄧ殑数据搴撴搷浣滄柟娉?
 */
@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {
    
    /**
     * 鏍规嵁璁惧浠ｇ爜查询璁惧淇℃伅
     * @param equipmentCode 璁惧浠ｇ爜
     * @return 璁惧淇℃伅
     */
    Equipment selectByEquipmentCode(String equipmentCode);
    
    /**
     * 鏍规嵁璁惧绫诲瀷查询璁惧鍒楄〃
     * @param typeId 璁惧绫诲瀷ID
     * @return 璁惧鍒楄〃
     */
    List<Equipment> selectByTypeId(Long typeId);
}