package com.mold.digitalization.controller;

import com.mold.digitalization.service.DashboardService;
import com.mold.digitalization.dto.DashboardSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> summary() {
        return success(dashboardService.getSummary());
    }

    @GetMapping("/overlimit")
    public ResponseEntity<?> overlimit() {
        return success(dashboardService.getOverlimit());
    }

    @GetMapping("/mold-status-counts")
    public ResponseEntity<?> moldStatusCounts() {
        return success(dashboardService.getMoldStatusCounts());
    }

    @GetMapping("/key-molds-supply-demand")
    public ResponseEntity<?> keyMoldsSupplyDemand() {
        return success(dashboardService.getKeyMoldsSupplyDemand());
    }
}
