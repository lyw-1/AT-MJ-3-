package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 鏃ュ織Mapper接口
 * 鎻愪緵鏃ュ織琛ㄧ殑数据搴撴搷浣滄柟娉? */
@Mapper
public interface LogMapper extends BaseMapper<Log> {
    
    /**
     * 鏍规嵁用户ID查询鏃ュ織鍒楄〃
     * @param userId 用户ID
     * @return 鏃ュ織鍒楄〃
     */
    List<Log> selectByUserId(Long userId);
    
    /**
     * 鏍规嵁操作绫诲瀷查询鏃ュ織鍒楄〃
     * @param operationType 操作绫诲瀷
     * @return 鏃ュ織鍒楄〃
     */
    List<Log> selectByOperationType(String operationType);
    
    /**
     * 查询鎸囧畾鏃堕棿鑼冨洿鍐呯殑鏃ュ織
     * @param startTime 开€濮嬫椂闂?     * @param endTime 缁撴潫鏃堕棿
     * @return 鏃ュ織鍒楄〃
     */
    List<Log> selectByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
}
