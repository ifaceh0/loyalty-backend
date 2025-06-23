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
    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    @Value("${brevo.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    private LoginRepository loginRepository;

    public EmailService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("Content-Type", "application/json");

        // Email content (HTML)
        String htmlContent = String.format(
                "<html><body>" +
                        "<h2>Password Reset Request</h2>" +
                        "<p>Click the link below to reset your password:</p>" +
                        "<p><a href=\"%s\">Reset Password</a></p>" +
                        "<p>This link will expire in 1 hour.</p>" +
                        "<p>If you didn’t request this, please ignore this email.</p>" +
                        "</body></html>",
                resetLink
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

    public void sendShopkeeperWelcomeEmail(String to, String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("Content-Type", "application/json");

        String subject = "Welcome to Loyalty Program!";
        String htmlContent = String.format(
                "<html><body>" +
                        "<h2>Welcome, %s! 🎉</h2>" +
                        "<p>Thank you for signing up with <strong>Loyalty Program</strong>.</p>" +
                        "<p>We're excited to have you on board. Start earning and rewarding loyalty today!</p>" +
                        "<br><p>Best regards,<br>The Loyalty Program Team</p>" +
                        "</body></html>", name
        );

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", senderName, "email", senderEmail));
        body.put("to", List.of(Map.of("email", to, "name", name)));
        body.put("subject", subject);
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(
                    apiUrl + "/smtp/email",
                    HttpMethod.POST,
                    request,
                    String.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to send welcome email: " + e.getMessage(), e);
        }
    }

    public void sendUserWelcomeEmail(String to, String fullName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("Content-Type", "application/json");

        String subject = "Welcome to Loyalty Program!";
        String htmlContent = String.format(
                "<html><body>" +
                        "<h2>Welcome, %s! 🎉</h2>" +
                        "<p>Thank you for signing up for <strong>Loyalty Program</strong>.</p>" +
                        "<p>We’re thrilled to have you on board! Start earning and enjoying rewards today.</p>" +
                        "<br><p>Cheers,<br>The Loyalty Team</p>" +
                        "</body></html>", fullName
        );

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", senderName, "email", senderEmail));
        body.put("to", List.of(Map.of("email", to, "name", fullName)));
        body.put("subject", subject);
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(
                    apiUrl + "/smtp/email",
                    HttpMethod.POST,
                    request,
                    String.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to send welcome email to user: " + e.getMessage(), e);
        }
    }


}
