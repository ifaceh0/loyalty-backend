package com.sts.dto;

import lombok.Data;

@Data
public class PurchaseRequestDTO {
    private Long userId;
    private Long shopId;
    private Integer transactionAmount;
}
