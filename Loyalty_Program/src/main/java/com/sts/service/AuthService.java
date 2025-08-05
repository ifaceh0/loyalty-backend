package com.sts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.dto.*;
import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.enums.Role;
import com.sts.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sts.entity.Login;
import com.sts.repository.LoginRepository;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@EnableCaching//changed
@CacheConfig(cacheNames = "subscriptions")//changed
public class AuthService {
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private EmailService emailService;

	//new code
	@Autowired
	private RestTemplate restTemplate;

	private final ObjectMapper objectMapper; // CHANGE: Added for JSON serialization

	@Value("${subscription.service.url:https://subscription-backend-e8gq.onrender.com}")
	private String subscriptionServiceUrl;

	public AuthService(LoginRepository loginRepository, UserRepository userRepository,
					   ShopRepository shopRepository, JwtUtil jwtUtil,
					   RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.loginRepository = loginRepository;
		this.userRepository = userRepository;
		this.shopRepository = shopRepository;
		this.jwtUtil = jwtUtil;
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper; // CHANGE: Initialize ObjectMapper
	}
	// CHANGE: Added caching for subscription details
	@Cacheable(key = "#email", unless = "#result == null")
	public SubscriptionDetails getSubscriptionDetails(String email) {
		String subscriptionUrl = subscriptionServiceUrl + "/api/subscription/getSubscriptionDetails";
		Map<String, String> emailRequest = new HashMap<>();
		emailRequest.put("email", email);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + jwtUtil.generateServiceToken());
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(emailRequest, headers);

		// CHANGE: Implement retry logic for 429 responses
		int maxRetries = 3;
		int delaySeconds = 1;
		for (int attempt = 0; attempt < maxRetries; attempt++) {
			try {
				log.info("Fetching subscription details for email: {}, attempt: {}", email, attempt + 1);
				ResponseEntity<Map> subscriptionResponse = restTemplate.postForEntity(subscriptionUrl, entity, Map.class);
				if (subscriptionResponse.getStatusCode().is2xxSuccessful() && (Boolean) subscriptionResponse.getBody().get("success")) {
					Map<String, Object> responseBody = subscriptionResponse.getBody();
					String status = (String) responseBody.get("status");
					if (!"ACTIVE".equals(status)) {
						throw new RuntimeException("Subscription is " + status.toLowerCase() + ".");
					}
					return new SubscriptionDetails(
							(String) responseBody.get("email"),
							status,
							(String) responseBody.get("planName"),
							(String) responseBody.get("interval"),
							(Double) responseBody.get("price"),
							(List) responseBody.get("applications"),
							(String) responseBody.get("nextPlanName"),
							(String) responseBody.get("nextInterval"),
							(String) responseBody.get("startDate"),
							(String) responseBody.get("endDate"),
							(Boolean) responseBody.get("autoRenew"),
							(Boolean) responseBody.get("cancelAtPeriodEnd"),
							(String) responseBody.get("stripeCustomerId"),
							(String) responseBody.get("stripeSubscriptionId")
					);
				} else {
					throw new RuntimeException("Failed to retrieve subscription details: " + subscriptionResponse.getBody().get("message"));
				}
			} catch (HttpClientErrorException e) {
				if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS && attempt < maxRetries - 1) {
					String retryAfter = e.getResponseHeaders().getFirst("Retry-After");
					long waitTime = retryAfter != null ? Long.parseLong(retryAfter) : (long) Math.pow(2, attempt) * delaySeconds;
					log.warn("429 Too Many Requests for email: {}. Retrying after {} seconds.", email, waitTime);
					try {
						Thread.sleep(waitTime * 1000);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						throw new RuntimeException("Interrupted during retry wait", ie);
					}
				} else {
					throw new RuntimeException("Failed to retrieve subscription details after retries: " + e.getMessage());
				}
			}
		}
		return null; // Fallback if all retries fail
	}

	//new end

	@Transactional
	public AuthResponse signupUser(UserSignupRequest request) {
		// Validate email and phone uniqueness
		if (loginRepository.existsByEmail(request.getEmail()) || loginRepository.existsByPhone(request.getPhone())) {
			throw new RuntimeException("Email or phone already exists");
		}

		// Create User entity
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());

		// Link referral if present
		if (request.getReferralShopId() != null) {
			Shop shop = shopRepository.findById(request.getReferralShopId())
					.orElseThrow(() -> new RuntimeException("Referred shop not found"));
			user.setReferredBy(shop);
		}

		User u = userRepository.save(user);

		// Create Login entity
		Login login = new Login();
		login.setEmail(request.getEmail());
		login.setPhone(request.getPhone());
		login.setPassword(passwordEncoder.encode(request.getPassword()));
		login.setRole(Role.USER);
		login.setRefId(u.getUserId());
		loginRepository.save(login);

		//Send welcome email
		String fullName = request.getFirstName() + " " + request.getLastName();
		emailService.sendUserWelcomeEmail(request.getEmail(), fullName);

		// Generate JWT
		String token = jwtUtil.generateToken(login.getEmail(), login.getRole());
		return new AuthResponse(token, "User registered successfully", user.getUserId(),user.getFirstName()+" "+user.getLastName());
	}

	@Transactional
	public AuthResponse signupShop(ShopSignupRequest request) {
		// Validate email, phone, and company details uniqueness
		if (loginRepository.existsByEmail(request.getEmail()) || loginRepository.existsByPhone(request.getPhone()) ||
				shopRepository.existsByCompanyEmail(request.getCompanyEmail()) ||
				shopRepository.existsByCompanyPhone(request.getCompanyPhone())) {
			throw new RuntimeException("Email, phone, or company details already exist");
		}

		// Create Shop entity
		Shop shop = new Shop();
		shop.setShopName(request.getShopName());
		shop.setEmail(request.getEmail());
		shop.setPhone(request.getPhone());
		shop.setCompanyName(request.getCompanyName());
		shop.setCompanyAddress(request.getCompanyAddress());
		shop.setCompanyEmail(request.getCompanyEmail());
		shop.setCompanyPhone(request.getCompanyPhone());
		Shop s = shopRepository.save(shop);

		// Create Login entity
		Login login = new Login();
		login.setEmail(request.getEmail());
		login.setPhone(request.getPhone());
		login.setPassword(passwordEncoder.encode(request.getPassword()));
		login.setRole(Role.SHOPKEEPER);
		login.setRefId(s.getShopId());
		loginRepository.save(login);

		// Send welcome email
		emailService.sendShopkeeperWelcomeEmail(request.getEmail(), request.getShopName());

		// Generate JWT
		String token = jwtUtil.generateToken(login.getEmail(), login.getRole());
		return new AuthResponse(token, "Shop registered successfully", shop.getShopId(), shop.getShopName());
	}

