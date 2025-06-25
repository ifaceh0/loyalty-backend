package com.sts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sts.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.entity.UserProfile;
import com.sts.repository.UserRepository;
import com.sts.service.ShopService;
import com.sts.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/shop")
public class ShopController {
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	
	@PostMapping("/saveShop")
	public ResponseEntity<Shop> createShop(@RequestBody Shop shop) {
		Shop shopDetails = shopService.createShop(shop);
		return new ResponseEntity<Shop>(shopDetails, HttpStatus.CREATED);
	}
	
	@GetMapping("/fetchAllShop")
	public ResponseEntity<List<Shop>> getAllShopList(){
		List<Shop> shopList = shopService.getAllShop();
		return new ResponseEntity<List<Shop>>(shopList, HttpStatus.OK);
	}
	
	@GetMapping("/fetchShopById/{shopId}")
	public ResponseEntity<Shop> getShopById(@PathVariable Long shopId){
		return shopService.getShopById(shopId)
				.map(shop -> ResponseEntity.ok(shop))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(null));	
	}
	

//	 @GetMapping("/userinfo-by-email")
//	    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
//	        return userRepository.findByEmail(email)
//	                .map(user -> {
//	                    UserProfile profile = user.getUserProfile();
//
//	                    Map<String, Object> userInfo = new HashMap<>();
//	                    userInfo.put("firstName", user.getFirstName());
//	                    userInfo.put("lastName", user.getLastName());
//	                    userInfo.put("email", user.getEmail());
//	                    userInfo.put("phoneNumber", user.getPhone());
//	                    userInfo.put("availablePoints", profile != null ? profile.getAvailablePoints() : 0);
//
//	                    return ResponseEntity.ok(userInfo);
//	                })
//	                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found")));
//	    }
//
//	 @GetMapping("/userinfo-by-phone")
//	 public ResponseEntity<Map<String, Object>>  getUserByPhone(@RequestParam String phone) {
//	     return userRepository.findByPhone(phone)
//	             .map(user -> {
//	                 UserProfile profile = user.getUserProfile();
//
//
//
//	                 Map<String, Object> userInfo = new HashMap<>();
//	                 userInfo.put("firstName", user.getFirstName());
//	                 userInfo.put("lastName", user.getLastName());
//	                 userInfo.put("email", user.getEmail());
//	                 userInfo.put("phoneNumber", user.getPhone());
//	                 userInfo.put("availablePoints", profile != null ? profile.getAvailablePoints() : 0);
//
//	                 return ResponseEntity.ok(userInfo);
//	             })
//	             .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                                   .body(Map.of("error", "User not found")));
//	 }
	 
	 /*@GetMapping("/user-by-qr")
	 public ResponseEntity<?> getUserByQrCode(@RequestParam String code) {
	     Optional<User> userOpt = userRepository.findByQrToken(code);

	     return userOpt
	             .map(user -> {
	                 UserProfile profile = user.getUserProfile();
	                 Map<String, Object> userInfo = new HashMap<>();
	                 userInfo.put("firstName", user.getFirstName());
	                 userInfo.put("lastName", user.getLastName());
	                 userInfo.put("email", user.getEmail());
	                 userInfo.put("phoneNumber", user.getPhoneNumber());
	                 userInfo.put("totalPoints", profile != null ? profile.getAvailablePoints() : 0);
	                 return ResponseEntity.ok(userInfo);
	             })
	             .orElse( ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found")));
	 }*/
	 
	 
	/* @SuppressWarnings("unchecked")
	@PostMapping("/create-user")
	    public ResponseEntity<?> createUser(@RequestBody User user) {
	        Map<String, Object> result = (Map<String, Object>) userService.createUser(user);
	        return ResponseEntity.ok(result);
	    }*/

	   /* @GetMapping("/user-by-qr")
	    public ResponseEntity<?> getUserByQr(@RequestParam ("token")String token) {
	        return userService.getUserByQrToken(token)
	                .map(user -> {
	                    Map<String, Object> userInfo = new HashMap<>();
	                    userInfo.put("firstName", user.getFirstName());
	                    userInfo.put("lastName", user.getLastName());
	                    userInfo.put("email", user.getEmail());
	                    userInfo.put("phoneNumber", user.getPhoneNumber());
	                    return ResponseEntity.ok(userInfo);
	                })
	                .orElse(ResponseEntity.notFound().build());
	    }*/
	 
	 
	 
	 
//	 @GetMapping("/user-by-qr")
//	 public ResponseEntity<?> getUserByQrToken(@RequestParam("token") String token) {
//	     Optional<User> userOptional = userService.getUserByQrToken(token);
//	     if (userOptional.isPresent()) {
//	         User user = userOptional.get();
//	         Map<String, Object> response = new HashMap<>();
//	         response.put("firstName", user.getFirstName());
//	         response.put("lastName", user.getLastName());
//	         response.put("email", user.getEmail());
//	         response.put("phoneNumber", user.getPhone());
//	         response.put("qrToken", user.getQrToken());
//	         return ResponseEntity.ok(response);
//	     } else {
//	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for provided QR token.");
//	     }
//	 }

	}



