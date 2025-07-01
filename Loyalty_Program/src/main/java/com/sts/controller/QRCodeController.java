package com.sts.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.QrcodeGenerator.QrCodeGenerator;
import com.sts.dto.AddPointsRequest;
import com.sts.dto.PurchaseRequestDTO;
import com.sts.entity.*;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserProfileRepository;
import com.sts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sts.service.QRCodeService;

import java.time.LocalDateTime;
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
		qrData.put("shopId", shop.getShopId());
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
		try {
			// Extract required fields from QR code
			String email = (String) qrData.get("email");
			Long shopId = Long.valueOf(String.valueOf(qrData.get("shopId")));

			// Validate input
			if (email == null || shopId == null) {
				return ResponseEntity.badRequest().body("Invalid QR data: Missing email or shop ID.");
			}

			// Fetch user by email
			Optional<User> userOpt = userRepository.findByEmail(email);
			if (userOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found in the system.");
			}

			User user = userOpt.get();

			// Check if user is associated with this shop
			UserProfile profile = userProfileRepository.findByUserIdAndShopId(user.getUserId(), shopId);
//			if (profile == null) {
//				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not associated with this shop.");
//			}

			// Fetch balance from profile
			int verifiedBalance = profile.getAvailablePoints() != null ? profile.getAvailablePoints() : 0;

			// Prepare response
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("userId", user.getUserId());
			response.put("userName", user.getFirstName() + " " + user.getLastName());
			response.put("email", user.getEmail());
			response.put("phone", user.getPhone());
			response.put("verifiedBalance", verifiedBalance);
			response.put("associated", profile != null);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing QR code: " + e.getMessage());
		}
	}

	@PostMapping("/add-points")
	public ResponseEntity<?> addPointsToUserProfile(@RequestBody AddPointsRequest request) {
		try {
			Long userId = request.getUserId();
			Long shopId = request.getShopId();
			Integer pointsToAdd = request.getPointsToAdd();

			// Validate input
			if (userId == null || shopId == null || pointsToAdd == null || pointsToAdd <= 0) {
				return ResponseEntity.badRequest().body("Invalid input: All fields are required and points must be > 0.");
			}

//			// Check if user and shop exist
//			Optional<User> userOpt = userRepository.findById(userId);
//			Optional<Shop> shopOpt = shopRepository.findById(shopId);
//			if (userOpt.isEmpty() || shopOpt.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user or shop ID.");
//			}

			UserProfile profile = userProfileRepository.findByUserIdAndShopId(userId, shopId);

			// Create new profile if not present
			if (profile == null) {
				profile = new UserProfile();
				profile.setUserId(userId);
				profile.setShopId(shopId);
				profile.setAvailablePoints(0);
				profile.setCreatedAt(LocalDateTime.now());
			}

			// Update balance and timestamp
//			int currentPoints = profile.getAvailablePoints() != null ? profile.getAvailablePoints() : 0;
			int currentPoints = profile.getAvailablePoints();
			profile.setAvailablePoints(currentPoints + pointsToAdd);
			profile.setUpdatedAt(LocalDateTime.now());

			// Save profile
			userProfileRepository.save(profile);

			return ResponseEntity.ok(Map.of(
					"message", "Points added successfully",
					"newBalance", profile.getAvailablePoints()
			));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error adding points: " + e.getMessage());
		}
	}

	@PostMapping("/add-dollars")
	public ResponseEntity<Map<String, String>> savePurchase(@RequestBody PurchaseRequestDTO request) {
		try {
			qrCodeService.savePurchase(request);
			return ResponseEntity.ok(Map.of("message", "Purchase history saved successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(Map.of("message", "An unexpected error occurred."));
		}
	}
}