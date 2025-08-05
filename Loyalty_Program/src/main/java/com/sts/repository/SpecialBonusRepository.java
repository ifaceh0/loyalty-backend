package com.sts.repository;

import com.sts.entity.SpecialBonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SpecialBonusRepository extends JpaRepository<SpecialBonus, Long> {
    List<SpecialBonus> findByShop_ShopId(Long shopId);
    void deleteByShop_ShopId(Long shopId);

    //check specialBonous available or not
    @Query(value = "SELECT * FROM special_bonus sb WHERE sb.shop_id = :shopId AND :currentDate BETWEEN sb.start_date AND sb.end_date", nativeQuery = true)
    List<SpecialBonus> findActiveSpecialBonuses(@Param("shopId") Long shopId, @Param("currentDate") LocalDate currentDate);

}
