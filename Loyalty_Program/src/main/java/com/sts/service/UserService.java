package com.sts.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.User;
import com.sts.repository.UserRepository;


@Service
public class UserService {
	@Autowired
	private UserRepository userService;
	
	

	public User createUser(User user) {
		
		return userService.save(user);
	}
	
//	public Optional<User> getUserById(Long id){
//		return userService.findById(id);
//	}
}
