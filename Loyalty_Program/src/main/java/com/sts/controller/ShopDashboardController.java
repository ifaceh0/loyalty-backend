package com.sts.controller;
import com.sts.dto.MonthlyUserStatsDTO;
import com.sts.service.ShopDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // Monthly sales data (last 12 months)
    @GetMapping("/monthlySales/{shopId}")
    public ResponseEntity<Map<String, Integer>> getMonthlySales(@PathVariable Long shopId) {
        Map<String, Integer> data = dashboardService.getMonthlySalesData(shopId);
        return ResponseEntity.ok(data);
    }


   // Customer count comparison like-Current vs 1 month/3 months/6months/12 months
   @GetMapping("/customerCount/{shopId}")
   public ResponseEntity<Map<String, Integer>> getCustomerCountComparison(@PathVariable Long shopId) {
       Map<String, Integer> data = dashboardService.getCustomerComparison(shopId);
       return ResponseEntity.ok(data);
   }
   //find number of register user and active user
   @GetMapping("/monthly")
   public ResponseEntity<List<MonthlyUserStatsDTO>> getMonthlyStats(@RequestParam Long shopId) {
       List<MonthlyUserStatsDTO> stats = dashboardService.getMonthlyStats(shopId);
       return ResponseEntity.ok(stats);
   }
}
