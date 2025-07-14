package com.sts.entity;

import java.time.LocalDateTime;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanStatus;
import com.sts.enums.PlanType;
import com.sts.enums.SubscriptionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "subscription_history")
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;       
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    private PaymentTerm paymentTerm;
    private Double price;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_definition_id")
    private PlanDefinition planDefinition;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Enumerated(EnumType.STRING)
    private PlanStatus planStatus; // ACTIVE, EXPIRED, CANCELLED, RENEWED

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
