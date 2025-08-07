package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.UserProfile;
import com.sts.entity.UserProfileId;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, UserProfileId> {
    UserProfile findByUserIdAndShopId(Long userId, Long shopId);
    Optional<UserProfile> findByUser_UserIdAndShop_ShopId(Long userId, Long shopId);

    List<UserProfile> findByUserId(Long userId);
}

