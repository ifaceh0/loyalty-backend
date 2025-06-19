package com.sts.dto;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;
import lombok.Data;

@Data
public class PlanChangeRequestDto {
    private Long shopId;
    private PlanType newPlanType;
    private PaymentTerm newPaymentTerm;
}
