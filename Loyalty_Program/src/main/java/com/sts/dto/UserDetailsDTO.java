package com.sts.dto;

import lombok.Data;

@Data
public class UserDetailsDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
