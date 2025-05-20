package com.sts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.LoginDto;
import com.sts.entity.Login;
import com.sts.entity.Role;
import com.sts.service.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {
	@Autowired
	private LoginService loginService;
	
	@PostMapping("/registerUser")
	public ResponseEntity<Login> createUser(@RequestBody Login login){
		login.setRole(Role.USER);
		Login loginDetails = loginService.register(login);
		return ResponseEntity.ok(loginDetails);
	}
	
	@PostMapping("/registerShopkeeper")
	public ResponseEntity<Login> createShopkeeper(@RequestBody Login login){
		login.setRole(Role.SHOPKEEPER);
		Login loginDetails = loginService.register(login);
		return ResponseEntity.ok(loginDetails);
	}
	
	@PostMapping("/signIn")
	public ResponseEntity<?> loginValidation(@RequestBody LoginDto dto){
		Login login = loginService.fetchByEmail(dto.getEmail());
		
		if (login == null) {
			return ResponseEntity.status(404).body("User not found");
		}
		
		if (!login.getPassword().equals(dto.getPassword())) {
			return ResponseEntity.status(401).body("Invalid password");
		}
		return ResponseEntity.ok(login);
	}
}
