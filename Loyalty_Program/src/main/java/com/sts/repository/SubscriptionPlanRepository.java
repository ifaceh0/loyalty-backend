package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Subscription;
import com.sts.entity.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long>{
    SubscriptionPlan findTopBySubscriptionOrderByEndDateDesc(Subscription subscription);
}
