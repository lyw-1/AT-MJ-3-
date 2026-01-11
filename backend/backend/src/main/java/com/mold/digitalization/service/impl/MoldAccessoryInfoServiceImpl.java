package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.MoldAccessoryInfo;
import com.mold.digitalization.mapper.MoldAccessoryInfoMapper;
import com.mold.digitalization.service.MoldAccessoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoldAccessoryInfoServiceImpl extends ServiceImpl<MoldAccessoryInfoMapper, MoldAccessoryInfo> implements MoldAccessoryInfoService {

    @Autowired
    private MoldAccessoryInfoMapper mapper;

    @Override
    public List<MoldAccessoryInfo> getByMoldCode(String moldCode) {
        return mapper.selectByMoldCode(moldCode);
    }

    @Override
    public List<MoldAccessoryInfo> getBySeqCodes(List<String> seqCodes) {
        return mapper.selectBySeqCodes(seqCodes);
    }

    @Override
    public boolean updateStatusAndHandler(List<String> seqCodes, String status, String handler) {
        UpdateWrapper<MoldAccessoryInfo> uw = new UpdateWrapper<>();
        uw.in("seq_code", seqCodes).set("lend_return_status", status).set("handler", handler);
        return update(uw);
    }
}