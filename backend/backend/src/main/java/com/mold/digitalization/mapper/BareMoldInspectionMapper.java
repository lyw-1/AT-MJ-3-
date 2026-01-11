package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.BareMoldInspection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BareMoldInspectionMapper extends BaseMapper<BareMoldInspection> {
    List<BareMoldInspection> selectByMoldCode(@Param("moldCode") String moldCode);
}
