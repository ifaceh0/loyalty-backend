package com.sts.repository;

import com.sts.entity.Subscription;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByShopId(Long shopId);
    
    Optional<Subscription> findByShop_ShopIdAndStatus(Long shopId, String status);
}
