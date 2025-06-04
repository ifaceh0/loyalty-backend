package com.sts.dto;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;

public class PlanChangeRequestDto {
    private Long shopId;
    private PlanType newPlanType;
    private PaymentTerm newPaymentTerm;
    
    public Long getShopId() {
        return shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public PlanType getNewPlanType() {
        return newPlanType;
    }
    public void setNewPlanType(PlanType newPlanType) {
        this.newPlanType = newPlanType;
    }
    public PaymentTerm getNewPaymentTerm() {
        return newPaymentTerm;
    }
    public void setNewPaymentTerm(PaymentTerm newPaymentTerm) {
        this.newPaymentTerm = newPaymentTerm;
    }
}
