package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.LogMapper;
import com.mold.digitalization.entity.Log;
import com.mold.digitalization.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日志服务实现类
 * 实现日志相关的业务服务方法
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    
    @Autowired
    private LogMapper logMapper;
    
    @Override
    public List<Log> getLogsByUserId(Long userId) {
        return logMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Log> getLogsByOperationType(String operationType) {
        return logMapper.selectByOperationType(operationType);
    }
    
    @Override
    public List<Log> getLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return logMapper.selectByTimeRange(startTime, endTime);
    }
    
    @Override
    public boolean createLog(Log log) {
        return save(log);
    }
    
    @Override
    public List<Log> getAllLogs() {
        return list();
    }
    
    @Override
    public int cleanLogsBefore(LocalDateTime beforeDate) {
        // 这里可以实现清理日志的逻辑
        // 由于LogMapper中没有直接提供清理方法，可以通过扩展Mapper或通过条件删除实现
        // 此处仅作为示例实现
        return 0;
    }
}
