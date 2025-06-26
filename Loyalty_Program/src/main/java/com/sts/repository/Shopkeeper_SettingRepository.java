package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Shopkeeper_Setting;

import java.util.Optional;

public interface Shopkeeper_SettingRepository extends JpaRepository<Shopkeeper_Setting, Long>{
    Optional<Shopkeeper_Setting> findByShop_ShopId(Long shopId);

}
