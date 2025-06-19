package com.sts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	
	/*@PostMapping("saveUser")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User userDetails = userService.createUserWithQrCode(user);
		return new ResponseEntity<User>(userDetails, HttpStatus.CREATED);
	}*/
	
	// Create user and return user details + QR code
    @PostMapping("/createuser")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            Map<String, Object> response = userService.createUserWithQrCode(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user: " + e.getMessage());
        }
    }
    
    
 //  GET: Get user by QR token
    @GetMapping("/user-by-qr")
    public ResponseEntity<?> getUserByQrToken(@RequestParam("token") String token) {
        Optional<User> userOpt = userService.getUserByQrToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("email", user.getEmail());
            response.put("phoneNumber", user.getPhone());
            response.put("qrToken", user.getQrToken());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
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
    /*@PostMapping("/create")
    public ResponseEntity<User> createUser1(@RequestBody User user) {
        User response = userService.createUserWithQrCode(user);
        return ResponseEntity.ok(response);
    }*/
    
   /* DELETE
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/
 
}