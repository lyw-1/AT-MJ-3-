package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.SlotWidthRecord;
import java.util.List;

/**
 * 妲藉记录服务接口
 * 鎻愪緵妲藉娴嬮噺记录鐩稿叧鐨勪笟鍔℃搷浣? */
public interface SlotWidthRecordService extends IService<SlotWidthRecord> {
    
    /**
     * 创建妲藉记录
     * @param record 妲藉记录
     * @return 鏄惁创建成功
     */
    boolean createRecord(SlotWidthRecord record);
    
    /**
     * 鏍规嵁妯″叿ID查询妲藉记录
     * @param moldId 妯″叿ID
     * @return 妲藉记录鍒楄〃
     */
    List<SlotWidthRecord> getByMoldId(Long moldId);
    
    /**
     * 查询涓嶅悎鏍肩殑妲藉记录
     * @return 涓嶅悎鏍艰褰曞垪琛?     */
    List<SlotWidthRecord> getUnqualifiedRecords();
    
    /**
     * 更新记录状态     * @param id 记录ID
     * @param status 鏂扮姸鎬?     * @return 鏄惁更新成功
     */
    boolean updateStatus(Long id, Integer status);
}
