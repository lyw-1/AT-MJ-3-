package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.MoldAccessoryInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MoldAccessoryInfoMapper extends BaseMapper<MoldAccessoryInfo> {
    List<MoldAccessoryInfo> selectByMoldCode(@Param("moldCode") String moldCode);
    List<MoldAccessoryInfo> selectBySeqCodes(@Param("seqCodes") java.util.List<String> seqCodes);
}