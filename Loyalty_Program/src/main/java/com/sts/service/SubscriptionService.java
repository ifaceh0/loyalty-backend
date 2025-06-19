package com.sts.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.sts.dto.PlanChangeRequestDto;
import com.sts.dto.SubscriptionRequestDto;
import com.sts.dto.SubscriptionResponseDto;
import com.sts.entity.PlanDefinition;
import com.sts.entity.Shop;
import com.sts.entity.Subscription;
import com.sts.entity.SubscriptionPlan;
import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanStatus;
import com.sts.enums.PlanType;
import com.sts.enums.SubscriptionStatus;
import com.sts.repository.SubscriptionRepository;
import com.sts.repository.PlanDefinitionRepository;
import com.sts.repository.ShopRepository;
import com.sts.repository.SubscriptionPlanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private PlanDefinitionRepository planDefinitionRepository;

    @Autowired
    private StripeService stripeService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto dto) {
        Shop shop = shopRepository.findById(dto.getShopId())
                                  .orElseThrow(() -> new RuntimeException("Shop not found"));

        // Create Stripe Customer if not exists
        String stripeCustomerId = shop.getStripeCustomerId();
        if (stripeCustomerId == null || stripeCustomerId.isBlank()) {
        try {
                Customer customer = stripeService.createCustomer(shop.getEmail(), shop.getShopName());
                stripeCustomerId = customer.getId();
                shop.setStripeCustomerId(stripeCustomerId);
                shopRepository.save(shop);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Stripe customer", e);
            }
        }

        // Select Stripe Price ID based on plan
        String priceId = getStripePriceId(dto.getPlanType(), dto.getPaymentTerm());

        // Create Stripe Subscription
        com.stripe.model.Subscription stripeSub;
        try {
            stripeSub = stripeService.createStripeSubscription(stripeCustomerId, priceId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe subscription", e);
        }

        // Subscription subscription = new Subscription();
        // subscription.setShop(shop);
        // subscription.setPlanType(dto.getPlanType());
        // subscription.setPaymentTerm(dto.getPaymentTerm());
        // subscription.setStripeCustomerId(stripeCustomerId);
        // subscription.setStripeSubscriptionId(String.valueOf(stripeSub.getId()));
        // subscription.setPrice(calculatePrice(dto.getPlanType(), dto.getPaymentTerm()));
        // subscription.setStatus("PENDING"); // Stripe will confirm later
        // subscription.setStartDate(LocalDateTime.now());
        // subscription.setEndDate(subscription.getStartDate().plusMonths(getMonths(dto.getPaymentTerm())));
        // subscription.setCreatedOn(LocalDateTime.now());
        // subscription.setUpdatedOn(LocalDateTime.now());

        // Save active Subscription
        Subscription subscription = new Subscription();
        subscription.setShop(shop);
        subscription.setStripeCustomerId(stripeCustomerId);
        subscription.setStripeSubscriptionId(stripeSub.getStatus().equals("active") ? stripeSub.getId() : null);
        subscription.setPlanType(dto.getPlanType());
        subscription.setPaymentTerm(dto.getPaymentTerm());
        // subscription.setPrice(calculatePrice(dto.getPlanType(), dto.getPaymentTerm()));
        double price = getPlanPrice(dto.getPlanType(), dto.getPaymentTerm());
        subscription.setPrice(price);

        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(subscription.getStartDate().plusMonths(getMonths(dto.getPaymentTerm(), dto.getPlanType())));
        subscription.setStatus(SubscriptionStatus.PENDING);
        subscription.setAutoRenew(true);
        subscription.setCreatedOn(LocalDateTime.now());
        subscription.setUpdatedOn(LocalDateTime.now());

        subscriptionRepository.save(subscription);

         // Save to SubscriptionPlan (history)
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setSubscription(subscription);
        plan.setPlanType(subscription.getPlanType());
        plan.setPaymentTerm(subscription.getPaymentTerm());
        plan.setPrice(subscription.getPrice());
        plan.setStartDate(subscription.getStartDate());
        plan.setEndDate(subscription.getEndDate());
        plan.setStatus(SubscriptionStatus.PENDING);
        plan.setCreatedOn(LocalDateTime.now());
        plan.setUpdatedOn(LocalDateTime.now());

        subscriptionPlanRepository.save(plan);


        return mapToResponseDto(subscription);
    }

    //Auto renewal of subscription
    @Scheduled(cron = "0 0 0 * * *") // Every day at midnight
    public void processAutoRenewals() {
        List<Subscription> subscriptions = subscriptionRepository.findByStatusAndAutoRenewTrueAndEndDateBefore(SubscriptionStatus.ACTIVE, LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();

        for (Subscription sub : subscriptions) {
            if (sub.getEndDate().isBefore(now) && SubscriptionStatus.ACTIVE.equals(sub.getStatus()) && sub.isAutoRenew()) {
                // Mark old subscription as expired
                SubscriptionPlan latestPlan = subscriptionPlanRepository.findTopBySubscriptionOrderByEndDateDesc(sub);
                if (latestPlan != null) {
                    latestPlan.setPlanStatus(PlanStatus.EXPIRED);
                    latestPlan.setUpdatedOn(LocalDateTime.now());
                    subscriptionPlanRepository.save(latestPlan);
                }

                PlanType renewedPlan = (sub.getNextPlanType() != null) ? sub.getNextPlanType() : sub.getPlanType();
                PaymentTerm renewedTerm = (sub.getNextPaymentTerm() != null) ? sub.getNextPaymentTerm() : sub.getPaymentTerm();
                // Double renewedPrice = (sub.getNextPrice() != null) ? sub.getNextPrice() : sub.getPrice();
                Double renewedPrice = getPlanPrice(renewedPlan, renewedTerm); 

                String priceId = getStripePriceId(renewedPlan, renewedTerm);
                // Create Stripe Subscription
                com.stripe.model.Subscription stripeSub;
                try {
                    stripeSub = stripeService.createStripeSubscription(sub.getStripeCustomerId(), priceId);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create Stripe subscription", e);
                }

                // Update Subscription (only one per shop)
                sub.setPlanType(renewedPlan);
                sub.setPaymentTerm(renewedTerm);
    

                sub.setPrice(renewedPrice);
                sub.setStripeSubscriptionId(stripeSub.getId());
                sub.setStartDate(now);
                sub.setEndDate(now.plusMonths(getMonths(renewedTerm, renewedPlan)));
                sub.setUpdatedOn(LocalDateTime.now());
                sub.setStatus(SubscriptionStatus.ACTIVE);

                // Clear next plan fields
                sub.setNextPlanType(null);
                sub.setNextPaymentTerm(null);
                sub.setNextPrice(null);
                // Create renewed subscription
                // Subscription renewed = new Subscription();
                // renewed.setShop(sub.getShop());
                // renewed.setPlanType(renewedPlan);
                // renewed.setPaymentTerm(renewedTerm);
                // renewed.setPrice(renewedPrice);
                // renewed.setStripeCustomerId(sub.getStripeCustomerId());
                // renewed.setStripeSubscriptionId(stripeSub.getId());

                // renewed.setStartDate(LocalDateTime.now());
                // renewed.setEndDate(LocalDateTime.now().plusMonths(getMonths(renewedTerm)));
                // // renewed.setEndDate(calculateNewEndDate(LocalDateTime.now(), sub.getPaymentTerm()));
                // renewed.setCreatedOn(LocalDateTime.now());
                // renewed.setUpdatedOn(LocalDateTime.now());
                // renewed.setStatus("ACTIVE");
                // renewed.setAutoRenew(true);

                subscriptionRepository.save(sub);

                // Add new SubscriptionPlan record
                SubscriptionPlan renewed = new SubscriptionPlan();
                renewed.setSubscription(sub);
                renewed.setPlanType(renewedPlan);
                renewed.setPaymentTerm(renewedTerm);
                renewed.setPrice(renewedPrice);
                renewed.setStartDate(now);
                renewed.setEndDate(sub.getEndDate());
                renewed.setPlanStatus(PlanStatus.ACTIVE);
                renewed.setCreatedOn(now);
                renewed.setUpdatedOn(now);

                subscriptionPlanRepository.save(renewed);
            }
        }
    }

    // private LocalDateTime calculateNewEndDate(LocalDateTime start, String paymentTerm) {
    //     return switch (paymentTerm.toUpperCase()) {
    //         case "MONTHLY" -> start.plusMonths(1);
    //         case "QUARTERLY" -> start.plusMonths(3);
    //         case "YEARLY" -> start.plusYears(1);
    //         default -> throw new IllegalArgumentException("Invalid payment term: " + paymentTerm);
    //     };
    // }


    public String cancelSubscription(Long shopId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        // Find the active subscription for the shop
        Subscription subscription = subscriptionRepository.findByShop_ShopIdAndStatus(shopId, SubscriptionStatus.ACTIVE)
            .orElseThrow(() -> new RuntimeException("Active subscription not found"));

        // Cancel the subscription in Stripe
        if (subscription.getStripeSubscriptionId() != null && subscription.isAutoRenew()) {
            try {
                com.stripe.model.Subscription stripeSub = com.stripe.model.Subscription.retrieve(subscription.getStripeSubscriptionId());

                Map<String, Object> params = new HashMap<>();
                params.put("cancel_at_period_end", true); // Keep active until end of billing period

                stripeSub.update(params);

            } catch (Exception e) {
                throw new RuntimeException("Stripe cancellation failed: " + e.getMessage());
            }
        }

        // Update local subscription status
        subscription.setAutoRenew(false);
        subscription.setUpdatedOn(LocalDateTime.now());
        subscriptionRepository.save(subscription);

        return "Subscription cancelled successfully. It will remain active until the end of the current billing period.";
    }

    public String schedulePlanChange(PlanChangeRequestDto dto) {
        Subscription current = subscriptionRepository.findByShop_ShopIdAndStatus(dto.getShopId(), SubscriptionStatus.ACTIVE)
            .orElseThrow(() -> new RuntimeException("Active subscription not found"));

        if (!current.isAutoRenew()) {
            throw new IllegalStateException("Cannot change plan for a non-renewing subscription.");
        }

        current.setNextPlanType(dto.getNewPlanType());
        current.setNextPaymentTerm(dto.getNewPaymentTerm());
        current.setNextPrice(getPlanPrice(dto.getNewPlanType(), dto.getNewPaymentTerm()));
        current.setUpdatedOn(LocalDateTime.now());

        subscriptionRepository.save(current);
        return "Plan change scheduled. It will take effect after the current billing period ends.";
    }

    private SubscriptionResponseDto mapToResponseDto(Subscription sub) {
        SubscriptionResponseDto dto = new SubscriptionResponseDto();
        dto.setId(sub.getId());
        dto.setShopId(sub.getShop().getShopId());
        dto.setPlanType(sub.getPlanType());
        dto.setPaymentTerm(sub.getPaymentTerm());
        dto.setPrice(sub.getPrice());
        dto.setStatus(sub.getStatus());
        // dto.setAutoRenew(sub.isAutoRenew());
        dto.setStripeCustomerId(sub.getStripeCustomerId());
        dto.setStripeSubscriptionId(sub.getStripeSubscriptionId());
        dto.setStartDate(sub.getStartDate());
        dto.setEndDate(sub.getEndDate());
        dto.setCreatedAt(sub.getCreatedOn());
        dto.setUpdatedAt(sub.getUpdatedOn());
        return dto;
    }


    

    // private double calculatePrice(PlanType planType, String term) {
    //     PlanDefinition latest = planDefinitionRepository
    //         .findTopByPlanTypeAndPaymentTermOrderByEffectiveFromDesc(planType, term)
    //         .orElseThrow(() -> new IllegalArgumentException("No price defined for " + planType + " " + term));
    //     return latest.getPrice();
    // }

    // private double calculatePriceOrDefault(PlanType planType, String term) {
    // try {
    //     return calculatePrice(planType, term);
    //     } catch (Exception e) {
    //         return switch (planType) {
    //             case BASIC -> switch (term.toUpperCase()) {
    //                 case "MONTHLY" -> 9.99;
    //                 case "QUARTERLY" -> 9.99 * 3 * 0.8;
    //                 case "YEARLY" -> 9.99 * 12 * 0.7;
    //                 default -> throw new IllegalArgumentException("Invalid term");
    //             };
    //             case PRO -> switch (term.toUpperCase()) {
    //                 case "MONTHLY" -> 29.99;
    //                 case "QUARTERLY" -> 29.99 * 3 * 0.8;
    //                 case "YEARLY" -> 29.99 * 12 * 0.7;
    //                 default -> throw new IllegalArgumentException("Invalid term");
    //             };
    //             default -> throw new IllegalArgumentException("Enterprise not handled here");
    //         };
    //     }
    // }

    // private int getMonths(PaymentTerm term) {
    //     return switch (term.toUpperCase()) {
    //         case "MONTHLY" -> 1;
    //         case "QUARTERLY" -> 3;
    //         case "YEARLY" -> 12;
    //         default -> 1;
    //     };
    // }

    // private String getStripePriceId(PlanType plan, String term) {
    //     return switch (plan) {
    //         case BASIC -> switch (term.toUpperCase()) {
    //             case "MONTHLY" -> "price_1BasicMonth123";
    //             case "QUARTERLY" -> "price_1BasicQuarter123";
    //             case "YEARLY" -> "price_1BasicYear123";
    //             default -> throw new IllegalArgumentException("Invalid term");
    //         };
    //         case PRO -> switch (term.toUpperCase()) {
    //             case "MONTHLY" -> "price_1ProMonth123";
    //             case "QUARTERLY" -> "price_1ProQuarter123";
    //             case "YEARLY" -> "price_1ProYear123";
    //             default -> throw new IllegalArgumentException("Invalid term");
    //         };
    //         case ENTERPRISE -> throw new IllegalArgumentException("Enterprise handled manually");
    //     };
    // }

    private PlanDefinition getPlanDefinition(PlanType planType, PaymentTerm term) {
    return planDefinitionRepository.findByPlanTypeAndPaymentTermAndActiveTrue(planType, term)
        .orElseThrow(() -> new IllegalArgumentException("Plan definition not found for: " + planType + ", " + term));
    }

    private String getStripePriceId(PlanType planType, PaymentTerm term) {
        return getPlanDefinition(planType, term).getStripePriceId();
    }

    private double getPlanPrice(PlanType planType, PaymentTerm term) {
        return getPlanDefinition(planType, term).getPrice();
    }

    private int getMonths(PaymentTerm term, PlanType planType) {
        return getPlanDefinition(planType, term).getDurationInMonths();
    }

}
