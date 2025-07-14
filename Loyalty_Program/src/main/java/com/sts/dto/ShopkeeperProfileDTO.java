package com.sts.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ShopkeeperProfileDTO {
    private Long shopId;
    private String shopName;
    private String email;
    private String phone;
    private String companyName;
    private String companyAddress;
    private String companyEmail;
    private String companyPhone;
}
