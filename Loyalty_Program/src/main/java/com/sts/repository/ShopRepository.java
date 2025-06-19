package com.sts.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    boolean existsByCompanyEmail(String companyEmail);
    boolean existsByCompanyPhone(String companyPhone);
}