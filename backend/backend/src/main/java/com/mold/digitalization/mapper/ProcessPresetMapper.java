package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.ProcessPreset;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工序预设置Mapper
 */
@Mapper
public interface ProcessPresetMapper extends BaseMapper<ProcessPreset> {
    // 仅继承MyBatis Plus的BaseMapper，不再定义自定义方法
}
