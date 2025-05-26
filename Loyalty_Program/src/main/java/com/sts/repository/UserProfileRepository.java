package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.UserProfile;
import com.sts.entity.UserProfileId;

public interface UserProfileRepository extends JpaRepository<UserProfile, UserProfileId>
{
	

}

