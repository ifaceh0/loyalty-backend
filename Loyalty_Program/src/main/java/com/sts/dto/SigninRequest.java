package com.sts.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class SigninRequest {
	@Email
	private String email;
	private String password;
}
