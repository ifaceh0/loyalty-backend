package com.springboot.loyalprogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.loyalprogram.entities.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>
{
	

}
