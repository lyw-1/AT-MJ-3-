package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.WireCutProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WireCutProcessMapper extends BaseMapper<WireCutProcess> {
    List<WireCutProcess> selectByMoldCode(@Param("moldCode") String moldCode);
}