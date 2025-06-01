package com.sts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.QrcodeGenerator.QrCodeGenerator;
import com.sts.entity.User;

import com.sts.repository.UserRepository;

@Service
public class QRCodeService {
		@Autowired
	    private UserRepository userRepository;

	    public byte[] generateUserQRCode(String email) throws Exception {
	        User user = userRepository.findByEmail(email)
	                                  .orElseThrow(() -> new RuntimeException("User not found"));

	        Double balance = user.getUserProfile() != null ? user.getUserProfile().getAvailablePoints() : 0.0;

	        String qrContent = String.format(
	                "First Name: %s\nLast Name: %s\nEmail: %s\nPhone: %s\nAvailable Balance: %.2f",
	                user.getFirstName(),
	                user.getLastName(),
	                user.getEmail(),
	                user.getPhoneNumber(),
	                balance
	        );

	        return QrCodeGenerator.generateQRCodeImage(qrContent, 300, 300);
	    }
}