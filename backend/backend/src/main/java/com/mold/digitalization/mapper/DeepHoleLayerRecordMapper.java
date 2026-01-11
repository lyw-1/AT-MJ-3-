package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.DeepHoleLayerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeepHoleLayerRecordMapper extends BaseMapper<DeepHoleLayerRecord> {
    List<DeepHoleLayerRecord> selectByProcessId(@Param("processId") Long processId);
}