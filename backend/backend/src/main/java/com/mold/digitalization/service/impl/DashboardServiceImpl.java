package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mold.digitalization.dto.DashboardSummary;
import com.mold.digitalization.entity.Accessory;
import com.mold.digitalization.entity.Equipment;
import com.mold.digitalization.entity.Mold;
import com.mold.digitalization.entity.ProductionTask;
import com.mold.digitalization.mapper.AccessoryMapper;
import com.mold.digitalization.mapper.EquipmentMapper;
import com.mold.digitalization.mapper.MoldMapper;
import com.mold.digitalization.mapper.ProductionTaskMapper;
import com.mold.digitalization.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired private MoldMapper moldMapper;
    @Autowired private EquipmentMapper equipmentMapper;
    @Autowired private ProductionTaskMapper productionTaskMapper;
    @Autowired private AccessoryMapper accessoryMapper;
    @Autowired private JdbcTemplate jdbcTemplate;

    @Override
    public DashboardSummary getSummary() {
        DashboardSummary s = new DashboardSummary();
        s.setMoldTotal(moldMapper.selectCount(null));
        s.setEquipmentTotal(equipmentMapper.selectCount(null));
        s.setTaskTotal(productionTaskMapper.selectCount(null));
        s.setKeyMolds(moldMapper.selectCount(new QueryWrapper<Mold>().eq("is_key", true)));

        s.setEquipmentByStatus(groupCount("select cast(status as char) as k, count(1) as v from equipment group by status"));
        s.setTasksByStatus(groupCount("select cast(status as char) as k, count(1) as v from production_task group by status"));
        s.setMoldByStatus(groupCount("select cast(mold_status_id as char) as k, count(1) as v from mold group by mold_status_id"));
        s.setMoldByType(groupCount("select cast(mold_type_id as char) as k, count(1) as v from mold group by mold_type_id"));

        // 低库存配件数量（stock_quantity < minimum_stock）
        List<Accessory> low = jdbcTemplate.query("select * from accessory where stock_quantity < minimum_stock", (rs, i) -> {
            Accessory a = new Accessory();
            a.setId(rs.getLong("id"));
            return a;
        });
        s.setLowStockAccessories((long) low.size());
        return s;
    }

    @Override
    public Object getOverlimit() {
        Map<String, Long> res = new HashMap<>();
        Long slotOver = jdbcTemplate.queryForObject("select count(1) from slot_width_record where is_qualified = 0", Long.class);
        res.put("slotWidthExceeded", slotOver == null ? 0L : slotOver);
        Long densityOver = jdbcTemplate.queryForObject("select count(1) from inspection_result where inspection_item = ? and inspection_result = 0", Long.class, "容重");
        res.put("densityExceeded", densityOver == null ? 0L : densityOver);
        return res;
    }

    @Override
    public Object getMoldStatusCounts() {
        Map<String, Long> map = new HashMap<>();
        String[] codes = {"WAIT_PROCESS","PROCESSING","OUTSOURCED","WAIT_TUNING","TUNING","WAIT_ACCEPT"};
        for (String c : codes) {
            Long v = jdbcTemplate.queryForObject(
                    "select count(1) from mold m join mold_status s on m.mold_status_id = s.id where s.status_code = ?",
                    Long.class,
                    c
            );
            map.put(c, v == null ? 0L : v);
        }
        return map;
    }

    @Override
    public Object getKeyMoldsSupplyDemand() {
        Map<String, Long> map = new HashMap<>();
        Long supply = jdbcTemplate.queryForObject(
                "select count(1) from mold m join mold_status s on m.mold_status_id = s.id where m.is_key = 1 and s.status_code = ?",
                Long.class,
                "AVAILABLE"
        );
        map.put("supply", supply == null ? 0L : supply);
        Long demand = jdbcTemplate.queryForObject(
                "select count(1) from production_task t join mold m on t.mold_id = m.id where m.is_key = 1 and t.status in (0,1)",
                Long.class
        );
        map.put("demand", demand == null ? 0L : demand);
        return map;
    }
    private Map<String, Long> groupCount(String sql) {
        Map<String, Long> map = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            map.put(rs.getString("k"), rs.getLong("v"));
        });
        return map;
    }
}
