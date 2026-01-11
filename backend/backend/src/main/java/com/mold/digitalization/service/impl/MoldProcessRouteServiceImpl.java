package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.MoldProcessRouteStep;
import com.mold.digitalization.mapper.MoldProcessRouteStepMapper;
import com.mold.digitalization.service.MoldProcessRouteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MoldProcessRouteServiceImpl extends ServiceImpl<MoldProcessRouteStepMapper, MoldProcessRouteStep> implements MoldProcessRouteService {
    @Override
    public List<MoldProcessRouteStep> listByMold(Long moldId) {
        return list(new QueryWrapper<MoldProcessRouteStep>().eq("mold_id", moldId).orderByAsc("order_no"));
    }

    @Override
    public void replaceRoute(Long moldId, List<MoldProcessRouteStep> steps) {
        remove(new QueryWrapper<MoldProcessRouteStep>().eq("mold_id", moldId));
        int i = 1;
        for (MoldProcessRouteStep s : steps) {
            s.setMoldId(moldId);
            s.setOrderNo(s.getOrderNo() != null ? s.getOrderNo() : i++);
            s.setCreateTime(LocalDateTime.now());
            s.setUpdateTime(LocalDateTime.now());
            save(s);
        }
    }
}
