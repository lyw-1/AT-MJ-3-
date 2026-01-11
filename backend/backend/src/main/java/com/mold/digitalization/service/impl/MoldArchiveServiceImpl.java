package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.MoldArchive;
import com.mold.digitalization.mapper.MoldArchiveMapper;
import com.mold.digitalization.service.MoldArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoldArchiveServiceImpl extends ServiceImpl<MoldArchiveMapper, MoldArchive> implements MoldArchiveService {

    @Autowired
    private MoldArchiveMapper mapper;

    @Override
    public MoldArchive getByMoldCode(String moldCode) {
        return mapper.selectByMoldCode(moldCode);
    }

    @Override
    public boolean create(MoldArchive archive) {
        return save(archive);
    }

    @Override
    public boolean updateArchive(MoldArchive archive) {
        return updateById(archive);
    }
}