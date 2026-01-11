package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldStatus;
import java.util.List;

/**
 * 妯″叿鐘舵€佹湇鍔℃帴鍙? * 瀹氫箟妯″叿鐘舵€佺浉鍏崇殑涓氬姟服务方法
 */
public interface MoldStatusService extends IService<MoldStatus> {
    
    /**
     * 鏍规嵁鐘舵€佷唬鐮佹煡璇㈡ā鍏风姸鎬?     * @param statusCode 鐘舵€佷唬鐮?     * @return 妯″叿鐘舵€佷俊鎭?     */
    MoldStatus getMoldStatusByCode(String statusCode);
    
    /**
     * 创建鏂版ā鍏风姸鎬?     * @param moldStatus 妯″叿鐘舵€佷俊鎭?     * @return 鏄惁创建成功
     */
    boolean createMoldStatus(MoldStatus moldStatus);
    
    /**
     * 更新妯″叿鐘舵€佷俊鎭?     * @param moldStatus 妯″叿鐘舵€佷俊鎭?     * @return 鏄惁更新成功
     */
    boolean updateMoldStatus(MoldStatus moldStatus);
    
    /**
     * 删除妯″叿状态     * @param statusId 妯″叿鐘舵€両D
     * @return 鏄惁删除成功
     */
    boolean deleteMoldStatus(Long statusId);
    
    /**
     * 获取鎵€鏈夋ā鍏风姸鎬佸垪琛?     * @return 妯″叿鐘舵€佸垪琛?     */
    List<MoldStatus> getAllMoldStatuses();
}