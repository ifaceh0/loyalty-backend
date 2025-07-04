package com.sts.dto;

import lombok.Data;

@Data
public class ReferralInviteRequest {
    private String email;
    private Long shopId;
    private String shopName;
}
