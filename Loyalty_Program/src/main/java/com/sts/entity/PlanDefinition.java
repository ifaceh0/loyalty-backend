package com.sts.entity;

import java.time.LocalDateTime;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "plan_definition")
@Data
public class PlanDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    private PaymentTerm paymentTerm; // MONTHLY, QUARTERLY, YEARLY

    private double price;
    private String stripePriceId;

    private boolean active;

    @Column(name = "duration_in_months")
    private int durationInMonths;

    private LocalDateTime effectiveFrom;
    private LocalDateTime createdOn;

//    public int getDurationInMonths() {
//        return durationInMonths;
//    }
//
//    public void setDurationInMonths(int durationInMonths) {
//        this.durationInMonths = durationInMonths;
//    }
//
//    public String getStripePriceId() {
//        return stripePriceId;
//    }
//
//    public void setStripePriceId(String stripePriceId) {
//        this.stripePriceId = stripePriceId;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }
//
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public PlanType getPlanType() {
//        return planType;
//    }
//
//    public void setPlanType(PlanType planType) {
//        this.planType = planType;
//    }
//
//    public PaymentTerm getPaymentTerm() {
//        return paymentTerm;
//    }
//
//    public void setPaymentTerm(PaymentTerm paymentTerm) {
//        this.paymentTerm = paymentTerm;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public LocalDateTime getEffectiveFrom() {
//        return effectiveFrom;
//    }
//
//    public void setEffectiveFrom(LocalDateTime effectiveFrom) {
//        this.effectiveFrom = effectiveFrom;
//    }
//
//    public LocalDateTime getCreatedOn() {
//        return createdOn;
//    }
//
//    public void setCreatedOn(LocalDateTime createdOn) {
//        this.createdOn = createdOn;
//    }
//
//
}