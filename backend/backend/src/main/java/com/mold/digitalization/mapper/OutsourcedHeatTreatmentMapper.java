package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.OutsourcedHeatTreatment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OutsourcedHeatTreatmentMapper extends BaseMapper<OutsourcedHeatTreatment> {
    List<OutsourcedHeatTreatment> selectByMoldCode(@Param("moldCode") String moldCode);
}
