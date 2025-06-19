package com.sts.dto;

import java.time.LocalDateTime;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;
import com.sts.enums.SubscriptionStatus;
import lombok.Data;

@Data
public class SubscriptionResponseDto {
    private Long id;
    private Long shopId;
    private PlanType planType;
    private PaymentTerm paymentTerm;
    private Double price;
    private SubscriptionStatus status;
    private boolean autoRenew;
    private String stripeSubscriptionId;
    private String stripeCustomerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
