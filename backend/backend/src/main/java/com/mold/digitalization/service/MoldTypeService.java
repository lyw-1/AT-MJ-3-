package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldType;
import java.util.List;

/**
 * 妯″叿绫诲瀷服务接口
 * 瀹氫箟妯″叿绫诲瀷鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
public interface MoldTypeService extends IService<MoldType> {
    
    /**
     * 鏍规嵁绫诲瀷浠ｇ爜查询妯″叿绫诲瀷
     * @param typeCode 绫诲瀷浠ｇ爜
     * @return 妯″叿绫诲瀷淇℃伅
     */
    MoldType getMoldTypeByCode(String typeCode);
    
    /**
     * 创建鏂版ā鍏风被鍨?     * @param moldType 妯″叿绫诲瀷淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createMoldType(MoldType moldType);
    
    /**
     * 更新妯″叿绫诲瀷淇℃伅
     * @param moldType 妯″叿绫诲瀷淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateMoldType(MoldType moldType);
    
    /**
     * 删除妯″叿绫诲瀷
     * @param typeId 妯″叿绫诲瀷ID
     * @return 鏄惁删除成功
     */
    boolean deleteMoldType(Long typeId);
    
    /**
     * 获取鎵€鏈夋ā鍏风被鍨嬪垪琛?     * @return 妯″叿绫诲瀷鍒楄〃
     */
    List<MoldType> getAllMoldTypes();
}
