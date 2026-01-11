package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.DeepHolePreDrillBatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeepHolePreDrillBatchMapper extends BaseMapper<DeepHolePreDrillBatch> {
    List<DeepHolePreDrillBatch> selectByProcessId(@Param("processId") Long processId);
}
