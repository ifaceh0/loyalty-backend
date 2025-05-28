package com.sts.dto;

import com.sts.enums.PlanType;

public class SubscriptionRequestDto {
    private Long shopId;
    private PlanType planType; // BASIC, PRO, ENTERPRISE
    private String paymentTerm; // Monthly, Quarterly, Yearly

    public Long getShopId() {
        return shopId;
    }
    public PlanType getPlanType() {
        return planType;
    }
    public String getPaymentTerm() {
        return paymentTerm;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }
    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }
}