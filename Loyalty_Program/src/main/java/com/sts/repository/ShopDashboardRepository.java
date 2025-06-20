package com.sts.repository;
import com.sts.entity.UserPurchase_History;
import com.sts.entity.UserPurchase_Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ShopDashboardRepository extends JpaRepository<UserPurchase_History,UserPurchase_Id> {
    // 1. Total number of users per shop
    @Query(value = "SELECT COUNT(*) FROM users WHERE shop_id = :shopId", nativeQuery = true)
    Long getUserCountByShopId(Long shopId);

    // 2. Top 5 most visited users by transaction count
    @Query(value = "SELECT u.user_id, u.first_name, u.last_name, u.email, u.phone_number, COUNT(*) AS visit_count " +
            "FROM user_purchase_history ph " +
            "JOIN users u ON ph.user_id = u.user_id " +
            "WHERE ph.shop_id = :shopId " +
            "GROUP BY u.user_id, u.first_name, u.last_name, u.email, u.phone_number " +
            "ORDER BY visit_count DESC LIMIT 5", nativeQuery = true)
    List<Object[]> getTopVisitedUsers(Long shopId);

    // 3. Top 5 users by total transaction amount
    @Query(value = "SELECT u.user_id, u.first_name, u.last_name, u.email, u.phone_number, SUM(ph.transaction_amount) AS total_spent " +
            "FROM user_purchase_history ph " +
            "JOIN users u ON ph.user_id = u.user_id " +
            "WHERE ph.shop_id = :shopId " +
            "GROUP BY u.user_id, u.first_name, u.last_name, u.email, u.phone_number " +
            "ORDER BY total_spent DESC LIMIT 5", nativeQuery = true)
    List<Object[]> getTopSpendingUsers(Long shopId);
}

