package com.sts.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.QrcodeGenerator.QrCodeGenerator;
import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.entity.UserProfile;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserProfileRepository;
import com.sts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sts.service.QRCodeService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {
	@Autowired
	private QRCodeService qrCodeService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;
	// code for show your qrcode in postman
//	@GetMapping("/qr")
//	public ResponseEntity<byte[]> getQRCode(@RequestParam String email) {
//		try {
//			byte[] qrImage = qrCodeService.generateUserQRCode(email);
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.IMAGE_PNG);
//
//			return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

//	@PostMapping("/generate/{shopId}")
//	public ResponseEntity<Map<String, Object>> generateQRCode(@PathVariable Long shopId) throws WriterException, IOException {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String email = authentication.getName();
//		Long userId = getUserIdFromEmail(email); // Implement this
//		Map<String, Object> response = qrCodeService.generateQRCode(userId, shopId);
//		return ResponseEntity.ok(response);
//	}
//
//	@GetMapping("/scan/{qrToken}")
//	public ResponseEntity<Map<String, Object>> scanQRCode(@PathVariable String qrToken) {
//		Map<String, Object> userDetails = qrCodeService.getUserDetailsFromQRToken(qrToken);
//		return ResponseEntity.ok(userDetails);
//	}
//
//	private Long getUserIdFromEmail(String email) {
//		return userRepository.findByEmail(email)
//				.map(user -> user.getUserId())
//				.orElseThrow(() -> new RuntimeException("User not found"));
//	}
//
//	@GetMapping("/shops")
//	@PreAuthorize("isAuthenticated()")
//	public ResponseEntity<List<ShopResponseDto>> getAllShops(Authentication authentication) {
//		// Fetch all shops from the database
//		List<Shop> shops = shopRepository.findAll();
//
//		// Map to DTO
//		List<ShopResponseDto> shopDtos = shops.stream().map(shop -> {
//			ShopResponseDto dto = new ShopResponseDto();
//			dto.setShopId(shop.getShopId());
//			dto.setShopName(shop.getShopName());
//			dto.setPhone(shop.getPhone());
//			return dto;
//		}).collect(Collectors.toList());
//
//		return ResponseEntity.ok(shopDtos);
//	}

	@GetMapping("/allShops")
	public ResponseEntity<?> getAllShopsForUser(@RequestParam Long userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

		List<Map<String, Object>> response = shopRepository.findAll().stream().map(shop -> {
			Map<String, Object> map = new HashMap<>();
			map.put("shopId", shop.getShopId());
			map.put("shopName", shop.getShopName());
			map.put("shopPhone", shop.getPhone());
			map.put("customerId", userId);
			return map;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/generate")
	public ResponseEntity<?> getShopQRCodeForUser(
			@RequestParam Long shopId,
			@RequestParam Long userId) throws Exception {

		Optional<User> userOpt = userRepository.findById(userId);
		Optional<Shop> shopOpt = shopRepository.findById(shopId);

		if (userOpt.isEmpty() || shopOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		User user = userOpt.get();
		Shop shop = shopOpt.get();

		UserProfile profile = userProfileRepository.findByUserIdAndShopId(userId, shopId);
		Integer availableBalance = (profile != null) ? profile.getAvailablePoints() : 0;

		Map<String, Object> qrData = new LinkedHashMap<>();
		qrData.put("userName", user.getFirstName() + " " + user.getLastName());
		qrData.put("email", user.getEmail());
		qrData.put("phone", user.getPhone());
		qrData.put("customerId", user.getUserId());
		qrData.put("shopName", shop.getShopName());
		qrData.put("availableBalance", availableBalance);

		String jsonContent = new ObjectMapper().writeValueAsString(qrData);
		String qrBase64 = QrCodeGenerator.generateQRCodeBase64(jsonContent, 250, 250);

		Map<String, Object> response = new HashMap<>();
		response.put("shopId", shopId);
		response.put("shopName", shop.getShopName());
		response.put("customerId", userId);
		response.put("availableBalance", availableBalance);
		response.put("qrCodeImage", "data:image/png;base64," + qrBase64);
		response.put("qrRawData", qrData);

		return ResponseEntity.ok(response);
	}


	@PostMapping("/decode")
	public ResponseEntity<?> processScannedQRCode(@RequestBody Map<String, Object> qrData) {
		String userName = (String) qrData.get("userName");
		String email = (String) qrData.get("email");
		String phone = (String) qrData.get("phone");
		Integer availableBalance = (Integer) qrData.get("availableBalance");

		User user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body("User not found in system");
		}

		UserProfile profile = userProfileRepository.findByUserIdAndShopId(
				user.getUserId(), user.getShop().getShopId()
		);

		int verifiedBalance = (profile != null) ? profile.getAvailablePoints() : 0;

		return ResponseEntity.ok(Map.of(
				"userId", user.getUserId(),
				"userName", userName,
				"email", email,
				"phone", phone,
				"verifiedBalance", verifiedBalance
		));
	}

}