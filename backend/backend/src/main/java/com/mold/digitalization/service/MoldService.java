package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.Mold;
import java.util.List;

/**
 * 妯″叿服务接口
 * 瀹氫箟妯″叿鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
public interface MoldService extends IService<Mold> {
    
    /**
     * 鏍规嵁妯″叿缂栧彿查询妯″叿淇℃伅
     * @param moldCode 妯″叿缂栧彿
     * @return 妯″叿淇℃伅
     */
    Mold getMoldByCode(String moldCode);
    
    /**
     * 鏍规嵁绫诲瀷ID查询妯″叿鍒楄〃
     * @param typeId 妯″叿绫诲瀷ID
     * @return 妯″叿鍒楄〃
     */
    List<Mold> getMoldsByTypeId(Long typeId);
    
    /**
     * 鏍规嵁鐘舵€両D查询妯″叿鍒楄〃
     * @param statusId 妯″叿鐘舵€両D
     * @return 妯″叿鍒楄〃
     */
    List<Mold> getMoldsByStatusId(Long statusId);
    
    /**
     * 创建鏂版ā鍏?
     * @param mold 妯″叿淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createMold(Mold mold);
    
    /**
     * 更新妯″叿淇℃伅
     * @param mold 妯″叿淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateMold(Mold mold);
    
    /**
     * 删除妯″叿
     * @param moldId 妯″叿ID
     * @return 鏄惁删除成功
     */
    boolean deleteMold(Long moldId);
    
    /**
     * 获取鎵€鏈夋ā鍏峰垪琛?
     * @return 妯″叿鍒楄〃
     */
    List<Mold> getAllMolds();
    
    /**
     * 更新妯″叿状态
     * @param moldId 妯″叿ID
     * @param statusId 鏂扮姸鎬両D
     * @return 鏄惁更新成功
     */
    boolean updateMoldStatus(Long moldId, Long statusId);
}
