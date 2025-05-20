package com.sts.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@PostMapping("saveUser")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User userDetails = userService.createUser(user);
		return new ResponseEntity<User>(userDetails, HttpStatus.CREATED);
	}
	
//	@GetMapping("/fetchUserById/{userId}")
//	public ResponseEntity<User> getUserById(@PathVariable Long userId){
//		return userService.getUserById(userId)
//				.map(user -> ResponseEntity.ok(user))
//				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(null));	
//	}
	// READ ALL
    @GetMapping("/fetchAllUser")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    //READ ONE BY ID
    @GetMapping("/fetchUserById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // UPDATE
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    // DELETE
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
