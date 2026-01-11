package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mold.digitalization.dto.HomeOverview;
import com.mold.digitalization.dto.HomeStats;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.entity.MoldStatus;
import com.mold.digitalization.entity.NonMoldApplication;
import com.mold.digitalization.entity.ProductionRecord;
import com.mold.digitalization.entity.ProductionTask;
import com.mold.digitalization.mapper.MoldMapper;
import com.mold.digitalization.mapper.MoldStatusMapper;
import com.mold.digitalization.mapper.NonMoldApplicationMapper;
import com.mold.digitalization.mapper.ProductionRecordMapper;
import com.mold.digitalization.mapper.ProductionTaskMapper;
import com.mold.digitalization.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private MoldStatusMapper moldStatusMapper;
    @Autowired private MoldMapper moldMapper;
    @Autowired private ProductionTaskMapper productionTaskMapper;
    @Autowired private NonMoldApplicationMapper nonMoldApplicationMapper;
    @Autowired private ProductionRecordMapper productionRecordMapper;

    @Override
    public HomeStats getStats() {
        HomeStats s = new HomeStats();
        // 验收合格率（2025年之后）：以 inspection_result 表为基础，统计 result=1 / total
        var since = LocalDateTime.of(2025, 1, 1, 0, 0);
        Long total = jdbcTemplate.queryForObject("select count(1) from inspection_result where inspection_time >= ?", Long.class, since);
        Long qualified = jdbcTemplate.queryForObject("select count(1) from inspection_result where inspection_time >= ? and inspection_result = 1", Long.class, since);
        s.setAcceptanceRateSince2025(total != null && total > 0 ? (qualified != null ? qualified.doubleValue() / total.doubleValue() : 0.0) : 0.0);

        // 裸模库、可用库、封存库：按 mold_status.status_code 映射到 mold.mold_status_id
        s.setBareCount(countByStatusCode("BARE"));
        s.setAvailableCount(countByStatusCode("AVAILABLE"));
        s.setSealedCount(countByStatusCode("SEALED"));
        return s;
    }

    private Long countByStatusCode(String code) {
        MoldStatus st = moldStatusMapper.selectByStatusCode(code);
        if (st == null) return 0L;
        return moldMapper.selectCount(new QueryWrapper<Mold>().eq("mold_status_id", st.getId()));
    }

    @Override
    public HomeOverview getOverview() {
        HomeOverview o = new HomeOverview();
        o.setStats(getStats());

        // 待办事项（示例）：未开始的生产任务数量、待审核的非模具申请数量
        List<String> todos = new ArrayList<>();
        Long pendingTasks = productionTaskMapper.selectCount(new QueryWrapper<ProductionTask>().eq("status", 0));
        Long pendingApps = nonMoldApplicationMapper.selectCount(new QueryWrapper<NonMoldApplication>().eq("status", 0));
        todos.add("未开始生产任务: " + pendingTasks);
        todos.add("待审核非模具申请: " + pendingApps);
        o.setTodos(todos);

        // 在模具加工任务列表：进行中的任务（status=1），且绑定了 moldId
        List<ProductionTask> ongoingMold = productionTaskMapper.selectList(new QueryWrapper<ProductionTask>().eq("status", 1).isNotNull("mold_id").orderByDesc("update_time"));
        o.setOngoingMoldTasks(ongoingMold);

        // 非模具加工任务列表：非模具申请进行中（status=1）
        List<NonMoldApplication> ongoingNonMold = nonMoldApplicationMapper.selectList(new QueryWrapper<NonMoldApplication>().eq("status", 1).orderByDesc("id"));
        o.setOngoingNonMoldTasks(ongoingNonMold);

        // 最近生产数据录入记录（取最近10条）
        List<ProductionRecord> recentRecords = productionRecordMapper.selectList(new QueryWrapper<ProductionRecord>().orderByDesc("create_time").last("limit 10"));
        o.setRecentProductionRecords(recentRecords);

        return o;
    }
}
