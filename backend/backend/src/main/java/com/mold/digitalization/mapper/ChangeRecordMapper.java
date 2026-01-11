package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.ChangeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChangeRecordMapper extends BaseMapper<ChangeRecord> {
    List<ChangeRecord> selectByMoldCode(@Param("moldCode") String moldCode);
    List<ChangeRecord> selectByMoldId(@Param("moldId") Long moldId);
}