package com.sts.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
	private Long userId;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private LocalDateTime createdDate;
	private LocalDateTime lastUpdatedDate;

	private UserProfileDto profile;
}
