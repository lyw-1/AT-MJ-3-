package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.SlottingLayerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SlottingLayerRecordMapper extends BaseMapper<SlottingLayerRecord> {
    List<SlottingLayerRecord> selectByProcessId(@Param("processId") Long processId);
}
