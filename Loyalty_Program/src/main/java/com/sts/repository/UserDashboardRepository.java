package com.sts.repository;

import com.sts.dto.UserDashboardDTO;
import com.sts.entity.UserPurchase_History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDashboardRepository extends JpaRepository<UserPurchase_History, Long> {
    @Query(value = """
        SELECT purchase_date, transaction_amount 
        FROM user_purchase_history 
        WHERE user_id = :userId 
        ORDER BY purchase_date DESC
        """, nativeQuery = true)
    List<Object[]> findUserTransactions(@Param("userId") Long userId);
}
