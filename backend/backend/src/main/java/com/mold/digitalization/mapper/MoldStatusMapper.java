package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldStatus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 妯″叿鐘舵€丮apper接口
 * 鎻愪緵妯″叿鐘舵€佽〃鐨勬暟鎹簱操作方法
 */
@Mapper
public interface MoldStatusMapper extends BaseMapper<MoldStatus> {
    
    /**
     * 鏍规嵁鐘舵€佷唬鐮佹煡璇㈡ā鍏风姸鎬?
     * @param statusCode 鐘舵€佷唬鐮?
     * @return 妯″叿鐘舵€佷俊鎭?
     */
    MoldStatus selectByStatusCode(String statusCode);
}
