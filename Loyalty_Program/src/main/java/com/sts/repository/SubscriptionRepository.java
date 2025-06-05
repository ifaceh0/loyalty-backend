package com.sts.repository;

import com.sts.entity.Subscription;
import com.sts.enums.SubscriptionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByShop_ShopId(Long shopId);
    
    Optional<Subscription> findByShop_ShopIdAndStatus(Long shopId, SubscriptionStatus status);

    List<Subscription> findByStatusAndAutoRenewTrueAndEndDateBefore(SubscriptionStatus active, LocalDateTime now);
}
