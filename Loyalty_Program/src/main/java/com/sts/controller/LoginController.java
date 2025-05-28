package com.sts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.LoginDto;
import com.sts.dto.ResetPasswordDto;
import com.sts.entity.Login;
import com.sts.enums.Role;
import com.sts.service.EmailService;
import com.sts.service.LoginService;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("/registerUser")
	public ResponseEntity<?> createUser(@RequestBody Login login){
		if (login.getPhoneNumber() == null || login.getPhoneNumber().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Phone number is required"));
		}
		if (login.getEmail() == null || login.getEmail().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
		}
		if (login.getPassword() == null || login.getPassword().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
		}
		login.setRole(Role.USER);
		Login loginDetails = loginService.register(login);
		return ResponseEntity.ok(loginDetails);
	}
	
	@PostMapping("/registerShopkeeper")
	public ResponseEntity<?> createShopkeeper(@RequestBody Login login){
		if (login.getPhoneNumber() == null || login.getPhoneNumber().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Phone number is required"));
		}
		if (login.getEmail() == null || login.getEmail().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
		}
		if (login.getPassword() == null || login.getPassword().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
		}
		login.setRole(Role.SHOPKEEPER);
		Login loginDetails = loginService.register(login);
		return ResponseEntity.ok(loginDetails);
	}
	
	@PostMapping("/signIn")
	public ResponseEntity<?> loginValidation(@RequestBody LoginDto dto){
		Login login = loginService.fetchByEmail(dto.getEmail());
		
		if (login == null) {
			return ResponseEntity.status(404).body(Map.of("message", "User not found"));
		}
		
		if (!login.getPassword().equals(dto.getPassword())) {
			return ResponseEntity.status(401).body(Map.of("message", "Invalid password"));
		}
		return ResponseEntity.ok(login);
	}
	
	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		if (email == null || email.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
		}
		Login login = loginService.fetchByEmail(email);
		if (login == null) {
			return ResponseEntity.status(404).body(Map.of("message", "User not found"));
		}
		// Generate a secure random reset token
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[32];
		random.nextBytes(bytes);
		String resetToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
		// Set expiry to 15 minutes from now
		long expiry = Instant.now().plusSeconds(15 * 60).toEpochMilli();
		login.setResetToken(resetToken);
		login.setResetTokenExpiry(expiry);
		loginService.save(login);
		// Send resetToken to user's email
		String resetLink = "https://loyalty-frontend-mu.vercel.app/reset-password?token=" + resetToken;
		emailService.sendSimpleMessage(email, "Password Reset Request", "Click the link to reset your password: " + resetLink);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Password reset link sent to email");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/reset-Password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {
		String token = dto.getToken();
		String newPassword = dto.getNewPassword();
		if (token == null || newPassword == null || newPassword.isEmpty()) {
			return ResponseEntity.badRequest().body("Token and new password are required");
		}
		Login login = loginService.fetchByResetToken(token);
		if (login == null || login.getResetTokenExpiry() == null || login.getResetTokenExpiry() < System.currentTimeMillis()) {
			return ResponseEntity.status(400).body("Invalid or expired token");
		}
		login.setPassword(newPassword);
		login.setResetToken(null);
		login.setResetTokenExpiry(null);
		loginService.save(login);
		return ResponseEntity.ok("Password reset successful");
	}
}
