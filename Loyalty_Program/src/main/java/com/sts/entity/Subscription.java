package com.sts.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;
import com.sts.enums.SubscriptionStatus;

@Entity
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


    // Getters and Setters
    public Long getId() {
        return id;  
    }

    public Shop getShop() {
        return shop;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public PaymentTerm getPaymentTerm() {
        return paymentTerm;
    }

    public Double getPrice() {
        return price;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public void setPaymentTerm(PaymentTerm paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

     public PlanType getNextPlanType() {
        return nextPlanType;
    }

    public void setNextPlanType(PlanType nextPlanType) {
        this.nextPlanType = nextPlanType;
    }

    public PaymentTerm getNextPaymentTerm() {
        return nextPaymentTerm;
    }

    public void setNextPaymentTerm(PaymentTerm nextPaymentTerm) {
        this.nextPaymentTerm = nextPaymentTerm;
    }

    public Double getNextPrice() {
        return nextPrice;
    }

    public void setNextPrice(Double nextPrice) {
        this.nextPrice = nextPrice;
    }

    public PlanDefinition getPlanDefinition() {
        return planDefinition;
    }

    public void setPlanDefinition(PlanDefinition planDefinition) {
        this.planDefinition = planDefinition;
    }
}
