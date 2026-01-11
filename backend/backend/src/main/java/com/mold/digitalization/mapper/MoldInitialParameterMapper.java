package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldInitialParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MoldInitialParameterMapper extends BaseMapper<MoldInitialParameter> {
    List<MoldInitialParameter> selectByMoldCode(@Param("moldCode") String moldCode);
    MoldInitialParameter selectLatestByMoldCode(@Param("moldCode") String moldCode);
    MoldInitialParameter selectByApplicationNo(@Param("applicationNo") String applicationNo);
}