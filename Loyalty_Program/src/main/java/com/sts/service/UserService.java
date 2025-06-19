package com.sts.service;

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
    public List<User> getAllUsers() {
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
}
    
    
    

