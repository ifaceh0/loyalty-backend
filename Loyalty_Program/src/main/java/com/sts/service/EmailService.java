package com.sts.service;

import com.sts.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {
//    @Autowired
//    private JavaMailSender mailSender;

//    public void sendSimpleMessage(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        mailSender.send(message);
//    }

    @Autowired
    private LoginRepository loginRepository;

//    public void sendPasswordResetEmail(String email) throws MessagingException {
//        Login login = loginRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String resetToken = UUID.randomUUID().toString();
//        login.setResetToken(resetToken);
//        login.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
//        loginRepository.save(login);
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(email);
//        helper.setSubject("Password Reset Request");
//        helper.setText("Use this token to reset your password: " + resetToken + "\nThis token will expire in 1 hour.");
//        mailSender.send(message);
//    }

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    @Value("${brevo.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public EmailService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("Content-Type", "application/json");

        // Email content (HTML)
        String htmlContent = String.format(
                "<html><body>" +
                        "<h2>Password Reset Request</h2>" +
                        "<p>Use this token to reset your password: <strong>%s</strong></p>" +
                        "<p>This token will expire in 1 hour.</p>" +
                        "<p>If you didn’t request this, ignore this email.</p>" +
                        "</body></html>",
                resetToken
        );

        // Build request body
        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", senderName, "email", senderEmail));
        body.put("to", List.of(Map.of("email", to)));
        body.put("subject", "Password Reset Request");
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // Send API request
        try {
            restTemplate.exchange(
                    apiUrl + "/smtp/email",
                    HttpMethod.POST,
                    request,
                    String.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage(), e);
        }
    }
}
