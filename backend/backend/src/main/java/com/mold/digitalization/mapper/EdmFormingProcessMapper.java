package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.EdmFormingProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EdmFormingProcessMapper extends BaseMapper<EdmFormingProcess> {
    List<EdmFormingProcess> selectByMoldCode(@Param("moldCode") String moldCode);
}