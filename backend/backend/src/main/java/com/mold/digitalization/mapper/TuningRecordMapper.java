package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.TuningRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TuningRecordMapper extends BaseMapper<TuningRecord> {
    List<TuningRecord> selectByMoldCode(@Param("moldCode") String moldCode);
    List<TuningRecord> selectByMoldId(@Param("moldId") Long moldId);
}
