package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.SystemParameter;
import java.util.List;

/**
 * 系统鍙傛暟服务接口
 * 瀹氫箟系统鍙傛暟鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
public interface SystemParameterService extends IService<SystemParameter> {
    
    /**
     * 鏍规嵁鍙傛暟閿悕查询系统鍙傛暟
     * @param paramKey 鍙傛暟閿悕
     * @return 系统鍙傛暟淇℃伅
     */
    SystemParameter getSystemParameterByKey(String paramKey);
    
    /**
     * 鏍规嵁鍙傛暟绫诲瀷查询系统鍙傛暟鍒楄〃
     * @param paramType 鍙傛暟绫诲瀷
     * @return 系统鍙傛暟鍒楄〃
     */
    List<SystemParameter> getSystemParametersByType(String paramType);
    
    /**
     * 创建鏂扮郴缁熷弬鏁?
     * @param systemParameter 系统鍙傛暟淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createSystemParameter(SystemParameter systemParameter);
    
    /**
     * 更新系统鍙傛暟淇℃伅
     * @param systemParameter 系统鍙傛暟淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateSystemParameter(SystemParameter systemParameter);
    
    /**
     * 删除系统鍙傛暟
     * @param paramId 鍙傛暟ID
     * @return 鏄惁删除成功
     */
    boolean deleteSystemParameter(Long paramId);
    
    /**
     * 获取鎵€鏈夌郴缁熷弬鏁板垪琛?
     * @return 系统鍙傛暟鍒楄〃
     */
    List<SystemParameter> getAllSystemParameters();
}
