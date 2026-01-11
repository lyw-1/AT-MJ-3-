package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldAccessoryInfo;

import java.util.List;

public interface MoldAccessoryInfoService extends IService<MoldAccessoryInfo> {
    List<MoldAccessoryInfo> getByMoldCode(String moldCode);
    List<MoldAccessoryInfo> getBySeqCodes(List<String> seqCodes);
    boolean updateStatusAndHandler(List<String> seqCodes, String status, String handler);
}