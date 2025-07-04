package com.sts.service;

import com.sts.dto.AuthResponse;
import com.sts.dto.ShopSignupRequest;
import com.sts.dto.SigninRequest;
import com.sts.dto.UserSignupRequest;
import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.enums.Role;
import com.sts.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sts.entity.Login;
import com.sts.repository.LoginRepository;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
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
		userRepository.save(user);

		// Link referral if present
		if (request.getReferralShopId() != null) {
			Shop shop = shopRepository.findById(request.getReferralShopId())
					.orElseThrow(() -> new RuntimeException("Referred shop not found"));
			user.setReferredBy(shop);
		}

		// Create Login entity
		Login login = new Login();
		login.setEmail(request.getEmail());
		login.setPhone(request.getPhone());
		login.setPassword(passwordEncoder.encode(request.getPassword()));
		login.setRole(Role.USER);
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
		shopRepository.save(shop);

		// Create Login entity
		Login login = new Login();
		login.setEmail(request.getEmail());
		login.setPhone(request.getPhone());
		login.setPassword(passwordEncoder.encode(request.getPassword()));
		login.setRole(Role.SHOPKEEPER);
		loginRepository.save(login);

		// Send welcome email
		emailService.sendShopkeeperWelcomeEmail(request.getEmail(), request.getShopName());

		// Generate JWT
		String token = jwtUtil.generateToken(login.getEmail(), login.getRole());
		return new AuthResponse(token, "Shop registered successfully", shop.getShopId(), shop.getShopName());
	}

	public AuthResponse signin(SigninRequest request) {
		// Step 1: Validate login
		Optional<Login> loginOpt = loginRepository.findByEmail(request.getEmail());
		if (loginOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), loginOpt.get().getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		Login login = loginOpt.get();
//		String token = jwtUtil.generateToken(login.getEmail(), Role.valueOf(login.getRole().name())); // Use role.name()
		String token = jwtUtil.generateToken(login.getEmail(), login.getRole());
		Long id = null;
		String name = null;

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
			}
		}

		if (id == null) {
			throw new RuntimeException("No matching User or Shop found for the provided email.");
		}

		return new AuthResponse(token, "Signin successful", id, name);
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
