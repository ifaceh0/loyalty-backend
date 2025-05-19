package com.sts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
