package com.sts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.entity.Shop;
import com.sts.repository.ShopRepository;
import com.sts.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class SubscriptionSyncService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CacheManager cacheManager;

    @Value("${subscription.service.url:https://subscription-backend-e8gq.onrender.com}")
    private String subscriptionServiceUrl;

    // Run every hour to sync subscription data
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
//    @Scheduled(fixedRate = 10000)
    public void syncSubscriptions() {
        List<Shop> shops = shopRepository.findAll(); // Fetch all shops
        log.info("Found {} shops to sync", shops.size());
        for (Shop shop : shops) {
            try {
                log.info("Attempting to sync subscription for email: {}", shop.getCompanyEmail());
                updateSubscriptionStatus(shop.getCompanyEmail());
            } catch (Exception e) {
                log.error("Failed to sync subscription for email: {}", shop.getCompanyEmail(), e);
            }
        }
    }

    public void updateSubscriptionStatus(String email) {
        String subscriptionUrl = subscriptionServiceUrl + "/api/subscription/getSubscriptionDetails";
        Map<String, String> emailRequest = new HashMap<>();
        emailRequest.put("email", email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwtUtil.generateServiceToken());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(emailRequest, headers);

        int maxRetries = 3;
        int baseDelaySeconds = 5;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(subscriptionUrl, entity, Map.class);
                log.info("Response for email {}: statusCode={}, body={}", email, response.getStatusCode(), response.getBody());
                if (response.getStatusCode().is2xxSuccessful()) {
                    Map<String, Object> responseBody = response.getBody();
                    Boolean success = (Boolean) responseBody.get("success");

                    if (Boolean.TRUE.equals(success)) {

                        String status = (String) responseBody.get("status");
                        String endDateStr = (String) responseBody.get("endDate");

//                        Optional<Shop> shopOpt = shopRepository.findByEmail(email);
                        Optional<Shop> shopOpt = shopRepository.findByCompanyEmail(email);
                        if (shopOpt.isPresent()) {
                            Shop shop = shopOpt.get();
                            shop.setSubscriptionStatus(status);
                            LocalDateTime endDate = null;
                            if (endDateStr != null) {
                                try {
                                    endDate = LocalDateTime.parse(endDateStr);
                                } catch (DateTimeParseException e) {
                                    log.error("Invalid endDate format for email {}: {}", email, endDateStr);
                                }
                            }
                            shop.setSubscriptionEndDate(endDate);
                            shopRepository.save(shop);
                            cacheManager.getCache("subscriptions").evict(email);
                            log.info("Updated subscription for {}: status={}, endDate={}",
                                    email, status, endDateStr);
                        }
                        return;
                    } else {
                        log.warn("Subscription response unsuccessful for email {}: {}", email, responseBody);
                    }
                } else {
                    log.warn("Non-2xx response for email {}: {}", email, response.getStatusCode());
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().value() == 429 && attempt < maxRetries - 1) {
                    String retryAfter = e.getResponseHeaders().getFirst("Retry-After");
                    long waitTime = retryAfter != null ? Long.parseLong(retryAfter) :
                            (long) (Math.pow(2, attempt) * baseDelaySeconds + Math.random() * 100);
                    log.warn("429 Too Many Requests for email: {}. Retrying after {} seconds.", email, waitTime);
                    try {
                        Thread.sleep(waitTime * 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("Interrupted during retry wait for email: {}", email, ie);
                    }
                } else {
                    log.error("Failed to sync subscription for email: {} after {} attempts: {}",
                            email, maxRetries, e.getMessage());
                    break;
                }
            }
        }
    }
}
