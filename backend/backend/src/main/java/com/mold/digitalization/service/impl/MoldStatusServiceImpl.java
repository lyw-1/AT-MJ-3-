package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.MoldStatusMapper;
import com.mold.digitalization.entity.MoldStatus;
import com.mold.digitalization.service.MoldStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 妯″叿鐘舵€佹湇鍔″疄鐜扮被
 * 实现妯″叿鐘舵€佺浉鍏崇殑涓氬姟服务方法
 */
@Service
public class MoldStatusServiceImpl extends ServiceImpl<MoldStatusMapper, MoldStatus> implements MoldStatusService {
    
    @Autowired
    private MoldStatusMapper moldStatusMapper;
    
    @Override
    public MoldStatus getMoldStatusByCode(String statusCode) {
        return moldStatusMapper.selectByStatusCode(statusCode);
    }
    
    @Override
    public boolean createMoldStatus(MoldStatus moldStatus) {
        return save(moldStatus);
    }
    
    @Override
    public boolean updateMoldStatus(MoldStatus moldStatus) {
        return updateById(moldStatus);
    }
    
    @Override
    public boolean deleteMoldStatus(Long statusId) {
        // 鍏堟鏌ユā鍏风姸鎬佹槸鍚﹀瓨鍦?
        MoldStatus existingStatus = getById(statusId);
        if (existingStatus == null) {
            return false;
        }
        
        // 浣跨敤Mapper鐨刣eleteById方法删除
        return moldStatusMapper.deleteById(statusId) > 0;
    }
    
    @Override
    public List<MoldStatus> getAllMoldStatuses() {
        return list();
    }
}