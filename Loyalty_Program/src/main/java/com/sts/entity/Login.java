package com.sts.entity;

import com.sts.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Login {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String phone;
	@Column(nullable = false, unique = true)
	private String email;
	private String password;
	
	 @Enumerated(EnumType.STRING)
	 @Column(nullable = false)
	 private Role role;
	 
	@Column(name = "reset_token")
	private String resetToken;

	@Column(name = "reset_token_expiry")
	private LocalDateTime resetTokenExpiry;
}
