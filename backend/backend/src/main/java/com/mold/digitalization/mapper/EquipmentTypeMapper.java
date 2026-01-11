package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.EquipmentType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 璁惧绫诲瀷Mapper接口
 * 鎻愪緵璁惧绫诲瀷琛ㄧ殑数据搴撴搷浣滄柟娉? */
@Mapper
public interface EquipmentTypeMapper extends BaseMapper<EquipmentType> {
    
    /**
     * 鏍规嵁绫诲瀷浠ｇ爜查询璁惧绫诲瀷
     * @param typeCode 绫诲瀷浠ｇ爜
     * @return 璁惧绫诲瀷淇℃伅
     */
    EquipmentType selectByTypeCode(String typeCode);
}
