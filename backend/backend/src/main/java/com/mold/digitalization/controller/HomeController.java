package com.mold.digitalization.controller;

import com.mold.digitalization.dto.HomeOverview;
import com.mold.digitalization.dto.HomeStats;
import com.mold.digitalization.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController extends BaseController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/stats")
    public ResponseEntity<HomeStats> stats() {
        return success(homeService.getStats());
    }

    @GetMapping("/overview")
    public ResponseEntity<HomeOverview> overview() {
        return success(homeService.getOverview());
    }
}
