package com.sts.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;
import com.sts.enums.SubscriptionStatus;
import lombok.Data;

@Entity
@Data
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Enumerated(EnumType.STRING)
    private PlanType planType; // BASIC, PRO, ENTERPRISE

    @Enumerated(EnumType.STRING)
    private PaymentTerm paymentTerm; // Monthly, Quarterly, Yearly

    private Double price;

    private String stripeSubscriptionId;
    private String stripeCustomerId;

    private boolean autoRenew = true;

    //for changes subscription plan
    @Enumerated(EnumType.STRING)
    private PlanType nextPlanType;
    
    @Enumerated(EnumType.STRING)
    private PaymentTerm nextPaymentTerm;

    private Double nextPrice;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status; // ACTIVE, CANCELLED, etc.

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_definition_id")
    private PlanDefinition planDefinition;
}
