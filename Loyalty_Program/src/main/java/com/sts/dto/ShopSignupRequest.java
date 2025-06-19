package com.sts.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ShopSignupRequest {
    private String shopName;
    @Email
    private String email;
    private String phone;
    private String companyName;
    private String companyAddress;
    @Email
    private String companyEmail;
    private String companyPhone;
    private String password;
}
