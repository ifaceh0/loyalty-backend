package com.sts.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Shop;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Shop> findByEmail(String email);
    boolean existsByCompanyEmail(String companyEmail);
    boolean existsByCompanyPhone(String companyPhone);
}