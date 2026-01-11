package com.mold.digitalization.service;

import com.mold.digitalization.dto.DashboardSummary;

public interface DashboardService {
    DashboardSummary getSummary();
    Object getOverlimit();
    Object getMoldStatusCounts();
    Object getKeyMoldsSupplyDemand();
}
