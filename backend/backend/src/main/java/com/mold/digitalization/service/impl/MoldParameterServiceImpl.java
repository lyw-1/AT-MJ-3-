package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.MoldParameterMapper;
import com.mold.digitalization.entity.MoldParameter;
import com.mold.digitalization.service.MoldParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * 妯″叿鍙傛暟服务实现绫?
 * 实现妯″叿鍙傛暟鐩稿叧鐨勪笟鍔￠€昏緫
 */
@Service
public class MoldParameterServiceImpl extends ServiceImpl<MoldParameterMapper, MoldParameter> implements MoldParameterService {
    
    @Autowired
    private MoldParameterMapper moldParameterMapper;
    
    @Override
    public MoldParameter getByMoldId(Long moldId) {
        QueryWrapper<MoldParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mold_id", moldId);
        return moldParameterMapper.selectOne(queryWrapper);
    }
    
    @Override
    public boolean saveParameter(MoldParameter parameter) {
        // 设置鏃堕棿鎴?       parameter.setCreateTime(LocalDateTime.now());
        parameter.setUpdateTime(LocalDateTime.now());
        return save(parameter);
    }
    
    @Override
    public boolean updateParameter(MoldParameter parameter) {
        // 更新鏃堕棿鎴?       parameter.setUpdateTime(LocalDateTime.now());
        return updateById(parameter);
    }
}