//	public AuthResponse signin(SigninRequest request) {
//		// Step 1: Validate login
//		Optional<Login> loginOpt = loginRepository.findByEmail(request.getEmail());
//		if (loginOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), loginOpt.get().getPassword())) {
//			throw new RuntimeException("Invalid credentials");
//		}
//
//		Login login = loginOpt.get();
////		String token = jwtUtil.generateToken(login.getEmail(), Role.valueOf(login.getRole().name())); // Use role.name()
//		String token = jwtUtil.generateToken(login.getEmail(), login.getRole());
//		Long id = null;
//		String name = null;
//		SubscriptionDetails subscriptionDetails = null; //new code
//
//		// Step 2: Match by email for USER or SHOP
//		if (login.getRole() == Role.USER) {
//			Optional<User> userOpt = userRepository.findByEmail(login.getEmail());
//			if (userOpt.isPresent()) {
//				User user = userOpt.get();
//				id = user.getUserId();
//				name = user.getFirstName()+" "+user.getLastName();
//			}
//		} else if (login.getRole() == Role.SHOPKEEPER) {
//			Optional<Shop> shopOpt = shopRepository.findByEmail(login.getEmail());
//			if (shopOpt.isPresent()) {
//				Shop shop = shopOpt.get();
//				id = shop.getShopId();
//				name = shop.getShopName();
//
//				//new start
//				String subscriptionUrl = subscriptionServiceUrl + "/api/subscription/getSubscriptionDetails";
//				Map<String, String> emailRequest = new HashMap<>();
//				emailRequest.put("email", shop.getCompanyEmail());
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				headers.set("Authorization", "Bearer " + jwtUtil.generateServiceToken());
//				HttpEntity<Map<String, String>> entity = new HttpEntity<>(emailRequest, headers);
//				ResponseEntity<Map> subscriptionResponse = restTemplate.postForEntity(subscriptionUrl, entity, Map.class);
//
//				if (subscriptionResponse.getStatusCode().is2xxSuccessful() && (Boolean) subscriptionResponse.getBody().get("success")) {
//					Map<String, Object> responseBody = subscriptionResponse.getBody();
//					String status = (String) responseBody.get("status");
//					if (!"ACTIVE".equals(status)) {
//						throw new RuntimeException("Login failed: Subscription is " + status.toLowerCase() + ".");
//					}
//					subscriptionDetails = new SubscriptionDetails(
//							(String) responseBody.get("email"),
//							status,
//							(String) responseBody.get("planName"),
//							(String) responseBody.get("interval"),
//							(String) responseBody.get("nextPlanName"),
//							(String) responseBody.get("nextInterval"),
//							(String) responseBody.get("startDate"),
//							(String) responseBody.get("endDate"),
//							(Boolean) responseBody.get("autoRenew"),
//							(Boolean) responseBody.get("cancelAtPeriodEnd"),
//							(String) responseBody.get("stripeCustomerId"),
//							(String) responseBody.get("stripeSubscriptionId")
//					);
//				} else {
//					throw new RuntimeException("Failed to retrieve subscription details: " + subscriptionResponse.getBody().get("message"));
//				}
//				//new end
//			}
//		}
//
//		if (id == null) {
//			throw new RuntimeException("No matching User or Shop found for the provided email.");
//		}
//
//		return new AuthResponse(token, "Signin successful", id, name, subscriptionDetails);
//	}

	public AuthResponse signin(SigninRequest request) {
		// Step 1: Validate login
		Optional<Login> loginOpt = loginRepository.findByEmail(request.getEmail());
		if (loginOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), loginOpt.get().getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		Login login = loginOpt.get();
		String token = jwtUtil.generateToken(login.getEmail(), login.getRole());
		Long id = null;
		String name = null;
		SubscriptionDetails subscriptionDetails = null; //new code

		// Step 2: Match by email for USER or SHOP
		if (login.getRole() == Role.USER) {
			Optional<User> userOpt = userRepository.findByEmail(login.getEmail());
			if (userOpt.isPresent()) {
				User user = userOpt.get();
				id = user.getUserId();
				name = user.getFirstName()+" "+user.getLastName();
			}
		} else if (login.getRole() == Role.SHOPKEEPER) {
			Optional<Shop> shopOpt = shopRepository.findByEmail(login.getEmail());
			if (shopOpt.isPresent()) {
				Shop shop = shopOpt.get();
				id = shop.getShopId();
				name = shop.getShopName();

				//new start
				// CHANGE: Use cached subscription details
				try {
					subscriptionDetails = getSubscriptionDetails(shop.getCompanyEmail());
					if (subscriptionDetails == null) {
						throw new RuntimeException("Failed to retrieve subscription details after retries.");
					}
				} catch (Exception e) {
					log.error("Error fetching subscription for email: {}", shop.getCompanyEmail(), e);
					throw new RuntimeException("Failed to retrieve subscription details: " + e.getMessage());
				}
			}
		}

		if (id == null) {
			throw new RuntimeException("No matching User or Shop found for the provided email.");
		}

		return new AuthResponse(token, "Signin successful", id, name, subscriptionDetails);
	}

	@Value("${app.frontend.reset-url}")
	private String frontendResetUrl;

	public void requestPasswordReset(String email) {
		Login login = loginRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));

		String resetToken = UUID.randomUUID().toString();
		login.setResetToken(resetToken);
		login.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
		loginRepository.save(login);

		String resetLink = String.format("%s?token=%s&email=%s", frontendResetUrl, resetToken, email);
		emailService.sendPasswordResetEmail(email, resetLink);
	}

	public void resetPassword(String email, String newPassword, String resetToken) {
		Login login = loginRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (login.getResetToken() == null || !login.getResetToken().equals(resetToken) ||
				login.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Invalid or expired reset token");
		}

		login.setPassword(passwordEncoder.encode(newPassword));
		login.setResetToken(null);
		login.setResetTokenExpiry(null);
		loginRepository.save(login);
	}
}
