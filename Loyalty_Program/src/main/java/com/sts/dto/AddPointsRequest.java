package com.sts.dto;

import lombok.Data;

@Data
public class AddPointsRequest {
    private Long userId;
    private Long shopId;
    private Integer pointsToAdd;
}
