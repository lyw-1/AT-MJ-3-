package com.mold.digitalization.dto;

import lombok.Data;
import java.util.Map;

@Data
public class DashboardSummary {
    private Long moldTotal;
    private Long equipmentTotal;
    private Long taskTotal;
    private Long keyMolds;
    private Map<String, Long> moldByStatus;
    private Map<String, Long> moldByType;
    private Map<String, Long> equipmentByStatus;
    private Map<String, Long> tasksByStatus;
    private Long lowStockAccessories;
}
