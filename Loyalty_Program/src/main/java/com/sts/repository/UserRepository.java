package com.sts.repository;

import java.util.Optional;

import com.sts.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sts.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByPhone(String phone);
	Optional<User> findByEmail(String email);
	Optional<User> findByQrToken(String qrToken);
//	Optional<User> findShopByShopId(Long shopId);

	boolean existsByEmail(String email);
	boolean existsByPhone(String phone);
}