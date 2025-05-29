package com.sts.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.sts.entity.Login;
import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.enums.Role;
import com.sts.repository.LoginRepository;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserRepository;

@Service
public class LoginService {
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private UserRepository userRepository;
	
	// public Login register(Login login) {
	// 	if (loginRepository.existsByEmail(login.getEmail())) {
	// 		throw new RuntimeException("Email already used !");
	// 	}
	// 	if (loginRepository.existsByPhoneNumber(login.getPhoneNumber())) {
	// 		throw new RuntimeException("PhoneNumber already used !");
	// 	}
	// 	return loginRepository.save(login);
	// }
	
	public Login fetchByEmail(String email) {
		return loginRepository.findByEmail(email);
	}
	
	public Login fetchByResetToken(String resetToken) {
		return loginRepository.findByResetToken(resetToken);
	}

	public void save(Login login) {
		loginRepository.save(login);
	}
	
	public Shop createShopkeeperAndLogin(Login login) {
		if (loginRepository.existsByEmail(login.getEmail())) {
			throw new RuntimeException("Email already used !");
		}
		if (loginRepository.existsByPhoneNumber(login.getPhoneNumber())) {
			throw new RuntimeException("PhoneNumber already used !");
		}
        // Save Login entity (should already have all fields set)
        login.setRole(Role.SHOPKEEPER);
        loginRepository.save(login);

        // Create Shop entity with only email
        Shop shop = new Shop();
        shop.setEmail(login.getEmail()); // Store only email in shop at registration
        shopRepository.save(shop);

        return shop;
    }

	public User createUserAndLogin(Login login) {
		if (loginRepository.existsByEmail(login.getEmail())) {
			throw new RuntimeException("Email already used !");
		}
		if (loginRepository.existsByPhoneNumber(login.getPhoneNumber())) {
			throw new RuntimeException("PhoneNumber already used !");
		}
        // Save Login entity 
        login.setRole(Role.USER);
        loginRepository.save(login);

        // Create User entity with only email
        User user = new User();
        user.setEmail(login.getEmail()); // Store only email in user at registration
        userRepository.save(user);

        return user;
    }
}
