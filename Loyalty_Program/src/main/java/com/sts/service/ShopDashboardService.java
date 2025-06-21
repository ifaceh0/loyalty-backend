package com.sts.service;

import com.sts.repository.ShopDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
