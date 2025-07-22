package com.sts.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserProfileDto {
    private Long userId;
    private Long shopId;
    private Integer availablePoints;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
