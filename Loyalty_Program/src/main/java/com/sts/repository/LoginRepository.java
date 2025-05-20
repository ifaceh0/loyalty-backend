package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Long>{
	boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Login findByEmail(String email);
}
