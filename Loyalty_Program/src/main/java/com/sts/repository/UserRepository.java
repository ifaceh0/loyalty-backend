package com.sts.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sts.entity.User;

public interface UserRepository extends JpaRepository<User, Long> 
{
	Optional<User> findByPhoneNumber(String phoneNumber);
	Optional<User> findByEmail(String email);

}