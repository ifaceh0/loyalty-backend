package com.sts.controller;

import com.sts.dto.*;
import com.sts.service.ShopService;
import com.sts.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.service.EmailService;
import com.sts.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/registerUser")
	public ResponseEntity<AuthResponse> signupUser(@RequestBody @Valid UserSignupRequest request) {
		return ResponseEntity.ok(authService.signupUser(request));
	}

	@PostMapping("/registerShopkeeper")
	public ResponseEntity<AuthResponse> signupShop(@RequestBody @Valid ShopSignupRequest request) {
		return ResponseEntity.ok(authService.signupShop(request));
	}

	@PostMapping("/signIn")
	public ResponseEntity<AuthResponse> signin(@RequestBody @Valid SigninRequest request) {
		return ResponseEntity.ok(authService.signin(request));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid PasswordResetRequest request) throws Exception {
		authService.requestPasswordReset(request.getEmail());
		return ResponseEntity.ok("Password reset email sent");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
		authService.resetPassword(request.getEmail(), request.getNewPassword(), request.getResetToken());
		return ResponseEntity.ok("Password reset successful");
	}

}
