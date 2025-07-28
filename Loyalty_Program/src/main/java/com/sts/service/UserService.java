package com.sts.service;

import com.google.zxing.WriterException;
import com.sts.dto.UserDto;
import com.sts.dto.UserProfileDto;
import com.sts.entity.Login;
import com.sts.entity.User;
import com.sts.entity.UserProfile;
import com.sts.enums.Role;
import com.sts.repository.LoginRepository;
import com.sts.repository.UserProfileRepository;
import com.sts.repository.UserRepository;
//import com.sts.util.QrCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserProfileRepository profileRepo;

    @Autowired
    private QrCodeGenerator qrCodeGenerator;

    /**
     * Create a user and generate a QR token and base64 image.
     * @param user User entity without QR token
     * @return Map containing user info and QR code
     */
    public Map<String, Object> createUserWithQrCode(User user) {
        user.setQrToken(UUID.randomUUID().toString());
        User savedUser = userRepository.save(user);

        String qrCodeBase64;
        try {
            qrCodeBase64 = "data:image/png;base64," + qrCodeGenerator.generateQrCodeBase64(savedUser.getQrToken());
        } catch (WriterException | IOException e) {
            log.error("QR code generation failed: {}", e.getMessage());
            throw new RuntimeException("QR Code generation failed");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", savedUser.getUserId());
        response.put("firstName", savedUser.getFirstName());
        response.put("lastName", savedUser.getLastName());
        response.put("email", savedUser.getEmail());
        response.put("phoneNumber", savedUser.getPhone());
        response.put("qrToken", savedUser.getQrToken());
        response.put("qrCodeBase64", qrCodeBase64);

        return response;
    }

    /**
     * Get all users.
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by ID.
     * @param id user ID
     * @return Optional<User>
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Update user details.
     * @param id user ID
     * @param userDetails updated user info
     * @return updated user
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());

        return userRepository.save(user);
    }

    /**
     * Find user by QR token.
     * @param qrToken token string
     * @return Optional<User>
     */
    public Optional<User> getUserByQrToken(String qrToken) {
        return userRepository.findByQrToken(qrToken);
    }

    //user info through phonenumber and email
    public UserDto findByPhoneInShop(String phone, Long shopId) {
        return userRepository.findByPhone(phone)
                .flatMap(user -> profileRepo.findByUser_UserIdAndShop_ShopId(user.getUserId(), shopId)
                        .filter(p -> p.getAvailablePoints() != null && p.getAvailablePoints() > 0)
                        .map(profile -> toDto(user, profile)))
                .orElse(null); // or throw new UserNotFoundException();
    }

    public UserDto findByEmailInShop(String email, Long shopId) {
        return userRepository.findByEmail(email)
                .flatMap(user -> profileRepo.findByUser_UserIdAndShop_ShopId(user.getUserId(), shopId)
                        .filter(p -> p.getAvailablePoints() != null && p.getAvailablePoints() > 0)
                        .map(profile -> toDto(user, profile)))
                .orElse(null); // or throw new UserNotFoundException();
    }

    private UserDto toDto(User user, UserProfile profile) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setLastUpdatedDate(user.getLastUpdatedDate());

        UserProfileDto pDto = new UserProfileDto();
        pDto.setUserId(profile.getUserId());
        pDto.setShopId(profile.getShop().getShopId());
        pDto.setAvailablePoints(profile.getAvailablePoints());
        pDto.setCreatedAt(profile.getCreatedAt());
        pDto.setUpdatedAt(profile.getUpdatedAt());

        dto.setProfile(pDto);
        return dto;
    }

    private UserProfileDto mapProfile(UserProfile p) {
        UserProfileDto d = new UserProfileDto();
        d.setUserId(p.getUserId());
        d.setShopId(p.getShop().getShopId());
        d.setAvailablePoints(p.getAvailablePoints());
        d.setCreatedAt(p.getCreatedAt());
        d.setUpdatedAt(p.getUpdatedAt());
        return d;
    }

    private UserProfileDto emptyProfile(Long userId, Long shopId) {
        UserProfileDto d = new UserProfileDto();
        d.setUserId(userId);
        d.setShopId(shopId);
        d.setAvailablePoints(0);
        return d;
    }
    //User profile update
    public UserDto updateUserProfile(UserDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() ->
                new NoSuchElementException("User not found with ID: " + dto.getUserId()));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        userRepository.save(user);

        // Update login if role is USER
        Login login = loginRepository.findByRefIdAndRole(dto.getUserId(), Role.USER).orElse(null);
        if (login != null) {
            login.setEmail(dto.getEmail());
            login.setPhone(dto.getPhone());
            loginRepository.save(login);
        } else {
            throw new RuntimeException("Login not found for this user");
        }

        return convertUserToDto(user);
    }
    private UserDto convertUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        return dto;
    }
}

    
    
    

