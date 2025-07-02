package com.sts.controller;

import com.sts.dto.UserDashboardDTO;

import com.sts.service.UserDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/userdashboard")
public class UserDashboardController {
    @Autowired
    private UserDashboardService userdashboardService;

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<UserDashboardDTO>> getUserTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(userdashboardService.getUserTransactions(userId));
    }
}
