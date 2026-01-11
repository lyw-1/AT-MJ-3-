package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldArchive;

public interface MoldArchiveService extends IService<MoldArchive> {
    MoldArchive getByMoldCode(String moldCode);
    boolean create(MoldArchive archive);
    boolean updateArchive(MoldArchive archive);
}