package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.SystemParameter;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 系统鍙傛暟Mapper接口
 * 鎻愪緵系统鍙傛暟琛ㄧ殑数据搴撴搷浣滄柟娉?
 */
@Mapper
public interface SystemParameterMapper extends BaseMapper<SystemParameter> {
    
    /**
     * 鏍规嵁鍙傛暟閿煡璇㈢郴缁熷弬鏁?
     * @param paramKey 鍙傛暟閿?
     * @return 系统鍙傛暟淇℃伅
     */
    SystemParameter selectByParamKey(String paramKey);
    
    /**
     * 鏍规嵁鍙傛暟绫诲瀷查询系统鍙傛暟鍒楄〃
     * @param paramType 鍙傛暟绫诲瀷
     * @return 系统鍙傛暟鍒楄〃
     */
    List<SystemParameter> selectByParamType(String paramType);
}
