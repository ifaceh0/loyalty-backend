package com.sts.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {}