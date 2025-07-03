package com.sts.controller;

import com.sts.dto.TransactionDTO;

import com.sts.entity.UserPurchase_History;
import com.sts.repository.UserPurchaseHistory_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userDashboard")
public class UserDashboardController {

    @Autowired
    private UserPurchaseHistory_Repository historyRepository;

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<Map<String, List<TransactionDTO>>> getUserTransactions(@PathVariable Long userId) {
        List<UserPurchase_History> historyList = historyRepository.findByUserUserId(userId);

        Map<String, List<TransactionDTO>> groupedByShop = new HashMap<>();

        for (UserPurchase_History history : historyList) {
            String shopName = history.getShop().getShopName();

            groupedByShop
                    .computeIfAbsent(shopName, k -> new ArrayList<>())
                    .add(new TransactionDTO(
                            history.getPurchaseDate().toLocalDate().toString(),
                            history.getTransactionAmount(),
                            history.getGivenPoints()
                    ));
        }

        return ResponseEntity.ok(groupedByShop);
    }
}
