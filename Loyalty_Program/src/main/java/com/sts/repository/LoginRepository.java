package com.sts.repository;

import com.sts.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Login;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Long>{
    Optional<Login> findByEmail(String email);
    Optional<Login> findByResetToken(String resetToken);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Login> findByRefIdAndRole(Long refId, Role role);
}
