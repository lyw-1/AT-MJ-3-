package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.SystemParameterMapper;
import com.mold.digitalization.entity.SystemParameter;
import com.mold.digitalization.service.SystemParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 系统鍙傛暟服务实现绫? * 实现系统鍙傛暟鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
@Service
public class SystemParameterServiceImpl extends ServiceImpl<SystemParameterMapper, SystemParameter> implements SystemParameterService {
    
    @Autowired
    private SystemParameterMapper systemParameterMapper;
    
    @Override
    public SystemParameter getSystemParameterByKey(String paramKey) {
        return systemParameterMapper.selectByParamKey(paramKey);
    }
    
    @Override
    public List<SystemParameter> getSystemParametersByType(String paramType) {
        return systemParameterMapper.selectByParamType(paramType);
    }
    
    @Override
    public boolean createSystemParameter(SystemParameter systemParameter) {
        return save(systemParameter);
    }
    
    @Override
    public boolean updateSystemParameter(SystemParameter systemParameter) {
        return updateById(systemParameter);
    }
    
    @Override
    public boolean deleteSystemParameter(Long paramId) {
        return removeById(paramId);
    }
    
    @Override
    public List<SystemParameter> getAllSystemParameters() {
        return list();
    }
}