package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 妯″叿绫诲瀷Mapper接口
 * 鎻愪緵妯″叿绫诲瀷琛ㄧ殑数据搴撴搷浣滄柟娉? */
@Mapper
public interface MoldTypeMapper extends BaseMapper<MoldType> {
    
    /**
     * 鏍规嵁绫诲瀷浠ｇ爜查询妯″叿绫诲瀷
     * @param typeCode 绫诲瀷浠ｇ爜
     * @return 妯″叿绫诲瀷淇℃伅
     */
    MoldType selectByTypeCode(String typeCode);
}