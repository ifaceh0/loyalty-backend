package com.sts.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserSignupRequest {
    private String firstName;
    private String lastName;
    private String phone;
    @Email
    private String email;
    private String password;
}
