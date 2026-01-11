package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.SlottingProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SlottingProcessMapper extends BaseMapper<SlottingProcess> {
    List<SlottingProcess> selectByMoldCode(@Param("moldCode") String moldCode);
}
