package com.sts.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SpecialBonusDTO {
    private String name;
    private double points;
    private LocalDate startDate;
    private LocalDate endDate;
}
