package com.mold.digitalization.service;

import com.mold.digitalization.dto.HomeOverview;
import com.mold.digitalization.dto.HomeStats;

public interface HomeService {
    HomeStats getStats();
    HomeOverview getOverview();
}
