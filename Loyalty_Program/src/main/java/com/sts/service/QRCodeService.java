package com.sts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sts.entity.Shop;
import com.sts.entity.UserProfile;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.User;

import com.sts.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QRCodeService {
		@Autowired
	    private UserRepository userRepository;

		@Autowired
		private ShopRepository shopRepository;

		@Autowired
		private UserProfileRepository userProfileRepository;

//	    public byte[] generateUserQRCode(String email) throws Exception {
//	        User user = userRepository.findByEmail(email)
//	                                  .orElseThrow(() -> new RuntimeException("User not found"));
//
//	        Double balance = user.getUserProfile() != null ? user.getUserProfile().getAvailablePoints() : 0.0;
//
//	        String qrContent = String.format(
//	                "First Name: %s\nLast Name: %s\nEmail: %s\nPhone: %s\nAvailable Balance: %.2f",
//	                user.getFirstName(),
//	                user.getLastName(),
//	                user.getEmail(),
//	                user.getPhone(),
//	                balance
//	        );
//
//	        return QrCodeGenerator.generateQRCodeImage(qrContent, 300, 300);
//	    }

//	public Map<String, Object> generateQRCode(Long userId, Long shopId) throws WriterException, IOException {
//
//		Shop shop = shopRepository.findById(shopId)
//				.orElseThrow(() -> new RuntimeException("Shop not found"));
//
//		// Fetch or initialize user profile
//		UserProfile userProfile = userProfileRepository.findByUserIdAndShopId(userId, shopId)
//				.orElseGet(() -> {
//					UserProfile newProfile = new UserProfile();
//					newProfile.setUserId(userId);
//					newProfile.setShopId(shopId);
//					newProfile.setAvailablePoints(0);
//					newProfile.setCreatedAt(LocalDateTime.now());
//					newProfile.setUpdatedAt(LocalDateTime.now());
//					return userProfileRepository.save(newProfile);
//				});
//
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new RuntimeException("User not found"));
//		user.setShop(shop);
//		String qrToken = UUID.randomUUID().toString();
//		user.setQrToken(qrToken);
//		userRepository.save(user);
//
//		// Generate QR code data
//		Map<String, Object> qrData = new HashMap<>();
//		qrData.put("userId", userId);
//		qrData.put("shopId", shopId);
//		qrData.put("shopName", shop.getShopName());
//		qrData.put("availablePoints", userProfile.getAvailablePoints());
//		qrData.put("qrToken", qrToken);
//
//		// Convert to JSON for QR content
//		String qrContent = new ObjectMapper().writeValueAsString(qrData);
//
//		// Generate QR code
//		QRCodeWriter qrCodeWriter = new QRCodeWriter();
//		BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
//		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//		byte[] pngData = pngOutputStream.toByteArray();
//		String qrCodeBase64 = Base64.getEncoder().encodeToString(pngData);
//
//		// Return response with additional data
//		Map<String, Object> response = new HashMap<>();
//		response.put("qrCode", qrCodeBase64);
//		response.put("availablePoints", userProfile.getAvailablePoints());
//		response.put("userId", userId); // Used as customerId
//		return response;
//	}

//	@Transactional
//	public Map<String, Object> generateQRCode(Long userId, Long shopId) throws WriterException, IOException {
//		Shop shop = shopRepository.findById(shopId)
//				.orElseThrow(() -> new IllegalArgumentException("Shop not found"));
//
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//		// Fetch or initialize user profile
//		Optional<UserProfile> profileOpt = userProfileRepository.findByUserIdAndShopId(userId, shopId);
//		UserProfile userProfile;
//		if (profileOpt.isPresent()) {
//			userProfile = profileOpt.get();
//		} else {
//			synchronized (this) { // Prevent concurrent creation
//				profileOpt = userProfileRepository.findByUserIdAndShopId(userId, shopId);
//				if (profileOpt.isPresent()) {
//					userProfile = profileOpt.get();
//				} else {
//					userProfile = new UserProfile();
//					userProfile.setUserId(userId);
//					userProfile.setShopId(shopId);
//					userProfile.setAvailablePoints(0);
//					userProfile = userProfileRepository.save(userProfile);
//				}
//			}
//		}
//
//		// Generate and store qrToken in User
//		String qrToken = UUID.randomUUID().toString();
//		user.setQrToken(qrToken);
//		userRepository.save(user);
//
//		// Generate QR code data
//		Map<String, Object> qrData = new HashMap<>();
//		qrData.put("userId", userId);
//		qrData.put("shopId", shopId);
//		qrData.put("shopName", shop.getShopName());
//		qrData.put("availablePoints", userProfile.getAvailablePoints() != null ? userProfile.getAvailablePoints() : 0);
//		qrData.put("qrToken", qrToken);
//
//		// Convert to JSON for QR content
//		String qrContent = new ObjectMapper().writeValueAsString(qrData);
//
//		// Generate QR code
//		QRCodeWriter qrCodeWriter = new QRCodeWriter();
//		BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
//		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//		byte[] pngData = pngOutputStream.toByteArray();
//		String qrCodeBase64 = Base64.getEncoder().encodeToString(pngData);
//
//		// Return response
//		Map<String, Object> response = new HashMap<>();
//		response.put("qrCode", qrCodeBase64);
//		response.put("availablePoints", userProfile.getAvailablePoints() != null ? userProfile.getAvailablePoints() : 0);
//		response.put("userId", userId); // Used as customerId
//		return response;
//	}
//
//
//
//	public Map<String, Object> getUserDetailsFromQRToken(String qrToken) {
//		User user = userRepository.findByQrToken(qrToken)
//				.orElseThrow(() -> new RuntimeException("Invalid QR token"));
//
//		Long userId = user.getUserId();
//		Shop shop = user.getShop();
//		if (shop == null) {
//			throw new RuntimeException("User is not associated with a shop");
//		}
//
//		UserProfile userProfile = userProfileRepository.findByUserIdAndShopId(userId, shop.getShopId())
//				.orElseThrow(() -> new RuntimeException("User profile not found for shop"));
//
//		Map<String, Object> userDetails = new HashMap<>();
//		userDetails.put("userId", userId);
//		userDetails.put("firstName", user.getFirstName());
//		userDetails.put("lastName", user.getLastName());
//		userDetails.put("email", user.getEmail());
//		userDetails.put("phone", user.getPhone());
//		userDetails.put("shopId", shop.getShopId());
//		userDetails.put("shopName", shop.getShopName());
//		userDetails.put("availablePoints", userProfile.getAvailablePoints());
//
//		return userDetails;
//	}
}