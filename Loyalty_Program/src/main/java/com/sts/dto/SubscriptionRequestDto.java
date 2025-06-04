package com.sts.dto;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;

public class SubscriptionRequestDto {
    private Long shopId;
    private PlanType planType; // BASIC, PRO, ENTERPRISE
    private PaymentTerm paymentTerm; // Monthly, Quarterly, Yearly

    public Long getShopId() {
        return shopId;
    }
    public PlanType getPlanType() {
        return planType;
    }
    public PaymentTerm getPaymentTerm() {
        return paymentTerm;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }
    public void setPaymentTerm(PaymentTerm paymentTerm) {
        this.paymentTerm = paymentTerm;
    }
}