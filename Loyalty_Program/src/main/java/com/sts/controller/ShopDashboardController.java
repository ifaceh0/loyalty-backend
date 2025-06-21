package com.sts.controller;
import com.sts.service.ShopDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class ShopDashboardController {
    @Autowired
    private ShopDashboardService dashboardService;

    // Single API to return all dashboard data
    @GetMapping("/dashboardChat/{shopId}")
    public Map<String, Object> getDashboard(@PathVariable Long shopId) {
        return dashboardService.getDashboardData(shopId);
    }
}
