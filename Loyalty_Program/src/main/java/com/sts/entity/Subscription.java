package com.sts.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.sts.enums.PlanType;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Enumerated(EnumType.STRING)
    private PlanType planType; // BASIC, PRO, ENTERPRISE

    private String paymentTerm; // Monthly, Quarterly, Yearly

    private Double price;

    private String stripeSubscriptionId;
    private String stripeCustomerId;
    // private String stripePaymentMethodId;

    private String status; // ACTIVE, CANCELLED, etc.

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public String getPaymentTerm() {
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

    // public String getStripePaymentMethodId() {
    //     return stripePaymentMethodId;
    // }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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

    public void setPaymentTerm(String paymentTerm) {
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

    // public void setStripePaymentMethodId(String stripePaymentMethodId) {
    //     this.stripePaymentMethodId = stripePaymentMethodId;
    // }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // public void setShopId(Long shopId) {
    //     // Deprecated: use setShop(Shop shop) instead
    //     throw new UnsupportedOperationException("Use setShop(Shop shop) instead.");
    // }
}
