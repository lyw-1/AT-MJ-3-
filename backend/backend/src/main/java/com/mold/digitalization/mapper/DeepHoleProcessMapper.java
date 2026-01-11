package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.DeepHoleProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeepHoleProcessMapper extends BaseMapper<DeepHoleProcess> {
    List<DeepHoleProcess> selectByMoldCode(@Param("moldCode") String moldCode);
}