package com.sts.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.sts.dto.SubscriptionRequestDto;
import com.sts.dto.SubscriptionResponseDto;
import com.sts.entity.Shop;
import com.sts.entity.Subscription;
import com.sts.enums.PlanType;
import com.sts.repository.SubscriptionRepository;
import com.sts.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private StripeService stripeService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    // public SubscriptionResponseDto createSubscription(SubscriptionRequestDto dto) {
    //     Shop shop = shopRepository.findById(dto.getShopId())
    //                               .orElseThrow(() -> new RuntimeException("Shop not found"));

    //     // Create Stripe Customer if not exists
    //     // String stripeCustomerId = shop.getStripeCustomerId();
    //     // if (stripeCustomerId == null) {
    //     // Customer customer = stripeService.createCustomer(shop.getEmail(), shop.getName());
    //     // stripeCustomerId = customer.getId();
    //     // shop.setStripeCustomerId(stripeCustomerId);
    //     // shopRepository.save(shop);
    //     // }

    //     // Select Stripe Price ID based on plan
    //     String priceId = getStripePriceId(dto.getPlanType(), dto.getPaymentTerm());

    //     // Create Stripe Subscription
    //     com.stripe.model.Subscription stripeSub = stripeService.createStripeSubscription(stripeCustomerId, priceId);

    //     Subscription subscription = new Subscription();
    //     subscription.setShop(shop);
    //     subscription.setPlanType(dto.getPlanType());
    //     subscription.setPaymentTerm(dto.getPaymentTerm());
    //     subscription.setStripeCustomerId(stripeCustomerId);
    //     subscription.setStripeSubscriptionId(String.valueOf(stripeSub.getId()));
    //     subscription.setPrice(calculatePrice(dto.getPlanType(), dto.getPaymentTerm()));
    //     subscription.setStatus("PENDING"); // Stripe will confirm later
    //     subscription.setStartDate(LocalDateTime.now());
    //     subscription.setEndDate(subscription.getStartDate().plusMonths(getMonths(dto.getPaymentTerm())));
    //     subscription.setCreatedAt(LocalDateTime.now());
    //     subscription.setUpdatedAt(LocalDateTime.now());

    //     subscriptionRepository.save(subscription);
    //     return mapToResponseDto(subscription);
    // }

    private double calculatePrice(PlanType planType, String term) {
        switch (planType) {
            case BASIC:
                return switch (term.toUpperCase()) {
                    case "MONTHLY" -> 9.99;
                    case "QUARTERLY" -> 9.99 * 3 * 0.8;
                    case "YEARLY" -> 9.99 * 12 * 0.7;
                    default -> throw new IllegalArgumentException("Invalid term");
                };
            case PRO:
                return switch (term.toUpperCase()) {
                    case "MONTHLY" -> 29.99;
                    case "QUARTERLY" -> 29.99 * 3 * 0.8;
                    case "YEARLY" -> 29.99 * 12 * 0.7;
                    default -> throw new IllegalArgumentException("Invalid term");
                };
            case ENTERPRISE:
                return 0.0; // Handled via sales contact
            default:
                throw new IllegalArgumentException("Invalid plan");
        }
    }

    private int getMonths(String term) {
        return switch (term.toUpperCase()) {
            case "MONTHLY" -> 1;
            case "QUARTERLY" -> 3;
            case "YEARLY" -> 12;
            default -> 1;
        };
    }

    private String getStripePriceId(PlanType plan, String term) {
        return switch (plan) {
            case BASIC -> switch (term.toUpperCase()) {
                case "MONTHLY" -> "price_1BasicMonth123";
                case "QUARTERLY" -> "price_1BasicQuarter123";
                case "YEARLY" -> "price_1BasicYear123";
                default -> throw new IllegalArgumentException("Invalid term");
            };
            case PRO -> switch (term.toUpperCase()) {
                case "MONTHLY" -> "price_1ProMonth123";
                case "QUARTERLY" -> "price_1ProQuarter123";
                case "YEARLY" -> "price_1ProYear123";
                default -> throw new IllegalArgumentException("Invalid term");
            };
            case ENTERPRISE -> throw new IllegalArgumentException("Enterprise handled manually");
        };
    }

    private SubscriptionResponseDto mapToResponseDto(Subscription sub) {
        SubscriptionResponseDto dto = new SubscriptionResponseDto();
        dto.setId(sub.getId());
        dto.setShopId(sub.getShop().getShopId());
        dto.setPlanType(sub.getPlanType());
        dto.setPaymentTerm(sub.getPaymentTerm());
        dto.setPrice(sub.getPrice());
        dto.setStatus(sub.getStatus());
        dto.setStripeCustomerId(sub.getStripeCustomerId());
        dto.setStripeSubscriptionId(sub.getStripeSubscriptionId());
        dto.setStartDate(sub.getStartDate());
        dto.setEndDate(sub.getEndDate());
        dto.setCreatedAt(sub.getCreatedAt());
        dto.setUpdatedAt(sub.getUpdatedAt());
        return dto;
    }
    public String cancelSubscription(Long shopId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        // Find the active subscription for the shop
        Subscription subscription = subscriptionRepository.findByShopIdAndStatus(shopId, "ACTIVE")
            .orElseThrow(() -> new RuntimeException("Active subscription not found"));

        // Cancel the subscription in Stripe
        com.stripe.model.Subscription stripeSubscription = com.stripe.model.Subscription.retrieve(subscription.getStripeSubscriptionId());
        stripeSubscription.cancel();

        // Update local subscription status
        subscription.setStatus("CANCELLED");
        subscription.setEndDate(LocalDateTime.now());
        subscription.setUpdatedAt(LocalDateTime.now());
        subscriptionRepository.save(subscription);

        return "Subscription cancelled successfully.";
    }


}
