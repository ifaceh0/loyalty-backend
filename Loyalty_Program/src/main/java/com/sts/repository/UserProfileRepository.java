package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>
{
	

}

