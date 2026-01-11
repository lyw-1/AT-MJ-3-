package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldParameter;

/**
 * 妯″叿鍙傛暟服务接口
 * 鎻愪緵妯″叿鍙傛暟鐩稿叧鐨勪笟鍔℃搷浣? */
public interface MoldParameterService extends IService<MoldParameter> {
    
    /**
     * 鏍规嵁妯″叿ID获取鍙傛暟淇℃伅
     * @param moldId 妯″叿ID
     * @return 妯″叿鍙傛暟淇℃伅
     */
    MoldParameter getByMoldId(Long moldId);
    
    /**
     * 淇濆瓨妯″叿鍙傛暟
     * @param parameter 妯″叿鍙傛暟
     * @return 鏄惁淇濆瓨成功
     */
    boolean saveParameter(MoldParameter parameter);
    
    /**
     * 更新妯″叿鍙傛暟
     * @param parameter 妯″叿鍙傛暟
     * @return 鏄惁更新成功
     */
    boolean updateParameter(MoldParameter parameter);
}
