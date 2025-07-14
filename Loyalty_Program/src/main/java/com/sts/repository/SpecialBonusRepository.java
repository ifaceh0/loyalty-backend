package com.sts.repository;

import com.sts.entity.SpecialBonus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialBonusRepository extends JpaRepository<SpecialBonus, Long> {
    List<SpecialBonus> findByShop_ShopId(Long shopId);
    void deleteByShop_ShopId(Long shopId);
}
