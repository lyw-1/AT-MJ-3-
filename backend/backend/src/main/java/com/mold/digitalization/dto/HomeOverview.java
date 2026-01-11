package com.mold.digitalization.dto;

import com.mold.digitalization.entity.NonMoldApplication;
import com.mold.digitalization.entity.ProductionRecord;
import com.mold.digitalization.entity.ProductionTask;
import lombok.Data;

import java.util.List;

@Data
public class HomeOverview {
    private HomeStats stats;
    private List<String> todos; // 简化为字符串描述，后续可扩展为结构化
    private List<ProductionTask> ongoingMoldTasks;
    private List<NonMoldApplication> ongoingNonMoldTasks;
    private List<ProductionRecord> recentProductionRecords;
}
