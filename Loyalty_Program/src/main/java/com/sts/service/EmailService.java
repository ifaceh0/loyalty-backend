package com.sts.service;

import com.sts.dto.ReferralInviteRequest;
import com.sts.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    @Value("${loyalty.app.url}")
    private String loyaltyAppUrl;
    public ResponseEntity<Map<String, Object>> sendReferralEmail(ReferralInviteRequest request) {
        Map<String, Object> emailBody = new HashMap<>();

        // Sender info
        Map<String, String> sender = new HashMap<>();
        sender.put("name", senderName);
        sender.put("email", senderEmail);
        emailBody.put("sender", sender);

        // Recipient info
        List<Map<String, String>> to = new ArrayList<>();
        Map<String, String> recipient = new HashMap<>();
        recipient.put("email", request.getEmail());
        to.add(recipient);
        emailBody.put("to", to);

        // Referral link with shopId as query param (not shown to user)
        String referralLink = loyaltyAppUrl + "signup-user?referralShopId=" + request.getShopId();

        // Email Subject and HTML body
        emailBody.put("subject", "You're Invited to Join Our Loyalty Rewards Program!");

        emailBody.put("htmlContent",
                "<h2 style='color:#333;'>Hey there 👋</h2>" +
                        "<p><strong>" + request.getShopName() + "</strong> has invited you to join our exclusive Loyalty Rewards Program.</p>" +
                        "<p>Earn points on every purchase and redeem exciting rewards!</p>" +
                        "<p style='margin: 20px 0;'>" +
                        "<a href='" + referralLink + "' " +
                        "style='display:inline-block;padding:12px 20px;background-color:#6c5ce7;color:#fff;" +
                        "text-decoration:none;border-radius:8px;font-weight:600;'>Join Now</a></p>" +
                        "<p>If the button doesn't work, use this link:</p>" +
                        "<p><a href='" + referralLink + "'>" + referralLink + "</a></p>" +
                        "<br/><p>Thanks & Happy Earning!<br/>💜 Loyalty Rewards Team</p>"
        );

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(emailBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl + "/smtp/email", entity, String.class);
            Map<String, Object> result = new HashMap<>();
            result.put("status", response.getStatusCodeValue());
            result.put("message", "Referral email sent successfully.");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "Failed to send referral email.");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
