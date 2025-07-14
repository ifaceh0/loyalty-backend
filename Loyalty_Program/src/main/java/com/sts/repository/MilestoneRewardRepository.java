package com.sts.repository;

import com.sts.entity.MilestoneReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRewardRepository extends JpaRepository<MilestoneReward, Long> {
    List<MilestoneReward> findByShop_ShopId(Long shopId);
    void deleteByShop_ShopId(Long shopId);
}
