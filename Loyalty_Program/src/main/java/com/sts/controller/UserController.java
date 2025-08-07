package com.sts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sts.dto.UserDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.UserDto;
//import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.repository.UserRepository;
import com.sts.service.UserService;


@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;

	// Create user and return user details + QR code
//    @PostMapping("/createuser")
//    public ResponseEntity<?> createUser(@RequestBody User user) {
//        try {
//            Map<String, Object> response = userService.createUserWithQrCode(user);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user: " + e.getMessage());
//        }
//    }

	// READ ALL
    @GetMapping("/fetchAllUser")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    //READ ONE BY ID
    @GetMapping("/fetchUserById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // UPDATE
//    @PutMapping("/updateUser/{userId}")
//    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
//        User updatedUser = userService.updateUser(id, userDetails);
//        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//    }

    //update user profile
    @PutMapping("/update-profile")
    public UserDetailsDTO updateUserProfile(@RequestBody UserDetailsDTO dto) {
        return userService.updateUserProfile(dto);
    }

    @GetMapping("/get-profile")
    public UserDetailsDTO getUserDetails(@RequestParam Long userId) {
        return userService.getUserDetails(userId);
    }
}