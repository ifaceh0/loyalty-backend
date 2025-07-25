package com.sts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sts.dto.ShopkeeperProfileDTO;
import com.sts.dto.ShopkeeperSettingDTO;
import com.sts.dto.UserDto;
import com.sts.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.entity.UserProfile;
import com.sts.repository.UserRepository;
import com.sts.service.ShopService;
import com.sts.service.UserService;


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

	@PostMapping("/update-setting")
	public ResponseEntity<Map<String, Object>> save(@RequestBody ShopkeeperSettingDTO request) {
		shopService.saveSetting(request);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Shopkeeper settings saved successfully");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-setting/{shopId}")
	public ResponseEntity<ShopkeeperSettingDTO> getSetting(@PathVariable Long shopId) {
		ShopkeeperSettingDTO setting = shopService.getSetting(shopId);
		return ResponseEntity.ok(setting);
	}


	@GetMapping("/get-profile")
	public ShopkeeperProfileDTO getProfile(@RequestParam Long shopId){
		return shopService.getProfile(shopId);
	}

	@PutMapping("/update-profile")
	public ResponseEntity<ShopkeeperProfileDTO> updatePersonal(@RequestBody ShopkeeperProfileDTO dto) {
		ShopkeeperProfileDTO updatedDto = shopService.updatePersonalProfile(dto);
		return ResponseEntity.ok(updatedDto);
	}

	/*@GetMapping("/email")
	public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
		try {
			return userRepository.findByEmail(email)
					.map(user -> {
						// ensure profiles list isn't null
						List<UserProfile> profiles = Optional.ofNullable(user.getUserProfiles())
								.orElse(List.of());
						UserProfile profile = profiles.isEmpty() ? null : profiles.get(0);


						Map<String, Object> userInfo = new HashMap<>();
						userInfo.put("userId", user.getUserId());
						userInfo.put("firstName", user.getFirstName());
						userInfo.put("lastName", user.getLastName());
						userInfo.put("email", user.getEmail());
						userInfo.put("phoneNumber", user.getPhone());
						userInfo.put("availablePoints", profile != null ? profile.getAvailablePoints() : 0);

						return ResponseEntity.ok(userInfo);
					})
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(Map.of("error", "User not found")));

		} catch (Exception ex) {
			ex.printStackTrace();  // important for troubleshooting
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Internal server error: " + ex.getMessage()));
		}
	}*/






	//@Transactional(readOnly = true)
//	@GetMapping("/userinfo-by-phone")
//	public ResponseEntity<Map<String, Object>> getUserByPhone(@RequestParam String phone) {
//		return userRepository.findByPhone(phone)
//				.map(user -> {
//					Long userId = user.getUserId();
//					Shop shop = user.getShop();
//					Long shopId = (shop != null) ? shop.getShopId() : null;
//					Optional<UserProfile> optionalProfile = Optional.ofNullable(userProfileRepository.findByUserIdAndShopId(userId, shopId));
//					Integer availablePoints = optionalProfile.map(UserProfile::getAvailablePoints).orElse(0);
//					Map<String, Object> userInfo = new HashMap<>();
//					userInfo.put("userId", userId);
//					userInfo.put("shopId", shopId);
//					userInfo.put("firstName", user.getFirstName());
//					userInfo.put("lastName", user.getLastName());
//					userInfo.put("email", user.getEmail());
//					userInfo.put("phoneNumber", user.getPhone());
//					userInfo.put("availablePoints", availablePoints); // ✅ Updated value
//
//					return ResponseEntity.ok(userInfo);
//				})
//				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(Map.of("error", "User not found")));
//	}


//
//@GetMapping("/email")
//	public ResponseEntity<Map<String, Object>> getUserByEmail(@RequestParam String email) {
//		return userRepository.findByPhone(email)
//				.map(user -> {
//					Long userId = user.getUserId();
//					Shop shop = user.getShop();
//					Long shopId = (shop != null) ? shop.getShopId() : null;
//					Optional<UserProfile> optionalProfile = Optional.ofNullable(userProfileRepository.findByUserIdAndShopId(userId, shopId));
//					Integer availablePoints = optionalProfile.map(UserProfile::getAvailablePoints).orElse(0);
//					Map<String, Object> userInfo = new HashMap<>();
//					userInfo.put("userId", userId);
//					userInfo.put("shopId", shopId);
//					userInfo.put("firstName", user.getFirstName());
//					userInfo.put("lastName", user.getLastName());
//					userInfo.put("email", user.getEmail());
//					userInfo.put("phoneNumber", user.getPhone());
//					userInfo.put("availablePoints", availablePoints); // ✅ Updated value
//
//					return ResponseEntity.ok(userInfo);
//				})
//				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(Map.of("error", "User not found")));
//	}
	@GetMapping("/search_by_phone")
	public UserDto searchByPhone(@RequestParam Long shopId, @RequestParam String phone) {
		return userService.findByPhoneInShop(phone, shopId);
}

	@GetMapping("/search_by_email")
	public UserDto searchByEmail(@RequestParam Long shopId, @RequestParam String email) {
		return userService.findByEmailInShop(email, shopId);
	}
}



