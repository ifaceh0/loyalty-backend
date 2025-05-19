package com.sts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sts.entity.Login;
import com.sts.repository.LoginRepository;

@Service
public class LoginService {
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public Login register(Login login) {
		if (loginRepository.existsByEmail(login.getEmail())) {
			throw new RuntimeException("Email already used !");
		}
		if (loginRepository.existsByPhoneNumber(login.getPhoneNumber())) {
			throw new RuntimeException("PhoneNumber already used !");
		}
		
//		if (!login.getPassword().equals(login.get)) {
//			
//		}
		
		//Password has been encrypted
		String encryptPassword = passwordEncoder.encode(login.getPassword());
		login.setPassword(encryptPassword);
		
		return loginRepository.save(login);
	}
}
