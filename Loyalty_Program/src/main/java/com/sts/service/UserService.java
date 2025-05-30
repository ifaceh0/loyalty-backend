package com.sts.service;

import com.sts.entity.User;
import com.sts.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService 
{

    @Autowired
    private UserRepository userRepository;
    
//    @Autowired
//    private QrCodeGenerator qrCodeGenerator;


    public User createUser(User user) {
        return userRepository.save(user);
    }

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
        user.setPhoneNumber(userDetails.getPhoneNumber());
       
        return userRepository.save(user);
    }
    public Optional<User> getUserByQrToken(String qrToken) {
        return userRepository.findByQrToken(qrToken);
    }

    public User createUser1(User user) {
        // Generate QR token (e.g., UUID (Universally unique identifier)
        user.setQrToken(java.util.UUID.randomUUID().toString());
        return userRepository.save(user);
    }
}
    
    
    //Qr code
   /* public UserDto createUser1(User user) {
        user.setQrtoken(UUID.randomUUID().toString());

        User savedUser = userRepository.save(user);

        String qrBase64;
        try {
            qrBase64 = qrCodeGenerator.generateQrCodeBase64(savedUser.getQrtoken());
        } catch (Exception e) {
            throw new RuntimeException("QR Code generation failed", e);
        }

        UserDto response = new UserDto();
        response.setUser_Id(savedUser.getUserId());
        response.setName(savedUser.getFirstName());
        
        response.setEmail(savedUser.getEmail());
        response.setPhoneNumber(savedUser.getPhoneNumber());
        response.setQrToken(savedUser.getQrtoken());
        response.setQrCodeBase64(qrBase64);

        return response;
    }
    
    
    
    public Optional<UserDto> getUserByQrToken(String qrToken) {
        return userRepository.findByQrToken(qrToken).map(user -> {
            UserDto dto = new UserDto();
            dto.setName(user.getFirstName());
           
            dto.setEmail(user.getEmail());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setQrToken(user.getQrtoken());
            dto.setTotalpoints(user.getUserProfile() != null ? user.getUserProfile().getAvailablePoints() : 0);
            return dto;
        });
    }
}
   /* @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }*/

