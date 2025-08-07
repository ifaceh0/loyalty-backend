package com.sts.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.QrcodeGenerator.QrCodeGenerator;
import com.sts.dto.AddPointsRequest;
import com.sts.dto.PurchaseRequestDTO;
import com.sts.entity.*;
import com.sts.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sts.service.QRCodeService;

import java.time.LocalDate;
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

    @Autowired
    private UserPurchaseHistory_Repository historyRepository;

	@Autowired
	private Shopkeeper_SettingRepository shopkeeperSettingRepository;
  
  @Autowired
	private SpecialBonusRepository specialBonusRepository;

	@GetMapping("/allShopsAvailable")
	public ResponseEntity<?> getAllShopsAvailable(@RequestParam Long userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

		List<Map<String, Object>> response = shopRepository.findAll().stream().map(shop -> {
			Map<String, Object> map = new HashMap<>();
			map.put("shopId", shop.getShopId());
			map.put("shopName", shop.getShopName());
			map.put("shopPhone", shop.getPhone());
			map.put("country", shop.getCountry());
			map.put("city", shop.getCity());
			map.put("customerId", userId);
			return map;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/userSpecificShop")
	public ResponseEntity<?> getAllShopsForSpecificUser(@RequestParam Long userId) {
		// Verify user exists
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// Fetch UserProfile records for the user
		List<UserProfile> userProfiles = userProfileRepository.findByUserId(userId);
		if (userProfiles.isEmpty()) {
			return ResponseEntity.ok().body(List.of()); // Return empty list if no shops visited
		}

		// Map UserProfile to response format
		List<Map<String, Object>> response = userProfiles.stream()
				.map(userProfile -> {
					Shop shop = userProfile.getShop();
					Map<String, Object> map = new HashMap<>();
					map.put("shopId", shop.getShopId());
					map.put("shopName", shop.getShopName());
					map.put("shopPhone", shop.getPhone());
					map.put("country", shop.getCountry());
					map.put("city", shop.getCity());
					map.put("customerId", userId);
					return map;
				})
				.collect(Collectors.toList());

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
			Integer dollarAmount = request.getDollarAmount();

			if (userId == null || shopId == null || dollarAmount == null || dollarAmount <= 0) {
				return ResponseEntity.badRequest().body("Invalid input: userId, shopId, and dollarAmount are required and must be > 0.");
			}
			// 1. Determine correct dollarToPointsMapping
			double dollarToPoints = 1.0; // default fallback

			List<SpecialBonus> activeBonuses = specialBonusRepository.findActiveSpecialBonuses(shopId, LocalDate.now());
			if (!activeBonuses.isEmpty()) {
				// Use the first active bonus
				dollarToPoints = activeBonuses.get(0).getDollartoPointsMapping();
			} else {
				//if there is now special bonus available
				Shopkeeper_Setting setting = shopkeeperSettingRepository.findByShop_ShopId(shopId)
						.orElseThrow(() -> new RuntimeException("No setting found for shop " + shopId));
				dollarToPoints = setting.getDollarToPointsMapping() != null ? setting.getDollarToPointsMapping() : 1.0;
			}

			// 2. Calculate points
			int calculatedPoints = (int) (dollarAmount * dollarToPoints);
			// 3. Fetch or create UserProfile
			UserProfile profile = userProfileRepository.findByUserIdAndShopId(userId, shopId);
			if (profile == null) {
				// First transaction → apply signup bonus
				Shopkeeper_Setting setting = shopkeeperSettingRepository.findByShop_ShopId(shopId)
						.orElseThrow(() -> new RuntimeException("No setting found for shop " + shopId));
				int signupBonus = (int) setting.getSign_upBonuspoints();
				profile = new UserProfile();
				profile.setUserId(userId);
				profile.setShopId(shopId);
				profile.setAvailablePoints(signupBonus + calculatedPoints);
				profile.setCreatedAt(LocalDateTime.now());
			} else {
				int currentPoints = profile.getAvailablePoints();
				profile.setAvailablePoints(currentPoints + calculatedPoints);
			}
			profile.setUpdatedAt(LocalDateTime.now());
			userProfileRepository.save(profile);

			// 4. Record in UserPurchase_History
			User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new RuntimeException("Shop not found"));

			UserPurchase_History history = new UserPurchase_History();
			history.setUser(user);
			history.setShop(shop);
			history.setTransactionAmount(dollarAmount);
			history.setGivenPoints(calculatedPoints);
			history.setPurchaseDate(LocalDateTime.now());

			historyRepository.save(history);

			return ResponseEntity.ok(Map.of(
					"message", "Points added successfully based on mapping.",
					"earnedPoints", calculatedPoints,
					"newBalance", profile.getAvailablePoints()
			));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error adding points: " + e.getMessage());
		}
	}

}