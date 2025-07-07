package com.sts.repository;
import com.sts.entity.UserPurchase_History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShopDashboardRepository extends JpaRepository<UserPurchase_History, Long> {
    // 1. Total number of users per shop
    @Query(value = "SELECT COUNT(*) FROM users WHERE referred_by_shop_id = :shopId", nativeQuery = true)
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

    // Monthly sales data (last 12 months)

    @Query(
            value = "SELECT EXTRACT(MONTH FROM purchase_date) AS month, " +
                    "SUM(transaction_amount) AS total " +
                    "FROM user_purchase_history " +
                    "WHERE shop_id = :shopId AND purchase_date >= :startDate " +
                    "GROUP BY EXTRACT(MONTH FROM purchase_date) " +
                    "ORDER BY month",
            nativeQuery = true
    )
    List<Object[]> getMonthlySalesByShopNative(@Param("shopId") Long shopId,
                                               @Param("startDate") LocalDateTime startDate);

    // Customer count comparison like-Current vs 1 month/3 months/6months/12 months
    @Query(value = """
    SELECT COUNT(DISTINCT user_id) FROM user_purchase_history 
    WHERE shop_id = :shopId AND purchase_date >= :date
    """, nativeQuery = true)
    int countDistinctUsersSince(@Param("shopId") Long shopId, @Param("date") LocalDateTime date);

    //find number of register user and active user
    @Query(value = "SELECT EXTRACT(MONTH FROM created_date) AS month, COUNT(user_id) " +
            "FROM users " +
            "WHERE referred_by_shop_id = :shopId " +
            "GROUP BY month " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlyRegisteredUsers(@Param("shopId") Long shopId);

    @Query(value = "SELECT EXTRACT(MONTH FROM purchase_date) AS month, COUNT(DISTINCT user_id) " +
            "FROM user_purchase_history " +
            "WHERE shop_id = :shopId " +
            "GROUP BY month " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlyVisitedUsers(@Param("shopId") Long shopId);
}

