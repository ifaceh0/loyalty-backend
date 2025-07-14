package com.sts.repository;

import com.sts.entity.PurchaseReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRewardRepository extends JpaRepository<PurchaseReward, Long> {
    List<PurchaseReward> findByShop_ShopId(Long shopId);
    void deleteByShop_ShopId(Long shopId);
}
