package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MoldArchiveMapper extends BaseMapper<MoldArchive> {
    MoldArchive selectByMoldCode(@Param("moldCode") String moldCode);
}