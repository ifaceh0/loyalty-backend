package com.sts.dto;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;
import lombok.Data;

@Data
public class SubscriptionRequestDto {
    private Long shopId;
    private PlanType planType; // BASIC, PRO, ENTERPRISE
    private PaymentTerm paymentTerm; // Monthly, Quarterly, Yearly
}