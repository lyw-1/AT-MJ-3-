package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldInitialParameter;

import java.util.List;

public interface MoldInitialParameterService extends IService<MoldInitialParameter> {
    List<MoldInitialParameter> getByMoldCode(String moldCode);
    MoldInitialParameter getLatestByMoldCode(String moldCode);
    MoldInitialParameter getByApplicationNo(String applicationNo);
    boolean create(MoldInitialParameter param);
    boolean updateParam(MoldInitialParameter param);
    boolean deleteParam(Long id);
}
