package com.sts.service;

import com.sts.repository.ShopDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ShopDashboardService{

    @Autowired
    private ShopDashboardRepository dashboardRepository;

    public Map<String, Object> getDashboardData(Long shopId) {
        Map<String, Object> response = new HashMap<>();

        // Total number of users
        Long userCount = dashboardRepository.getUserCountByShopId(shopId);
        response.put("totalUsers", userCount);

        // Top 5 most visited users
        List<Object[]> visitedResults = dashboardRepository.getTopVisitedUsers(shopId);
        List<Map<String, Object>> topVisitors = new ArrayList<>();
        for (Object[] row : visitedResults) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", row[0]);
            map.put("firstName", row[1]);
            map.put("lastName", row[2]);
            map.put("email", row[3]);
            map.put("phone", row[4]);
            map.put("visitCount", row[5]);
            topVisitors.add(map);
        }
        response.put("topVisitedUsers", topVisitors);

        // Top 5 users by spending
        List<Object[]> spendingResults = dashboardRepository.getTopSpendingUsers(shopId);
        List<Map<String, Object>> topSpenders = new ArrayList<>();
        for (Object[] row : spendingResults) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", row[0]);
            map.put("firstName", row[1]);
            map.put("lastName", row[2]);
            map.put("email", row[3]);
            map.put("phone", row[4]);
            map.put("totalSpent", row[5]);
            topSpenders.add(map);
        }
        response.put("topSpendingUsers", topSpenders);

        return response;
    }
    // Monthly sales data (last 12 months)
    public Map<String, Integer> getMonthlySalesData(Long shopId) {
        LocalDateTime startDate = LocalDateTime.now().minusMonths(11).withDayOfMonth(1); // 12 months range
        List<Object[]> results = dashboardRepository.getMonthlySalesByShopNative(shopId, startDate);

        Map<Integer, Integer> monthlyData = new HashMap<>();
        for (Object[] row : results) {
            Integer month = ((Number) row[0]).intValue();
            Integer totalAmount = ((Number) row[1]).intValue();
            monthlyData.put(month, totalAmount);
        }

        // Prepare data with month names for chart
        Map<String, Integer> chartData = new LinkedHashMap<>();
        YearMonth now = YearMonth.now().minusMonths(11);
        for (int i = 0; i < 12; i++) {
            String monthName = now.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            chartData.put(monthName, monthlyData.getOrDefault(now.getMonthValue(), 0));
            now = now.plusMonths(1);
        }

        return chartData;
    }
}
