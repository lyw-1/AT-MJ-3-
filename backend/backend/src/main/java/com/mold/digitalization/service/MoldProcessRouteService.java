package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldProcessRouteStep;
import java.util.List;

public interface MoldProcessRouteService extends IService<MoldProcessRouteStep> {
    List<MoldProcessRouteStep> listByMold(Long moldId);
    void replaceRoute(Long moldId, List<MoldProcessRouteStep> steps);
}
