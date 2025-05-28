package com.sts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.Shop;

import com.sts.entity.UserProfile;
import com.sts.repository.UserRepository;
import com.sts.service.ShopService;

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
	
/*	//for Phonenumber fetching data
	@GetMapping("/userInfo")
	    public ResponseEntity<?> getUserByPhoneNumber(@RequestParam String phoneNumber) {
	        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

	        if (userOpt.isPresent()) {
	            User user = userOpt.get();
	            Map<String, String> userInfo = new HashMap<>();
	            userInfo.put("firstName", user.getFirstName());
	            userInfo.put("lastName", user.getLastName());
	            userInfo.put("phoneNumber", user.getPhoneNumber());
	            userInfo.put("email", user.getEmail());
	            userInfo.put("totalPoints", profile != null ? profile.getTotalPoints() : 0);
	            return ResponseEntity.ok(userInfo);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("User not found with phone number: " + phoneNumber);
	        }
	    }
	
	
	//For E-mail fetching data
	@GetMapping("/userinfo-by-email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("firstName", user.getFirstName());
            userInfo.put("lastName", user.getLastName());
            userInfo.put("phoneNumber", user.getPhoneNumber());
            userInfo.put("email", user.getEmail());

            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with email: " + email);
        }
    }
	*/
	 @GetMapping("/userinfo-by-email")
	    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
	        return userRepository.findByEmail(email)
	                .map(user -> {
	                    UserProfile profile = user.getUserProfile();

	                    Map<String, Object> userInfo = new HashMap<>();
	                    userInfo.put("firstName", user.getFirstName());
	                    userInfo.put("lastName", user.getLastName());
	                    userInfo.put("email", user.getEmail());
	                    userInfo.put("phoneNumber", user.getPhoneNumber());
	                    userInfo.put("totalPoints", profile != null ? profile.getTotalPoints() : 0);

	                    return ResponseEntity.ok(userInfo);
	                })
	                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found")));
	    }

	 @GetMapping("/userinfo-by-phone")
	 public ResponseEntity<Map<String, Object>>  getUserByPhone(@RequestParam String phoneNumber) {
	     return userRepository.findByPhoneNumber(phoneNumber)
	             .map(user -> {
	                 UserProfile profile = user.getUserProfile();

	                 
	                 
	                 Map<String, Object> userInfo = new HashMap<>();
	                 userInfo.put("firstName", user.getFirstName());
	                 userInfo.put("lastName", user.getLastName());
	                 userInfo.put("email", user.getEmail());
	                 userInfo.put("phoneNumber", user.getPhoneNumber());
	                 userInfo.put("totalPoints", profile != null ? profile.getTotalPoints() : 0);

	                 return ResponseEntity.ok(userInfo);
	             })
	             .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                   .body(Map.of("error", "User not found")));
	 }

}
