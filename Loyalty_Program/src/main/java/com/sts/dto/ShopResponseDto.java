package com.sts.dto;

import lombok.Data;

@Data
public class ShopResponseDto {
    private Long shopId;
    private String shopName;
    private String phone;
    private Long customerId; // userId
    private int availablePoints;
}
