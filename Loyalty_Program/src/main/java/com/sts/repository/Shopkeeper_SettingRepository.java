package com.sts.repository;

import com.sts.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Shopkeeper_Setting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Shopkeeper_SettingRepository extends JpaRepository<Shopkeeper_Setting, Long>{
    Optional<Shopkeeper_Setting> findByShop_ShopId(Long shopId);

    @Query("SELECT s FROM Shopkeeper_Setting s WHERE s.shop.id = :shopId")
    Shopkeeper_Setting findByShop(@Param("shopId") Long shopId);

}
