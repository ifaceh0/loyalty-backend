/*package com.sts.service;

import com.google.zxing.WriterException;
import com.sts.dto.UserSignupRequest;
import com.sts.entity.Login;
import com.sts.entity.User;
import com.sts.enums.Role;
import com.sts.repository.LoginRepository;
import com.sts.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QrCodeGenerator qrCodeGenerator;

    @Autowired
    private LoginRepository loginRepository;


   /* public User createUser(User user) {
        return userRepository.save(user);
    }*/
   /* public User createUser(User user) {
        // Generate QR token (e.g., UUID (Universally unique identifier)
        user.setQrToken(java.util.UUID.randomUUID().toString());
        return userRepository.save(user);
    }
    */
   /* public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
       
        return userRepository.save(user);
    }
    
    //For qr codes
    public Optional<User> getUserByQrToken(String qrToken) {
        return userRepository.findByQrToken(qrToken);
    }
    public Map<String, Object> createUserWithQrCode(User user) {
        user.setQrToken(UUID.randomUUID().toString());
        User savedUser = userRepository.save(user);

        String qrCodeBase64;
        try {
            qrCodeBase64 = "data:image/png;base64," + qrCodeGenerator.generateQrCodeBase64(savedUser.getQrToken());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("QR Code generation failed");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("firstName", savedUser.getFirstName());
        response.put("lastName", savedUser.getLastName());
        response.put("email", savedUser.getEmail());
        response.put("phoneNumber", savedUser.getPhone());
        response.put("qrToken", savedUser.getQrToken());
        response.put("qrCodeBase64", qrCodeBase64);  

        return response;
    }
}*/

package com.sts.service;

import com.google.zxing.WriterException;
import com.sts.entity.User;
import com.sts.repository.LoginRepository;
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
}

    
    
    

