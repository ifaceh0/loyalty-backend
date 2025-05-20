package com.sts.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.sts.entity.Login;
import com.sts.repository.LoginRepository;

@Service
public class LoginService {
	@Autowired
	private LoginRepository loginRepository;
	
	public Login register(Login login) {
		if (loginRepository.existsByEmail(login.getEmail())) {
			throw new RuntimeException("Email already used !");
		}
		if (loginRepository.existsByPhoneNumber(login.getPhoneNumber())) {
			throw new RuntimeException("PhoneNumber already used !");
		}
		return loginRepository.save(login);
	}
	
	public Login fetchByEmail(String email) {
		return loginRepository.findByEmail(email);
	}
}
