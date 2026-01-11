package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldParameter;
import org.apache.ibatis.annotations.Mapper;

/**
 * 妯″叿鍙傛暟Mapper接口
 * 鐢ㄤ簬操作妯″叿鍙傛暟数据
 */
@Mapper
public interface MoldParameterMapper extends BaseMapper<MoldParameter> {
    
    // 可以鏍规嵁闇€瑕佹坊鍔犺嚜瀹氫箟查询方法
    // 渚嬪锛氭寜鏉愯川查询妯″叿鍙傛暟
}
