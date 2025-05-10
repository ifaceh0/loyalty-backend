package com.sts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.User;
import com.sts.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@PostMapping("saveUser")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User userDetails = userService.createUser(user);
		return new ResponseEntity<User>(userDetails, HttpStatus.CREATED);
	}
	
	
}
