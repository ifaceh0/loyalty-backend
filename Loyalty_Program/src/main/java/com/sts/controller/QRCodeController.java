package com.sts.controller;
import com.google.zxing.WriterException;
import com.sts.dto.ShopResponseDto;
import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.entity.UserProfile;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserProfileRepository;
import com.sts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.sts.service.QRCodeService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

	@PostMapping("/generate/{shopId}")
	public ResponseEntity<Map<String, Object>> generateQRCode(@PathVariable Long shopId) throws WriterException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Long userId = getUserIdFromEmail(email); // Implement this
		Map<String, Object> response = qrCodeService.generateQRCode(userId, shopId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/scan/{qrToken}")
	public ResponseEntity<Map<String, Object>> scanQRCode(@PathVariable String qrToken) {
		Map<String, Object> userDetails = qrCodeService.getUserDetailsFromQRToken(qrToken);
		return ResponseEntity.ok(userDetails);
	}

	private Long getUserIdFromEmail(String email) {
		return userRepository.findByEmail(email)
				.map(User::getUserId)
				.orElseThrow(() -> new RuntimeException("User not found for email: " + email));
	}

	@GetMapping("/shops")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ShopResponseDto>> getAllShops(Authentication authentication) {
		String email = authentication.getName();
		Long userId = getUserIdFromEmail(email);
		List<UserProfile> userProfiles = userProfileRepository.findByUserId(userId);
		List<Long> shopIds = userProfiles.stream().map(UserProfile::getShopId).toList();
		List<Shop> shops = shopRepository.findAllById(shopIds);

		// Map to DTO
		List<ShopResponseDto> shopDtos = shops.stream().map(shop -> {
			ShopResponseDto dto = new ShopResponseDto();
			dto.setShopId(shop.getShopId());
			dto.setShopName(shop.getShopName());
			dto.setPhone(shop.getPhone());
			return dto;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(shopDtos);
	}
}